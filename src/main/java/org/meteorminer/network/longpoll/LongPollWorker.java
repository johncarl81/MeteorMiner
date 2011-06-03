package org.meteorminer.network.longpoll;

import com.google.inject.assistedinject.Assisted;
import org.codehaus.jackson.JsonNode;
import org.meteorminer.config.binding.GetWorkMessage;
import org.meteorminer.config.binding.NetworkErrorPause;
import org.meteorminer.domain.Work;
import org.meteorminer.domain.WorkFactory;
import org.meteorminer.hash.WorkConsumer;
import org.meteorminer.network.JsonClient;
import org.meteorminer.output.CLInterface;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;

/**
 * @author John Ericksen
 */
public class LongPollWorker implements Runnable {

    private URL longPollWorkerUrl;
    private JsonClient jsonClient;
    private String getWorkRequest;
    private WorkFactory workFactory;
    private CLInterface output;
    private WorkConsumer workSource;
    private boolean running = true;
    private long errorWait;

    @Inject
    public LongPollWorker(@Assisted URL longPollWorkerUrl,
                          JsonClient jsonClient,
                          @GetWorkMessage String getWorkRequest,
                          WorkFactory workFactory,
                          CLInterface output, WorkConsumer workSource,
                          @NetworkErrorPause long netorkErrorPause) {
        this.longPollWorkerUrl = longPollWorkerUrl;
        this.jsonClient = jsonClient;
        this.getWorkRequest = getWorkRequest;
        this.workFactory = workFactory;
        this.output = output;
        this.workSource = workSource;
        this.errorWait = netorkErrorPause;
    }

    public void run() {
        do {
            try {
                JsonNode responseNode = jsonClient.execute("GetWork - Long Poll", getWorkRequest, longPollWorkerUrl);

                output.notification("Long poll received, pushing work");

                final Work work = workFactory.buildWork(responseNode);
                if (work != null) {
                    workSource.pushWork(work);
                }

            } catch (IOException e) {
                //pause until next loop
                try {
                    Thread.sleep(errorWait);
                } catch (InterruptedException e1) {
                    output.verbose("Error wait interrupted");
                }
                output.error(e);
            }
        } while (running);
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}

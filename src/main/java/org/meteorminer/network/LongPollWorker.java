package org.meteorminer.network;

import com.google.inject.assistedinject.Assisted;
import org.codehaus.jackson.JsonNode;
import org.meteorminer.config.binding.GetWorkMessage;
import org.meteorminer.domain.Work;
import org.meteorminer.domain.WorkFactory;
import org.meteorminer.hash.WorkConsumer;
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

    @Inject
    public LongPollWorker(@Assisted URL longPollWorkerUrl,
                          JsonClient jsonClient,
                          @GetWorkMessage String getWorkRequest,
                          WorkFactory workFactory,
                          CLInterface output, WorkConsumer workSource) {
        this.longPollWorkerUrl = longPollWorkerUrl;
        this.jsonClient = jsonClient;
        this.getWorkRequest = getWorkRequest;
        this.workFactory = workFactory;
        this.output = output;
        this.workSource = workSource;
    }

    public void run() {
        do {
            try {
                JsonNode responseNode = jsonClient.execute("GetWork - Long Poll", getWorkRequest, longPollWorkerUrl);

                output.notification("Long poll received.");

                final Work work = workFactory.buildWork(responseNode);
                workSource.setWork(work);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (running);
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}

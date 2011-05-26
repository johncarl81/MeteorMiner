package org.meteorminer.network;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.codehaus.jackson.JsonNode;
import org.meteorminer.config.binding.GetWorkMessage;
import org.meteorminer.domain.Work;
import org.meteorminer.domain.WorkFactory;
import org.meteorminer.hash.MinerController;
import org.meteorminer.output.CLInterface;

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
    private MinerController minerController;
    private CLInterface output;
    private LongPollWorkProducer longPollFactory;
    private boolean running = true;

    @Inject
    public LongPollWorker(@Assisted URL longPollWorkerUrl,
                          JsonClient jsonClient,
                          @GetWorkMessage String getWorkRequest,
                          WorkFactory workFactory,
                          MinerController minerController,
                          CLInterface output,
                          LongPollWorkProducer longPollFactory) {
        this.longPollWorkerUrl = longPollWorkerUrl;
        this.jsonClient = jsonClient;
        this.getWorkRequest = getWorkRequest;
        this.workFactory = workFactory;
        this.minerController = minerController;
        this.output = output;
        this.longPollFactory = longPollFactory;
    }

    public void run() {
        do {
            try {
                JsonNode responseNode = jsonClient.execute("GetWork - Long Poll", getWorkRequest, longPollWorkerUrl);

                output.notification("Long poll received.");

                final Work work = workFactory.buildWork(responseNode);
                longPollFactory.putWork(work);
                minerController.interruptProduction();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (running);
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}

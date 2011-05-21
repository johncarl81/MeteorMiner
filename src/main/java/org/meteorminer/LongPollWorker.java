package org.meteorminer;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.codehaus.jackson.JsonNode;
import org.meteorminer.binding.GetWorkMessage;
import org.meteorminer.binding.GetWorkTimeout;
import org.meteorminer.hash.MinerController;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author John Ericksen
 */
public class LongPollWorker implements Runnable {

    private URL longPollWorkerUrl;
    private BlockingQueue<Work> queue;
    private JsonClient jsonClient;
    private String getWorkRequest;
    private WorkFactory workFactory;
    private int getWorkTimeout;
    private MinerController minerController;

    @Inject
    public LongPollWorker(@Assisted URL longPollWorkerUrl,
                          BlockingQueue<Work> queue,
                          JsonClient jsonClient,
                          @GetWorkMessage String getWorkRequest,
                          @GetWorkTimeout int getWorkTimeout,
                          WorkFactory workFactory,
                          MinerController minerController) {
        this.longPollWorkerUrl = longPollWorkerUrl;
        this.queue = queue;
        this.jsonClient = jsonClient;
        this.getWorkRequest = getWorkRequest;
        this.workFactory = workFactory;
        this.getWorkTimeout = getWorkTimeout;
        this.minerController = minerController;
    }

    public void run() {
        while (true) {
            try {

                JsonNode responseNode = jsonClient.execute(getWorkRequest, longPollWorkerUrl);

                System.out.println("\rLong Poll");

                final Work work = workFactory.buildWork(responseNode);

                minerController.interruptProduction(new Runnable() {

                    public void run() {
                        try {
                            queue.offer(work, getWorkTimeout, TimeUnit.SECONDS);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        queue.clear();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

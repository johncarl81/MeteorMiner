package org.meteorminer.network;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.codehaus.jackson.JsonNode;
import org.meteorminer.binding.GetWorkMessage;
import org.meteorminer.domain.Work;
import org.meteorminer.domain.WorkFactory;
import org.meteorminer.hash.MinerController;
import org.meteorminer.logging.CLLogger;

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
    private CLLogger logger;
    private LongPollWorkProducer longPollFactory;

    @Inject
    public LongPollWorker(@Assisted URL longPollWorkerUrl,
                          JsonClient jsonClient,
                          @GetWorkMessage String getWorkRequest,
                          WorkFactory workFactory,
                          MinerController minerController,
                          CLLogger logger,
                          LongPollWorkProducer longPollFactory) {
        this.longPollWorkerUrl = longPollWorkerUrl;
        this.jsonClient = jsonClient;
        this.getWorkRequest = getWorkRequest;
        this.workFactory = workFactory;
        this.minerController = minerController;
        this.logger = logger;
        this.longPollFactory = longPollFactory;
    }

    public void run() {
        while (true) {
            try {
                JsonNode responseNode = jsonClient.execute(getWorkRequest, longPollWorkerUrl);

                logger.notification("Long poll received.");

                final Work work = workFactory.buildWork(responseNode);

                minerController.interruptProduction(new Runnable() {
                    public void run() {
                        longPollFactory.putWork(work);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

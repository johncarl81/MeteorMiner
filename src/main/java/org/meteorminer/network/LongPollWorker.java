package org.meteorminer.network;

import com.google.inject.assistedinject.Assisted;
import org.codehaus.jackson.JsonNode;
import org.meteorminer.config.binding.GetWorkMessage;
import org.meteorminer.domain.Work;
import org.meteorminer.domain.WorkFactory;
import org.meteorminer.hash.HashScanner;
import org.meteorminer.output.CLInterface;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class LongPollWorker implements Runnable {

    private URL longPollWorkerUrl;
    private JsonClient jsonClient;
    private String getWorkRequest;
    private WorkFactory workFactory;
    private CLInterface output;
    private LongPollWorkProducer longPollFactory;
    private boolean running = true;

    private Set<HashScanner> hashScanners;

    @Inject
    public LongPollWorker(@Assisted URL longPollWorkerUrl,
                          @Assisted Set<HashScanner> hashScanners,
                          JsonClient jsonClient,
                          @GetWorkMessage String getWorkRequest,
                          WorkFactory workFactory,
                          CLInterface output,
                          LongPollWorkProducer longPollFactory) {
        this.longPollWorkerUrl = longPollWorkerUrl;
        this.jsonClient = jsonClient;
        this.getWorkRequest = getWorkRequest;
        this.workFactory = workFactory;
        this.output = output;
        this.longPollFactory = longPollFactory;
        this.hashScanners = hashScanners;
    }

    public void run() {
        do {
            try {
                JsonNode responseNode = jsonClient.execute("GetWork - Long Poll", getWorkRequest, longPollWorkerUrl);

                output.notification("Long poll received.");

                final Work work = workFactory.buildWork(responseNode);
                longPollFactory.putWork(work);
                for (HashScanner scanner : hashScanners) {
                    scanner.stop();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (running);
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}

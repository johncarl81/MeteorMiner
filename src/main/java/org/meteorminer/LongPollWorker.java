package org.meteorminer;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.codehaus.jackson.JsonNode;
import org.meteorminer.binding.GetWorkMessage;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author John Ericksen
 */
public class LongPollWorker implements Runnable{

    private URL longPollWorkerUrl;
    private ArrayBlockingQueue<Work> queue;
    private JsonClient jsonClient;
    private String getWorkRequest;
    private WorkFactory workFactory;

    @Inject
    public LongPollWorker(@Assisted URL longPollWorkerUrl,
                          ArrayBlockingQueue<Work> queue,
                          JsonClient jsonClient,
                          @GetWorkMessage String getWorkRequest,
                          WorkFactory workFactory) {
        this.longPollWorkerUrl = longPollWorkerUrl;
        this.queue = queue;
        this.jsonClient = jsonClient;
        this.getWorkRequest = getWorkRequest;
        this.workFactory = workFactory;
    }

    public void run() {
        while(true){
            try {

                JsonNode responseNode = jsonClient.execute(getWorkRequest);

                System.out.println("Long Poll");

                Work work = workFactory.buildWork(responseNode);

                queue.clear();
                queue.add(work);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

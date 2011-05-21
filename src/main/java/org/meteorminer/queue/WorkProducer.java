package org.meteorminer.queue;

import com.google.inject.assistedinject.Assisted;
import org.codehaus.jackson.JsonNode;
import org.meteorminer.JsonClient;
import org.meteorminer.Work;
import org.meteorminer.WorkFactory;
import org.meteorminer.binding.GetWorkMessage;

import javax.inject.Inject;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * @author John Ericksen
 */
public class WorkProducer extends Producer<Work> {

    private String getWorkMessage;
    private JsonClient jsonClient;
    private WorkFactory workFactory;


    @Inject
    public WorkProducer(@Assisted BlockingQueue<Work> queue,
                        @GetWorkMessage String getWorkMessage,
                        JsonClient jsonClient,
                        WorkFactory workFactory) {
        super(queue);
        this.workFactory = workFactory;
        this.jsonClient = jsonClient;
        this.getWorkMessage = getWorkMessage;
    }

    @Override
    public Work produce() {
        try {

            JsonNode responseNode = jsonClient.execute(getWorkMessage);

            return workFactory.buildWork(responseNode);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

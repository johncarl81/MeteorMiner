package org.meteorminer.queue;

import org.codehaus.jackson.JsonNode;
import org.meteorminer.binding.GetWorkMessage;
import org.meteorminer.domain.Work;
import org.meteorminer.domain.WorkFactory;
import org.meteorminer.network.JsonClient;

import javax.inject.Inject;
import java.io.IOException;

/**
 * @author John Ericksen
 */
public class WorkProducerImpl implements WorkProducer {

    private String getWorkMessage;
    private JsonClient jsonClient;
    private WorkFactory workFactory;

    @Inject
    public WorkProducerImpl(
            @GetWorkMessage String getWorkMessage,
            JsonClient jsonClient,
            WorkFactory workFactory) {

        this.workFactory = workFactory;
        this.jsonClient = jsonClient;
        this.getWorkMessage = getWorkMessage;
    }

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

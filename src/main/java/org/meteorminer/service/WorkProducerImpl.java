package org.meteorminer.service;

import org.codehaus.jackson.JsonNode;
import org.meteorminer.config.binding.GetWorkMessage;
import org.meteorminer.config.binding.NetworkErrorPause;
import org.meteorminer.domain.Work;
import org.meteorminer.domain.WorkFactory;
import org.meteorminer.network.JsonClient;
import org.meteorminer.output.CLInterface;

import javax.inject.Inject;
import java.io.IOException;

/**
 * @author John Ericksen
 */
public class WorkProducerImpl implements WorkProducer {

    private String getWorkMessage;
    private JsonClient jsonClient;
    private WorkFactory workFactory;
    private CLInterface output;
    private long errorWait;

    @Inject
    public WorkProducerImpl(
            @GetWorkMessage String getWorkMessage,
            JsonClient jsonClient,
            WorkFactory workFactory,
            CLInterface output,
            @NetworkErrorPause long networkErrorPause) {

        this.workFactory = workFactory;
        this.jsonClient = jsonClient;
        this.getWorkMessage = getWorkMessage;
        this.output = output;
        this.errorWait = networkErrorPause;
    }

    public Work produce() {
        try {
            JsonNode responseNode = jsonClient.execute("GetWork", getWorkMessage);

            if (responseNode != null) {
                return workFactory.buildWork(responseNode);
            }

        } catch (IOException e) {
            output.error(e);
            try {
                Thread.sleep(errorWait);
            } catch (InterruptedException e1) {
                output.verbose("Error wait interrupted");
            }
        }

        return null;
    }
}

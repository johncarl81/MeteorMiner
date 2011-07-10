package org.meteorminer.service;

import org.meteorminer.config.binding.NetworkErrorPause;
import org.meteorminer.domain.Work;
import org.meteorminer.network.JsonClient;
import org.meteorminer.output.CLInterface;

import javax.inject.Inject;
import java.io.IOException;

/**
 * @author John Ericksen
 */
public class WorkProducerImpl implements WorkProducer {

    private JsonClient jsonClient;
    private CLInterface output;
    private long errorWait;
    private GetWorkMessageStrategy getWorkMessageStrategy;

    @Inject
    public WorkProducerImpl(
            JsonClient jsonClient,
            CLInterface output,
            @NetworkErrorPause long networkErrorPause,
            GetWorkMessageStrategy getWorkMessageStrategy) {

        this.jsonClient = jsonClient;
        this.output = output;
        this.errorWait = networkErrorPause;
        this.getWorkMessageStrategy = getWorkMessageStrategy;
    }

    public Work produce() {
        try {
            return jsonClient.execute(getWorkMessageStrategy);
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

package org.meteorminer.service;

import com.google.inject.Inject;
import org.meteorminer.domain.Work;
import org.meteorminer.network.LongPollWorkProducer;
import org.meteorminer.output.CLInterface;

/**
 * @author John Ericksen
 */
public class WorkProducerMultiplex implements WorkProducer {

    @Inject
    private LongPollWorkProducer longPollWorkProducer;
    @Inject
    private DelayedWorkProducer delayedWorkProducer;
    @Inject
    private WorkProducerImpl workProducer;
    @Inject
    private CLInterface output;

    @Override
    public Work produce() {
        if (longPollWorkProducer.hasWork()) {
            output.verbose("Producing from Long Poll work result");
            return longPollWorkProducer.produce();
        } else if (delayedWorkProducer.hasWork()) {
            output.verbose("Producing from Delayed work result");
            return delayedWorkProducer.produce();
        }

        output.verbose("Producing from Standard work result");
        return workProducer.produce();
    }
}

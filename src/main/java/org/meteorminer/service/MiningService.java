package org.meteorminer.service;

import com.google.inject.Inject;
import org.meteorminer.logging.CLInterface;
import org.meteorminer.logging.LoggingTimerTask;
import org.meteorminer.queue.WorkConsumerFactory;
import org.meteorminer.queue.WorkProducerFactory;

import java.util.Timer;

/**
 * @author John Ericksen
 */
public class MiningService {

    @Inject
    private WorkProducerFactory workProducerFactory;
    @Inject
    private WorkConsumerFactory workConsumerFactory;
    @Inject
    private LoggingTimerTask loggingTimerTask;
    @Inject
    private Timer timer;
    @Inject
    private CLInterface output;

    public void start() {
        timer.schedule(loggingTimerTask, 2000, 2000);

        while (true) {
            workConsumerFactory.createWorkConsumer().consume(workProducerFactory.createWorkProducer().produce());
        }
    }
}

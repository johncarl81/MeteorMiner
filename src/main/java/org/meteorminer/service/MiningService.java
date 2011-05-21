package org.meteorminer.service;

import com.google.inject.Inject;
import org.meteorminer.logging.CLLogger;
import org.meteorminer.logging.LoggingTimerTask;
import org.meteorminer.network.LongPollWorkProducer;
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
    private LongPollWorkProducer longPollWorkProducer;
    @Inject
    private WorkConsumerFactory workConsumerFactory;
    @Inject
    private LoggingTimerTask loggingTimerTask;
    @Inject
    private Timer timer;
    @Inject
    private CLLogger logger;

    public void start() {
        timer.schedule(loggingTimerTask, 2000, 2000);

        while (true) {
            workConsumerFactory.createWorkConsumer().consume(workProducerFactory.createWorkProducer().produce());

            //long poll
            if (longPollWorkProducer.hasWork()) {
                logger.verbose("Starting long poll work.");
                workConsumerFactory.createWorkConsumer().consume(longPollWorkProducer.produce());
            }

        }
    }
}

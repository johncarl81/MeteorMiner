package org.meteorminer.service;

import com.google.inject.Inject;
import org.meteorminer.hash.MinerController;
import org.meteorminer.output.CLInterface;
import org.meteorminer.output.LoggingTimerTask;

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
    @Inject
    private MinerController minerController;

    public void start() {
        timer.schedule(loggingTimerTask, 2000, 2000);

        while (true) {
            minerController.reset();
            workConsumerFactory.createWorkConsumer().consume(
                    workProducerFactory.createWorkProducer().produce());
        }
    }
}

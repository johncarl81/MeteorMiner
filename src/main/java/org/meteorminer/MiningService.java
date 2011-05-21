package org.meteorminer;

import com.google.inject.Inject;
import org.meteorminer.binding.GetWorkTimeout;
import org.meteorminer.queue.Consumer;
import org.meteorminer.queue.Producer;
import org.meteorminer.queue.WorkConsumerFactory;
import org.meteorminer.queue.WorkProducerFactory;
import org.meteorminer.stats.LoggingTimerTask;

import java.util.Timer;
import java.util.concurrent.BlockingQueue;

/**
 * @author John Ericksen
 */
public class MiningService {

    @Inject
    private WorkProducerFactory workProducerFactory;
    @Inject
    private WorkConsumerFactory workConsumerFactory;
    @Inject
    private BlockingQueue<Work> queue;
    @Inject
    private LoggingTimerTask loggingTimerTask;
    @Inject
    private Timer timer;
    @Inject
    @GetWorkTimeout
    private int timeout;

    public void start() {
        timer.schedule(loggingTimerTask, 2000, 2000);

        Producer producer = workProducerFactory.createWorkProducer(queue);
        new Thread(producer).start();
        
        int consumerCount = 1;
        for (int i = 0; i < consumerCount; i++) {
            Consumer consumer = workConsumerFactory.createWorkConsumer(queue);
            new Thread(consumer).start();
        }




    }
}

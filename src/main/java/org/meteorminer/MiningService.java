package org.meteorminer;

import com.google.inject.Inject;
import org.meteorminer.queue.Consumer;
import org.meteorminer.queue.Producer;
import org.meteorminer.queue.WorkConsumerFactory;
import org.meteorminer.queue.WorkProducerFactory;

import java.util.Timer;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author John Ericksen
 */
public class MiningService {

    @Inject
    private WorkProducerFactory workProducerFactory;
    @Inject
    private WorkConsumerFactory workConsumerFactory;
    @Inject
    private ArrayBlockingQueue<Work> queue;
    @Inject
    private LoggingTimerTask loggingTimerTask;

    public void start() {
        Producer producer = workProducerFactory.createWorkProducer(queue);
        new Thread(producer).start();

        for(int i = 0; i < 2; i++){
            Consumer consumer = workConsumerFactory.createWorkConsumer(queue);
            new Thread(consumer).start();
        }

        Timer timer = new Timer();
        timer.schedule(loggingTimerTask, 2000, 2000);
    }
}

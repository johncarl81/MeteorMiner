package org.meteorminer.service;

import com.google.inject.Inject;
import org.meteorminer.binding.GetWorkTimeout;
import org.meteorminer.domain.Work;
import org.meteorminer.logging.LoggingTimerTask;
import org.meteorminer.queue.WorkConsumerFactory;
import org.meteorminer.queue.WorkProducerFactory;

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

        while(true){
            workConsumerFactory.createWorkConsumer().consume(workProducerFactory.createWorkProducer().produce());
        }

        /*Producer producer = workProducerFactory.createWorkProducer(queue);
        new Thread(producer).start();
        
        int consumerCount = 4;
        for (int i = 0; i < consumerCount; i++) {
            Consumer consumer = workConsumerFactory.createWorkConsumer(queue);
            new Thread(consumer).start();
            try{
                Thread.sleep(5000 / 4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/




    }
}

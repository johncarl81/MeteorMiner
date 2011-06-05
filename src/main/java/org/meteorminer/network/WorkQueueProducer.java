package org.meteorminer.network;

import com.google.inject.Inject;
import org.meteorminer.domain.Work;
import org.meteorminer.output.CLInterface;
import org.meteorminer.service.WorkProducerImpl;

import java.util.concurrent.BlockingQueue;

/**
 * @author John Ericksen
 */
public class WorkQueueProducer implements Runnable {

    @Inject
    private BlockingQueue<Work> workQueue;
    @Inject
    private WorkProducerImpl workProducer;
    @Inject
    private CLInterface output;

    private boolean running = true;

    @Override
    public void run() {
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                Work work = workProducer.produce();
                if (work != null) {
                    work.updateTime();
                    workQueue.put(work);
                }
            } catch (InterruptedException e) {
                output.error(e);
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}

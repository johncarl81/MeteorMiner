package org.meteorminer.network;

import com.google.inject.Inject;
import org.meteorminer.domain.Work;
import org.meteorminer.output.CLInterface;
import org.meteorminer.service.WorkProducer;

import java.util.concurrent.BlockingQueue;

/**
 * Work Producer that forwards all produced work into the contained work queue.
 *
 * @author John Ericksen
 */
public class WorkQueueProducer implements Runnable {

    @Inject
    private BlockingQueue<Work> workQueue;
    @Inject
    private WorkProducer workProducer;
    @Inject
    private CLInterface output;

    private boolean running = true;

    @Override
    public void run() {
        do {
            try {
                Work work = workProducer.produce();
                if (work != null) {
                    workQueue.put(work);
                }
            } catch (InterruptedException e) {
                output.error(e);
            }
        } while (running && !Thread.currentThread().isInterrupted());
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setWorkQueue(BlockingQueue<Work> workQueue) {
        this.workQueue = workQueue;
    }

    public void setWorkProducer(WorkProducer workProducer) {
        this.workProducer = workProducer;
    }

    public void setOutput(CLInterface output) {
        this.output = output;
    }
}

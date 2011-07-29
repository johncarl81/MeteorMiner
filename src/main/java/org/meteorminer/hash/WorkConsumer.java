package org.meteorminer.hash;

import com.google.inject.Inject;
import org.meteorminer.config.ServerProvider;
import org.meteorminer.domain.Work;
import org.meteorminer.network.InterruptTimerTask;
import org.meteorminer.output.CLInterface;
import org.meteorminer.output.StatisticsHolder;
import org.meteorminer.service.WorkProducerImpl;

import javax.inject.Provider;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author John Ericksen
 */
public class WorkConsumer {

    private static final long LAST_NONCE = 0xFFFFFFFFL;

    @Inject
    private AtomicLong nonce;
    @Inject
    private AtomicReference<Work> work;
    @Inject
    private Timer timer;
    @Inject
    private Provider<InterruptTimerTask> interruptTimerTaskProvider;
    @Inject
    private WorkProducerImpl workProducer;
    @Inject
    private BlockingQueue<Work> workQueue;
    @Inject
    private CLInterface output;
    @Inject
    private StatisticsHolder statistics;
    @Inject
    private ServerProvider serverProvider;

    private InterruptTimerTask interruptTimerTask;

    public Integer reserveNext(int workSize) {
        long nextNonce = nonce.getAndAdd(workSize);

        if (nextNonce > LAST_NONCE || work == null) {
            output.verbose("Updating work because exhausted nonce");
            updateWork();
        }

        return (int) nextNonce;
    }

    public void updateWork() {
        try {
            setWork(workQueue.take());
        } catch (InterruptedException e) {
            output.error(e);
        }
    }

    public void pushWork(Work work) {
        //flag current work as stale
        Work currentWork = getWork();
        if (currentWork != null) {
            currentWork.setStale(true);
        }

        setWork(work);
        workQueue.clear();
    }

    private void setWork(Work work) {
        this.work.set(work);

        //reset
        nonce.set(0);
        //reset getwork timeout
        if (interruptTimerTask != null) {
            interruptTimerTask.cancel();
        }
        interruptTimerTask = interruptTimerTaskProvider.get();
        timer.schedule(interruptTimerTask, serverProvider.get().getGetWorkTimeout());
    }

    public Work getWork() {
        return this.work.get();
    }
}

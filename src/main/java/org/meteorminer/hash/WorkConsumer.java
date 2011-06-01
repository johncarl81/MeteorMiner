package org.meteorminer.hash;

import com.google.inject.Inject;
import org.meteorminer.config.binding.GetWorkTimeout;
import org.meteorminer.domain.Work;
import org.meteorminer.network.InterruptTimerTask;
import org.meteorminer.service.WorkProducerImpl;

import javax.inject.Provider;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author John Ericksen
 */
public class WorkConsumer {

    private static final long LAST_NONCE = 0xFFFFFFFFL;
    private AtomicLong nonce = new AtomicLong();
    private AtomicReference<Work> work = new AtomicReference<Work>();
    @Inject
    private Timer timer;
    @Inject
    private Provider<InterruptTimerTask> interruptTimerTaskProvider;
    @Inject
    @GetWorkTimeout
    private long getWorkTimeout;
    @Inject
    private WorkProducerImpl workProducer;

    private InterruptTimerTask interruptTimerTask;

    public Integer reserveNext(int workSize) {
        long nextNonce = nonce.getAndAdd(workSize);

        if (nextNonce > LAST_NONCE) {
            //todo:updateWork();
        }

        return (int) nextNonce;
    }

    public void updateWork() {
        setWork(workProducer.produce());
    }

    public void setWork(Work work) {
        this.work.set(work);
        reset();
    }

    public void reset() {
        nonce.set(0);
        //reset getwork timeout
        if (interruptTimerTask != null) {
            interruptTimerTask.cancel();
        }
        interruptTimerTask = interruptTimerTaskProvider.get();
        timer.schedule(interruptTimerTask, getWorkTimeout);
    }

    public Work getWork() {
        return this.work.get();
    }
}

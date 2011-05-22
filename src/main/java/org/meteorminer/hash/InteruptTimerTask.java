package org.meteorminer.hash;

import com.google.inject.assistedinject.Assisted;
import org.meteorminer.queue.DelayedWorkProducer;

import javax.inject.Inject;
import java.lang.ref.WeakReference;
import java.util.TimerTask;

/**
 * @author John Ericksen
 */
public class InteruptTimerTask extends TimerTask {

    private WeakReference<LocalMinerController> localControllerRef;
    private DelayedWorkProducer workProducer;

    @Inject
    public InteruptTimerTask(@Assisted LocalMinerController localController, DelayedWorkProducer workProducer) {
        this.localControllerRef = new WeakReference<LocalMinerController>(localController);
        this.workProducer = workProducer;
    }

    @Override
    public void run() {
        LocalMinerController localController = localControllerRef.get();
        if (localController != null) {
            //request new work..
            workProducer.delayedProduce();
            //and stop production to pick up new work
            localController.stopProduction();
        }
    }
}

package org.meteorminer.hash;

import com.google.inject.assistedinject.Assisted;
import org.meteorminer.service.DelayedWorkProducer;

import javax.inject.Inject;
import java.util.TimerTask;

/**
 * @author John Ericksen
 */
public class InteruptTimerTask extends TimerTask {

    private MinerController minerController;
    private DelayedWorkProducer workProducer;

    @Inject
    public InteruptTimerTask(@Assisted MinerController minerController, DelayedWorkProducer workProducer) {
        this.minerController = minerController;
        this.workProducer = workProducer;
    }

    @Override
    public void run() {
        //request new work..
        workProducer.delayedProduce();
        //and stop production to pick up new work
        minerController.interruptProduction();
    }
}

package org.meteorminer.hash;

import com.google.inject.Inject;
import org.meteorminer.config.binding.GetWorkTimeout;
import org.meteorminer.domain.Work;
import org.meteorminer.service.WorkFoundCallback;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author John Ericksen
 */
public abstract class AbstractHashScanner implements HashScanner {

    @Inject
    private Timer timer;
    @Inject
    @GetWorkTimeout
    private int getWorkTimeout;
    @Inject
    private InteruptTimerTaskFactory interuptTimerTaskFactory;
    @Inject
    private HashStatisticsOutputTimerTaskFactory hashStatisticsOutputTimerTaskFactory;
    @Inject
    private MinerController controller;

    @Override
    public void scan(Work work, WorkFoundCallback workFoundCallback) {
        controller.reset();
        TimerTask hashStatsTimerTask = hashStatisticsOutputTimerTaskFactory.buildStatisticsOutputTimerTask(this);
        TimerTask interuptTimerTask = interuptTimerTaskFactory.buildInteruptTimerTask(controller);
        timer.schedule(hashStatsTimerTask, 1000, 1000);
        timer.schedule(interuptTimerTask, getWorkTimeout * 1000);

        innerScan(work, workFoundCallback);

        hashStatsTimerTask.cancel();
        interuptTimerTask.cancel();
    }

    public MinerController getController() {
        return controller;
    }

    public abstract void innerScan(Work work, WorkFoundCallback workFoundCallback);

    public abstract long getNonceCount();
}

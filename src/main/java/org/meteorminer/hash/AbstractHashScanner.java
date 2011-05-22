package org.meteorminer.hash;

import com.google.inject.Inject;
import org.meteorminer.binding.GetWorkTimeout;
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
    private LocalMinerController localController;

    @Override
    public void scan(Work work, WorkFoundCallback workFoundCallback) {
        TimerTask hashStatsTimerTask = hashStatisticsOutputTimerTaskFactory.buildStatisticsOutputTimerTask(this);
        TimerTask interuptTimerTask = interuptTimerTaskFactory.buildInteruptTimerTask(localController);
        timer.schedule(hashStatsTimerTask, 1000, 1000);
        timer.schedule(interuptTimerTask, getWorkTimeout * 1000);

        innerScan(work, workFoundCallback);

        hashStatsTimerTask.cancel();
        interuptTimerTask.cancel();
    }

    public LocalMinerController getLocalController() {
        return localController;
    }

    public abstract void innerScan(Work work, WorkFoundCallback workFoundCallback);

    public abstract long getNonceCount();
}

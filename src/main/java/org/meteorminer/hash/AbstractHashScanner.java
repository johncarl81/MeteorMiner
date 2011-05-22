package org.meteorminer.hash;

import com.google.inject.Inject;
import org.meteorminer.binding.GetWorkTimeout;
import org.meteorminer.domain.Work;
import org.meteorminer.logging.Statistics;
import org.meteorminer.queue.WorkFoundCallback;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author John Ericksen
 */
public abstract class AbstractHashScanner implements HashScanner {

    @Inject
    private Timer timer;
    @Inject
    private Statistics statistics;
    @Inject
    @GetWorkTimeout
    private int getWorkTimeout;
    @Inject
    private LocalMinerController localController;
    @Inject
    private MinerController minerController;
    @Inject
    private InteruptTimerTaskFactory interuptTimerTaskFactory;
    @Inject
    private HashStatisticsOutputTimerTaskFactory hashStatisticsOutputTimerTaskFactory;

    @Override
    public void scan(Work work, WorkFoundCallback workFoundCallback) {

        TimerTask hashStatsTimerTask = hashStatisticsOutputTimerTaskFactory.buildStatisticsOutputTimerTask(this);
        TimerTask interuptTimerTask = interuptTimerTaskFactory.buildInteruptTimerTask(localController);
        timer.schedule(hashStatsTimerTask, 1000, 1000);
        timer.schedule(interuptTimerTask, getWorkTimeout * 1000);

        innerScan(work, workFoundCallback);

        hashStatsTimerTask.cancel();
        interuptTimerTask.cancel();
        minerController.unregister(localController);
    }

    public LocalMinerController getLocalController() {
        return localController;
    }

    public abstract void innerScan(Work work, WorkFoundCallback workFoundCallback);

    public abstract long getNonceCount();
}

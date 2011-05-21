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
    @javax.inject.Inject
    private MinerController minerController;

    @Override
    public void scan(Work work, WorkFoundCallback workFoundCallback) {

        TimerTask hashStatsTimerTask = new HashStatisticsOutputTimerTask(this, statistics);
        TimerTask interuptTimerTask = new InteruptTimerTask(localController);
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

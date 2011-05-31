package org.meteorminer.hash;

import org.meteorminer.config.binding.GetWorkTimeout;
import org.meteorminer.domain.Work;

import javax.inject.Inject;
import java.util.Timer;

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
    private InterruptTimerTaskFactory interruptTimerTaskFactory;
    @Inject
    private HashStatisticsOutputTimerTaskFactory hashStatisticsOutputTimerTaskFactory;

    private boolean stop;

    @Override
    public void scan(Work work) {
        stop = false;
        HashStatisticsOutputTimerTask hashStatsTimerTask = hashStatisticsOutputTimerTaskFactory.buildStatisticsOutputTimerTask(this);
        timer.schedule(hashStatsTimerTask, 1000, 1000);

        innerScan(work);

        hashStatsTimerTask.cancel();
    }

    public void stop() {
        stop = true;
    }

    public boolean isStop() {
        return stop;
    }

    public abstract void innerScan(Work work);
}

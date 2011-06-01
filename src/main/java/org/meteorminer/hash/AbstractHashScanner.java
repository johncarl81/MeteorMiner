package org.meteorminer.hash;

import javax.inject.Inject;
import java.util.Timer;

/**
 * @author John Ericksen
 */
public abstract class AbstractHashScanner implements HashScanner {

    private static final long STATS_UPDATE = 1000;

    @Inject
    private Timer timer;
    @Inject
    private HashStatisticsOutputTimerTaskFactory hashStatisticsOutputTimerTaskFactory;

    private boolean stop;

    @Override
    public void run() {
        stop = false;
        HashStatisticsOutputTimerTask hashStatsTimerTask = hashStatisticsOutputTimerTaskFactory.buildStatisticsOutputTimerTask(this);
        timer.schedule(hashStatsTimerTask, STATS_UPDATE, STATS_UPDATE);

        innerScan();

        hashStatsTimerTask.cancel();
    }

    public void stop() {
        stop = true;
    }

    public boolean isStop() {
        return stop;
    }

    public abstract void innerScan();
}

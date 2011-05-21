package org.meteorminer.stats;

import javax.inject.Inject;
import java.util.TimerTask;

/**
 * @author John Ericksen
 */
public class LoggingTimerTask extends TimerTask {

    @Inject
    private Statistics statistics;

    @Override
    public void run() {
        System.out.printf("\r%1.2f mhash/sec %2d pass %3d fail", statistics.getHashRate(), statistics.getWorkPassed(), statistics.getWorkFailed());
    }
}

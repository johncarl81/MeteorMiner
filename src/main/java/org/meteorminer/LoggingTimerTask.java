package org.meteorminer;

import org.meteorminer.stats.Statistics;

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
        System.out.println(statistics.getHashRate());
    }
}

package org.meteorminer.hash;

import com.google.inject.assistedinject.Assisted;
import org.meteorminer.logging.Statistics;

import javax.inject.Inject;
import java.util.TimerTask;

/**
 * @author John Ericksen
 */
public class HashStatisticsOutputTimerTask extends TimerTask {

    private HashScanner scanner;
    private long previousNonceCount;
    private Statistics statistics;

    @Inject
    public HashStatisticsOutputTimerTask(@Assisted HashScanner scanner, Statistics statistics) {
        this.scanner = scanner;
        this.statistics = statistics;
        this.previousNonceCount = 0;
    }

    @Override
    public void run() {
        long currentHashCount = scanner.getNonceCount();
        statistics.incrementHashCount(currentHashCount - previousNonceCount);
        previousNonceCount = currentHashCount;
    }
}

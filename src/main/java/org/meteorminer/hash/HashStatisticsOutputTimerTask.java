package org.meteorminer.hash;

import com.google.inject.assistedinject.Assisted;
import org.meteorminer.logging.Statistics;

import javax.inject.Inject;
import java.lang.ref.WeakReference;
import java.util.TimerTask;

/**
 * @author John Ericksen
 */
public class HashStatisticsOutputTimerTask extends TimerTask {

    private WeakReference<HashScanner> scannerRef;
    private long previousNonceCount;
    private Statistics statistics;

    @Inject
    public HashStatisticsOutputTimerTask(@Assisted HashScanner scanner, Statistics statistics) {
        this.scannerRef = new WeakReference<HashScanner>(scanner);
        this.statistics = statistics;
        this.previousNonceCount = 0;
    }

    @Override
    public void run() {
        HashScanner scanner = scannerRef.get();
        if (scanner != null) {
            long currentHashCount = scanner.getNonceCount();
            statistics.incrementHashCount(currentHashCount - previousNonceCount);
            previousNonceCount = currentHashCount;
        }
    }

}

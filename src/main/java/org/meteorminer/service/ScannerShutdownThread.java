package org.meteorminer.service;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.meteorminer.hash.HashScanner;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author John Ericksen
 */
public class ScannerShutdownThread extends Thread {

    private ExecutorService executor;
    private Set<HashScanner> scanners;

    @Inject
    public ScannerShutdownThread(@Assisted ExecutorService executor, @Assisted Set<HashScanner> scanners) {
        this.executor = executor;
        this.scanners = scanners;
    }

    @Override
    public void run() {
        executor.shutdown();

        for (HashScanner scanner : scanners) {
            scanner.stop();
        }
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

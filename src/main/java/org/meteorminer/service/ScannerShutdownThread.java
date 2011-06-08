package org.meteorminer.service;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.meteorminer.hash.HashScanner;
import org.meteorminer.output.CLInterface;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Scanner shutdown functionality.
 *
 * @author John Ericksen
 */
public class ScannerShutdownThread extends Thread {

    private ExecutorService executor;
    private Set<HashScanner> scanners;
    private CLInterface output;

    @Inject
    public ScannerShutdownThread(@Assisted ExecutorService executor, @Assisted Set<HashScanner> scanners, CLInterface output) {
        this.executor = executor;
        this.scanners = scanners;
        this.output = output;
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
            output.error(e);
        }
    }
}

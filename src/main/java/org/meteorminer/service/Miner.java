package org.meteorminer.service;

import com.google.inject.assistedinject.Assisted;
import org.meteorminer.hash.HashScanner;
import org.meteorminer.hash.WorkConsumer;

import javax.inject.Inject;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author John Ericksen
 */
public class Miner implements Runnable {

    private WorkConsumer workSource;
    private Set<HashScanner> scanners;

    private ExecutorService executor;

    @Inject
    public Miner(@Assisted Set<HashScanner> scanners,
                 WorkConsumer workSource) {

        this.workSource = workSource;
        this.scanners = scanners;
        this.executor = Executors.newFixedThreadPool(scanners.size());
    }

    public void run() {
        //initial production
        workSource.updateWork();

        for (HashScanner scanner : scanners) {
            executor.execute(scanner);
        }
    }
}

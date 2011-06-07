package org.meteorminer.service;

import com.google.inject.assistedinject.Assisted;
import org.meteorminer.hash.HashScanner;
import org.meteorminer.hash.WorkConsumer;
import org.meteorminer.network.WorkQueueProducer;
import org.meteorminer.output.CLInterface;

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
    private WorkQueueProducer workQueueProducer;
    private AsynchronousFactory asynchronousFactory;
    private ScannerShutdownFactory scannerShutdownFactory;

    private ExecutorService executor;

    private boolean shutdownHookAdded = false;

    @Inject
    public Miner(@Assisted Set<HashScanner> scanners,
                 WorkConsumer workSource, WorkQueueProducer workQueueProducer, AsynchronousFactory asynchronousFactory, CLInterface output, ScannerShutdownFactory scannerShutdownFactory) {

        this.workSource = workSource;
        this.scanners = scanners;
        this.workQueueProducer = workQueueProducer;
        this.asynchronousFactory = asynchronousFactory;
        this.scannerShutdownFactory = scannerShutdownFactory;
        this.executor = Executors.newFixedThreadPool(scanners.size());
    }

    public void run() {

        asynchronousFactory.startRunnableWithGracefulShutdown(workQueueProducer);
        //initial production
        workSource.updateWork();

        for (HashScanner scanner : scanners) {
            executor.execute(scanner);
        }

        if (!shutdownHookAdded) {
            shutdownHookAdded = true;
            Runtime.getRuntime().addShutdownHook(scannerShutdownFactory.buildScannerShutdown(executor, scanners));
        }
    }
}

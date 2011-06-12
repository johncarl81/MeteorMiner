package org.meteorminer.service;

import com.google.inject.assistedinject.Assisted;
import org.meteorminer.config.module.ThreadPoolFactory;
import org.meteorminer.hash.HashScanner;
import org.meteorminer.hash.WorkConsumer;
import org.meteorminer.network.WorkQueueProducer;
import org.meteorminer.output.CLInterface;

import javax.inject.Inject;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * The heart of the application.  The miner orchestrates the execution of the hash scanners
 * with the startup of the work producers and various asynchronous operations around the two.
 *
 * @author John Ericksen
 */
public class Miner implements Runnable {

    private WorkConsumer workSource;
    private Set<HashScanner> scanners;
    private WorkQueueProducer workQueueProducer;
    private AsynchronousFactory asynchronousFactory;
    private ExecutorService executor;

    @Inject
    public Miner(@Assisted Set<HashScanner> scanners,
                 WorkConsumer workSource, WorkQueueProducer workQueueProducer,
                 AsynchronousFactory asynchronousFactory, CLInterface output,
                 ScannerShutdownFactory scannerShutdownFactory,
                 Runtime runtime,
                 ThreadPoolFactory threadPoolFactory) {

        this.workSource = workSource;
        this.scanners = scanners;
        this.workQueueProducer = workQueueProducer;
        this.asynchronousFactory = asynchronousFactory;
        this.executor = threadPoolFactory.getFixedThreadPool(scanners.size());
        runtime.addShutdownHook(scannerShutdownFactory.buildScannerShutdown(executor, scanners));
    }

    public void run() {

        asynchronousFactory.startRunnable(workQueueProducer);
        //initial production
        workSource.updateWork();

        for (HashScanner scanner : scanners) {
            executor.execute(scanner);
        }
    }
}

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
import java.util.concurrent.TimeUnit;

/**
 * @author John Ericksen
 */
public class Miner implements Runnable {

    private WorkConsumer workSource;
    private Set<HashScanner> scanners;
    private WorkQueueProducer workQueueProducer;
    private AsynchronousFactory asynchronousFactory;
    private CLInterface output;

    private ExecutorService executor;

    @Inject
    public Miner(@Assisted Set<HashScanner> scanners,
                 WorkConsumer workSource, WorkQueueProducer workQueueProducer, AsynchronousFactory asynchronousFactory, CLInterface output) {

        this.workSource = workSource;
        this.scanners = scanners;
        this.workQueueProducer = workQueueProducer;
        this.asynchronousFactory = asynchronousFactory;
        this.output = output;
        this.executor = Executors.newFixedThreadPool(scanners.size());
    }

    public void run() {

        asynchronousFactory.startRunnable(workQueueProducer);
        //initial production
        workSource.updateWork();

        for (HashScanner scanner : scanners) {
            executor.execute(scanner);
        }


        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                output.notification("Shutting Down Executor");
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
        });
    }
}

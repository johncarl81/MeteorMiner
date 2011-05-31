package org.meteorminer.service;

import com.google.inject.assistedinject.Assisted;
import org.meteorminer.config.binding.GetWorkTimeout;
import org.meteorminer.domain.Work;
import org.meteorminer.hash.HashScanner;
import org.meteorminer.hash.InterruptTimerTask;
import org.meteorminer.hash.InterruptTimerTaskFactory;
import org.meteorminer.hash.NonceSource;
import org.meteorminer.network.LongPollAdaptor;
import org.meteorminer.network.LongPollWorker;
import org.meteorminer.network.LongPollWorkerFactory;
import org.meteorminer.output.CLInterface;

import javax.inject.Inject;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author John Ericksen
 */
public class Miner implements Runnable {

    private WorkProducerMultiplex workProducer;
    private NonceSource nonceSource;
    private CLInterface output;
    private InterruptTimerTaskFactory interruptTimerTaskFactory;
    private Timer timer;
    private int getWorkTimeout;
    private LongPollAdaptor longPollAdaptor;
    private AsynchronousFactory asynchronousFactory;
    private LongPollWorkerFactory longPollWorkerFactory;
    private boolean longPollEnabled = false;

    private Set<HashScanner> scanners;

    private boolean mining = true;

    @Inject
    public Miner(@Assisted Set<HashScanner> scanners,
                 WorkProducerMultiplex workProducer, NonceSource nonceSource,
                 CLInterface output, InterruptTimerTaskFactory interruptTimerTaskFactory, Timer timer,
                 @GetWorkTimeout int getWorkTimeout, LongPollAdaptor longPollAdaptor, AsynchronousFactory asynchronousFactory, LongPollWorkerFactory longPollWorkerFactory) {
        this.workProducer = workProducer;
        this.nonceSource = nonceSource;
        this.output = output;
        this.scanners = scanners;
        this.interruptTimerTaskFactory = interruptTimerTaskFactory;
        this.timer = timer;
        this.getWorkTimeout = getWorkTimeout;
        this.longPollAdaptor = longPollAdaptor;
        this.asynchronousFactory = asynchronousFactory;
        this.longPollWorkerFactory = longPollWorkerFactory;
    }

    public void run() {

        do {
            nonceSource.reset();
            Work work = workProducer.produce();

            //setup long poll
            if (!longPollEnabled && longPollAdaptor.getLongPollURL() != null) {
                longPollEnabled = true;
                LongPollWorker longPollWorker = longPollWorkerFactory.buildLongPollWorker(longPollAdaptor.getLongPollURL(), scanners);
                asynchronousFactory.startRunnable(longPollWorker);
            }

            InterruptTimerTask interruptTimerTask = interruptTimerTaskFactory.buildInteruptTimerTask(scanners, workProducer.getDelayedWorkProducer());

            ExecutorService executor = Executors.newFixedThreadPool(scanners.size());

            if (work != null) {
                output.verbose("Starting mine: " + work.getDataString());
                CyclicBarrier barrier = new CyclicBarrier(scanners.size() + 1);
                timer.schedule(interruptTimerTask, getWorkTimeout * 1000);
                for (HashScanner hashScanner : scanners) {
                    //asynchronousFactory.startRunnable(new HashScannerRunnable(hashScanner, work, barrier));
                    executor.execute(new HashScannerRunnable(hashScanner, work));
                }

                try {
                    executor.shutdown();
                    executor.awaitTermination(1, TimeUnit.DAYS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            interruptTimerTask.cancel();
        } while (mining);
    }

    public void setMining(boolean mining) {
        this.mining = mining;
    }
}

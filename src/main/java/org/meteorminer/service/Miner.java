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
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private Set<HashScannerCallable> wrappedScanners;

    private boolean mining = true;

    private ExecutorService executor;

    @Inject
    public Miner(@Assisted Set<HashScanner> scanners,
                 WorkProducerMultiplex workProducer, NonceSource nonceSource,
                 CLInterface output, InterruptTimerTaskFactory interruptTimerTaskFactory, Timer timer,
                 @GetWorkTimeout int getWorkTimeout, LongPollAdaptor longPollAdaptor, AsynchronousFactory asynchronousFactory, LongPollWorkerFactory longPollWorkerFactory) {
        this.workProducer = workProducer;
        this.nonceSource = nonceSource;
        this.output = output;
        this.scanners = scanners;
        this.wrappedScanners = new HashSet<HashScannerCallable>();

        for (HashScanner scanner : scanners) {
            this.wrappedScanners.add(new HashScannerCallable(scanner));
        }

        this.interruptTimerTaskFactory = interruptTimerTaskFactory;
        this.timer = timer;
        this.getWorkTimeout = getWorkTimeout;
        this.longPollAdaptor = longPollAdaptor;
        this.asynchronousFactory = asynchronousFactory;
        this.longPollWorkerFactory = longPollWorkerFactory;
        this.executor = Executors.newFixedThreadPool(scanners.size());
    }

    public void run() {
        try {
            do {
                nonceSource.reset();
                Work work = workProducer.produce();

                //setup long poll once available
                if (!longPollEnabled && longPollAdaptor.getLongPollURL() != null) {
                    longPollEnabled = true;
                    LongPollWorker longPollWorker = longPollWorkerFactory.buildLongPollWorker(longPollAdaptor.getLongPollURL(), scanners);
                    asynchronousFactory.startRunnable(longPollWorker);
                }

                InterruptTimerTask interruptTimerTask = interruptTimerTaskFactory.buildInteruptTimerTask(scanners, workProducer.getDelayedWorkProducer());

                if (work != null) {
                    output.verbose("Starting mine: " + work.getDataString());

                    //setup getwork timeout
                    timer.schedule(interruptTimerTask, getWorkTimeout * 1000);

                    executor.invokeAll(updateWrappedScanners(work));
                }

                interruptTimerTask.cancel();
            } while (mining);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Set<HashScannerCallable> updateWrappedScanners(Work work) {
        for (HashScannerCallable hashScannerCallable : wrappedScanners) {
            hashScannerCallable.setWork(work);
        }

        return wrappedScanners;
    }

    public void setMining(boolean mining) {
        this.mining = mining;
    }
}

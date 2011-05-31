package org.meteorminer.hash;

import com.google.inject.assistedinject.Assisted;
import org.meteorminer.service.DelayedWorkProducer;

import javax.inject.Inject;
import java.util.Set;
import java.util.TimerTask;

/**
 * @author John Ericksen
 */
public class InterruptTimerTask extends TimerTask {

    private Set<HashScanner> hashScanners;
    private DelayedWorkProducer workProducer;

    @Inject
    public InterruptTimerTask(@Assisted Set<HashScanner> hashScanners, @Assisted DelayedWorkProducer workProducer) {
        this.hashScanners = hashScanners;
        this.workProducer = workProducer;
    }

    @Override
    public void run() {
        //request new work..
        workProducer.delayedProduce();
        //and stop production to pick up new work
        for (HashScanner scanner : hashScanners) {
            scanner.stop();
        }
    }
}

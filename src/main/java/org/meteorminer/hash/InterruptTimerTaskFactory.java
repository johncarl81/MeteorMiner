package org.meteorminer.hash;

import org.meteorminer.service.DelayedWorkProducer;

import java.util.Set;

/**
 * Assisted Injection InteruptTimerTask Factory
 *
 * @author John Ericksen
 */
public interface InterruptTimerTaskFactory {

    InterruptTimerTask buildInteruptTimerTask(Set<HashScanner> hashScanners, DelayedWorkProducer delayedWorkProducer);
}

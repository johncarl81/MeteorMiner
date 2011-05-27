package org.meteorminer.hash;

/**
 * Assisted Injection InteruptTimerTask Factory
 *
 * @author John Ericksen
 */
public interface InteruptTimerTaskFactory {

    InteruptTimerTask buildInteruptTimerTask(MinerController localController);
}

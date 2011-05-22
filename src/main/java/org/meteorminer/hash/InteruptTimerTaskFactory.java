package org.meteorminer.hash;

/**
 * @author John Ericksen
 */
public interface InteruptTimerTaskFactory {

    InteruptTimerTask buildInteruptTimerTask(LocalMinerController localController);
}

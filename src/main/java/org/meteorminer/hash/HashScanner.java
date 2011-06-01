package org.meteorminer.hash;

/**
 * @author John Ericksen
 */
public interface HashScanner extends Runnable {

    long getNonceCount();

    void stop();
}

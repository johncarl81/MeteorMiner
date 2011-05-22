package org.meteorminer.service;

import org.meteorminer.domain.Work;
import org.meteorminer.hash.HashScanner;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class Miner {

    @Inject
    HashScanner scanner;
    @Inject
    WorkFoundCallback workFoundCallback;

    public void mine(Work work) {

        // Nonce is a number which starts at 0 and increments until 0xFFFFFFFF
        scanner.scan(work, workFoundCallback);
    }

}

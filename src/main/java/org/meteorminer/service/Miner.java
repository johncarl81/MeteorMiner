package org.meteorminer.service;

import org.meteorminer.domain.Work;
import org.meteorminer.hash.HashScanner;
import org.meteorminer.output.CLInterface;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class Miner {

    @Inject
    private HashScanner scanner;
    @Inject
    private WorkFoundCallback workFoundCallback;
    @Inject
    private CLInterface output;

    public void mine(Work work) {
        if (work != null) {
            output.verbose("Starting mine: " + work.getDataString());
            // Nonce is a number which starts at 0 and increments until 0xFFFFFFFF
            scanner.scan(work, workFoundCallback);
        }
    }

}

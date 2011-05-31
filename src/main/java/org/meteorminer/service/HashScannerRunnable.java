package org.meteorminer.service;

import org.meteorminer.domain.Work;
import org.meteorminer.hash.HashScanner;

import java.util.concurrent.CyclicBarrier;

/**
 * @author John Ericksen
 */
public class HashScannerRunnable implements Runnable {

    private HashScanner hashScanner;
    private Work work;
    private CyclicBarrier barrier;

    public HashScannerRunnable(HashScanner hashScanner, Work work) {
        this.hashScanner = hashScanner;
        this.work = work;
        this.barrier = barrier;
    }


    @Override
    public void run() {
        hashScanner.scan(work);
    }
}

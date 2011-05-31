package org.meteorminer.service;

import org.meteorminer.domain.Work;
import org.meteorminer.hash.HashScanner;

import java.util.concurrent.Callable;

/**
 * @author John Ericksen
 */
public class HashScannerCallable implements Callable<Void> {

    private HashScanner hashScanner;
    private Work work;

    public HashScannerCallable(HashScanner hashScanner) {
        this.hashScanner = hashScanner;
    }


    @Override
    public Void call() throws Exception {
        hashScanner.scan(work);
        return null;
    }

    public void setWork(Work work) {
        this.work = work;
    }
}

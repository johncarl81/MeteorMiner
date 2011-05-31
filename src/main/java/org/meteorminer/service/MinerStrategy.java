package org.meteorminer.service;

import org.meteorminer.hash.HashScanner;

/**
 * @author John Ericksen
 */
public interface MinerStrategy {

    void start();

    void add(HashScanner hashScanner);

}

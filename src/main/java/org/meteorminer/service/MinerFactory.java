package org.meteorminer.service;

import org.meteorminer.hash.HashScanner;

import java.util.Set;

/**
 * @author John Ericksen
 */
public interface MinerFactory {

    Miner createMiner(Set<HashScanner> scanners);
}

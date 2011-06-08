package org.meteorminer.service;

import org.meteorminer.hash.HashScanner;

import java.util.Collections;

/**
 * Mining Strategy to execute all work request one-to-one against a given hash scanner.  This produces
 * a scenario where each hash scanner is in essence a miner on its own.
 *
 * @author John Ericksen
 */
public class ParallelMinerStrategy extends AbstractMinerStrategy {

    @Override
    public void start() {
        for (HashScanner hashScanner : getScanners()) {
            //create a new miner for each hashScanner
            Miner miner = getMinerFactory().createMiner(Collections.singleton(hashScanner));
            //kickoff runnable
            getAsynchronousFactory().startRunnable(miner);
        }
    }

}

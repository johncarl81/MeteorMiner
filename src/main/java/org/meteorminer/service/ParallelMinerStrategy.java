package org.meteorminer.service;

import org.meteorminer.hash.HashScanner;

import java.util.Collections;

/**
 * @author John Ericksen
 */
public class ParallelMinerStrategy extends AbstractMinerStrategy {

    @Override
    public void start() {
        for (HashScanner hashScanner : getScanners()) {
            //create a new miner for each hashScanner
            Miner miner = getMinerFactory().createMiner(Collections.singleton(hashScanner));
            //kickoff runnable
            getAsynchronousFactory().startRunnableWithGracefulShutdown(miner);
        }
    }

}

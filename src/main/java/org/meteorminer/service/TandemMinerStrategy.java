package org.meteorminer.service;

/**
 * @author John Ericksen
 */
public class TandemMinerStrategy extends AbstractMinerStrategy {

    @Override
    public void start() {
        //create a one miner for all hashScanner
        Miner miner = getMinerFactory().createMiner(getScanners());
        //kickoff runnable
        getAsynchronousFactory().startRunnable(miner);
    }
}

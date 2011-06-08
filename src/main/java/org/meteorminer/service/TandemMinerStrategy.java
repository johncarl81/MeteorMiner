package org.meteorminer.service;

/**
 * Mining Strategy to execute against a work request in tandem.  That is, to divide chunks of the incoming
 * work to all of the available scanners.
 *
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

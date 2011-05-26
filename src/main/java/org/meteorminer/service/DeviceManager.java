package org.meteorminer.service;

import com.google.inject.Inject;
import org.meteorminer.hash.MinerController;

/**
 * @author John Ericksen
 */
public class DeviceManager implements Runnable {

    @Inject
    private WorkProducerMultiplex workProducer;
    @Inject
    private Miner miner;
    @Inject
    private MinerController minerController;

    private boolean mining;

    public DeviceManager() {
        mining = true;
    }

    @Override
    public void run() {
        do {
            minerController.reset();
            miner.mine(workProducer.produce());
        } while (mining);
    }

    public boolean isMining() {
        return mining;
    }

    public void setMining(boolean mining) {
        this.mining = mining;
    }
}

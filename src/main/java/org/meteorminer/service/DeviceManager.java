package org.meteorminer.service;

import com.google.inject.Inject;
import org.meteorminer.hash.MinerController;

/**
 * @author John Ericksen
 */
public class DeviceManager implements Runnable {

    @Inject
    private WorkProducerFactory workProducerFactory;
    @Inject
    private WorkConsumerFactory workConsumerFactory;
    @Inject
    private MinerController minerController;

    private boolean mining;

    public DeviceManager() {
        mining = true;
    }

    @Override
    public void run() {
        while (mining) {
            minerController.reset();
            workConsumerFactory.createWorkConsumer().mine(
                    workProducerFactory.createWorkProducer().produce());
        }
    }

    public boolean isMining() {
        return mining;
    }

    public void setMining(boolean mining) {
        this.mining = mining;
    }
}

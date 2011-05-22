package org.meteorminer.service;

import org.meteorminer.domain.Work;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class WorkConsumer {

    private Miner miner;

    @Inject
    public WorkConsumer(Miner miner) {
        this.miner = miner;
    }

    public void consume(Work work) {
        miner.mine(work);
    }
}

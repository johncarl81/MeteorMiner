package org.meteorminer.service;

import org.meteorminer.domain.Work;
import org.meteorminer.output.CLInterface;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class WorkConsumer {

    @Inject
    private Miner miner;
    @Inject
    private CLInterface output;

    public void consume(Work work) {
        output.verbose("Starting mine: " + work.getDataString());
        miner.mine(work);
    }
}

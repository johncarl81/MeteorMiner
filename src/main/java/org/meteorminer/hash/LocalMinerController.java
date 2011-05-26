package org.meteorminer.hash;

import org.meteorminer.output.CLInterface;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class LocalMinerController {

    private boolean stop = false;
    private CLInterface output;
    private MinerController minerController;

    @Inject
    public LocalMinerController(MinerController minerController, CLInterface output) {
        this.output = output;
        this.minerController = minerController;
    }

    public void stopProduction() {
        output.verbose("Stopping current work.");
        this.stop = true;
    }

    public boolean haltProduction() {
        return stop || minerController.haultProduction();
    }

    public void reset() {
        stop = false;
    }
}

package org.meteorminer.hash;

import org.meteorminer.logging.CLInterface;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class LocalMinerController {

    private boolean stop = false;
    private CLInterface output;

    @Inject
    public LocalMinerController(MinerController minerController, CLInterface output) {
        this.output = output;
        minerController.register(this);
    }

    public void stopProduction() {
        output.verbose("Stopping current work.");
        this.stop = true;
    }

    public boolean haltProduction() {
        return stop;
    }
}

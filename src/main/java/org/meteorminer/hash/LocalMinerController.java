package org.meteorminer.hash;

import org.meteorminer.logging.CLLogger;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class LocalMinerController {

    private boolean stop = false;
    private CLLogger logger;

    @Inject
    public LocalMinerController(MinerController minerController, CLLogger logger) {
        this.logger = logger;
        minerController.register(this);
    }

    public void stopProduction() {
        logger.verbose("stale work detected, stopping.");
        this.stop = true;
    }

    public boolean haltProduction() {
        return stop;
    }
}

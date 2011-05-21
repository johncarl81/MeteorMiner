package org.meteorminer.hash;

import org.meteorminer.logging.CLLogger;

/**
 * @author John Ericksen
 */
public class LocalMinerController {

    private boolean interupt = false;
    private CLLogger logger;

    public LocalMinerController(MinerController minerController, CLLogger logger) {
        this.logger = logger;
        minerController.register(this);
    }

    public void interuptProduction() {
        logger.notification("interupt");
        this.interupt = true;
    }

    public boolean haltProduction() {
        return interupt;
    }
}

package org.meteorminer.hash;

import javax.inject.Singleton;

/**
 * @author John Ericksen
 */
@Singleton
public class MinerController {

    private boolean stop;

    public void interruptProduction() {
        stop = true;
    }

    public boolean haultProduction() {
        return stop;
    }

    public void reset() {
        stop = false;
    }
}

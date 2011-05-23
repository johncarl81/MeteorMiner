package org.meteorminer.hash;

/**
 * @author John Ericksen
 */
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

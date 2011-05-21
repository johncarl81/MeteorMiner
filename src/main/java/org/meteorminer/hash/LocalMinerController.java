package org.meteorminer.hash;

/**
 * @author John Ericksen
 */
public class LocalMinerController {

    boolean interupt = false;

    public LocalMinerController(MinerController minerController) {
        minerController.register(this);
    }

    public void interuptProduction() {
        System.out.println("\rinterupt");
        this.interupt = true;
    }

    public boolean haltProduction() {
        return interupt;
    }
}

package org.meteorminer.hash;

import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Set;

/**
 * @author John Ericksen
 */
@Singleton
public class MinerController {

    private boolean haltProduction;
    private Set<LocalMinerController> listeners = new HashSet<LocalMinerController>();

    public boolean haltProduction() {
        for (LocalMinerController controller : listeners) {
            controller.interuptProduction();
        }
        return haltProduction;
    }

    public void interruptProduction(Runnable callback) {
        haltProduction = true;
        callback.run();
        haltProduction = false;
    }

    public void register(LocalMinerController localMinerController) {
        listeners.add(localMinerController);
    }

    public void unregister(LocalMinerController localMinerController) {
        listeners.remove(localMinerController);
    }
}

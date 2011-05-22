package org.meteorminer.hash;

import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Set;

/**
 * @author John Ericksen
 */
@Singleton
public class MinerController {

    private Set<LocalMinerController> listeners = new HashSet<LocalMinerController>();

    public synchronized void interruptProduction() {
        for (LocalMinerController controller : listeners) {
            controller.stopProduction();
        }
    }

    public void register(LocalMinerController localMinerController) {
        listeners.add(localMinerController);
    }

    public void unregister(LocalMinerController localMinerController) {
        listeners.remove(localMinerController);
    }
}

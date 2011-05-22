package org.meteorminer.hash;

import javax.inject.Singleton;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author John Ericksen
 */
@Singleton
public class MinerController {

    private Set<WeakReference<LocalMinerController>> listeners = new HashSet<WeakReference<LocalMinerController>>();

    public synchronized void interruptProduction() {
        Iterator<WeakReference<LocalMinerController>> referenceIterator = listeners.iterator();

        while (referenceIterator.hasNext()) {
            LocalMinerController controller = referenceIterator.next().get();
            if (controller == null) {
                referenceIterator.remove();
            } else {
                controller.stopProduction();
            }
        }
    }

    public void register(LocalMinerController localMinerController) {
        listeners.add(new WeakReference<LocalMinerController>(localMinerController));
    }
}

package org.meteorminer.queue;

import org.meteorminer.Work;

/**
 * @author John Ericksen
 */
public interface WorkFoundCallback {

    void found(Work work);
}

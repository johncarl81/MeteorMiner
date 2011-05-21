package org.meteorminer.queue;

import com.google.inject.ImplementedBy;
import org.meteorminer.Work;

/**
 * @author John Ericksen
 */
@ImplementedBy(WorkFoundCallbackImpl.class)
public interface WorkFoundCallback {

    void found(Work work, int nonce);
}

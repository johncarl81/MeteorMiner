package org.meteorminer.service;

import com.google.inject.ImplementedBy;
import org.meteorminer.domain.Work;

/**
 * @author John Ericksen
 */
@ImplementedBy(WorkFoundCallbackImpl.class)
public interface WorkFoundCallback {

    void found(Work work, int nonce);
}

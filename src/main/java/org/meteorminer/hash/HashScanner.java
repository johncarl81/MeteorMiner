package org.meteorminer.hash;

import org.meteorminer.domain.Work;
import org.meteorminer.service.WorkFoundCallback;

/**
 * @author John Ericksen
 */
public interface HashScanner {

    void scan(Work work, WorkFoundCallback workFoundCallback);

    long getNonceCount();
}

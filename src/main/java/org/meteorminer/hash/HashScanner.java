package org.meteorminer.hash;

import com.google.inject.ImplementedBy;
import org.meteorminer.domain.Work;
import org.meteorminer.service.WorkFoundCallback;

/**
 * @author John Ericksen
 */
@ImplementedBy(HashCacheScanner.class)
public interface HashScanner {

    void scan(Work work, WorkFoundCallback workFoundCallback);

    long getNonceCount();
}

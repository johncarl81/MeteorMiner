package org.meteorminer.hash;

import com.google.inject.ImplementedBy;
import org.meteorminer.domain.Work;
import org.meteorminer.hash.gpu.GpuHashScanner;
import org.meteorminer.service.WorkFoundCallback;

/**
 * @author John Ericksen
 */
@ImplementedBy(GpuHashScanner.class)
public interface HashScanner {

    void scan(Work work, WorkFoundCallback workFoundCallback);

    long getNonceCount();
}

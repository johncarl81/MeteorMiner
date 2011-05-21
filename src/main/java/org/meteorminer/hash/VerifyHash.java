package org.meteorminer.hash;

import com.google.inject.ImplementedBy;
import org.meteorminer.domain.Work;
import org.meteorminer.queue.WorkFoundCallback;

/**
 * @author John Ericksen
 */
@ImplementedBy(AsynchrounousVerifyHash.class)
public interface VerifyHash {

    void verify(Work work, int nonce, WorkFoundCallback callback);

}

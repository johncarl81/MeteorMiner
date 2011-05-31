package org.meteorminer.hash;

import com.google.inject.ImplementedBy;
import org.meteorminer.domain.Work;
import org.meteorminer.hash.scanHash.DigestProcessHashImpl;

/**
 * @author John Ericksen
 */
@ImplementedBy(DigestProcessHashImpl.class)
public interface VerifyHash {

    void verify(Work work, int nonce);

}

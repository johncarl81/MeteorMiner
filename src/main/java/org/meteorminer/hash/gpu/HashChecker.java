package org.meteorminer.hash.gpu;

import com.google.inject.ImplementedBy;
import org.meteorminer.domain.Work;

/**
 * Interface used to check the given output contains a valid hash, if so, use teh workFoundCallback to submit the work
 *
 * @author John Ericksen
 */
@ImplementedBy(AsynchronousHashChecker.class)
public interface HashChecker {

    void check(MinerResult output, Work work);

}

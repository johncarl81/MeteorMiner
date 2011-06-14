package org.meteorminer.hash.gpu;

import com.google.inject.ImplementedBy;
import org.meteorminer.domain.Work;

/**
 * Assisted injection factory to create a RunnableHashChecker
 *
 * @author John Ericksen
 */
@ImplementedBy(PooledRunnableHashCheckerFactory.class)
public interface RunnableHashCheckerFactory {

    RunnableHashChecker createHashChecker(MinerResult output, Work work);

}

package org.meteorminer.hash.gpu;

import org.meteorminer.domain.Work;

/**
 * Assisted injection factory to create a RunnableHashChecker
 *
 * @author John Ericksen
 */
public interface RunnableHashCheckerFactory {

    RunnableHashChecker createHashChecker(MinerResult output, Work work);

}

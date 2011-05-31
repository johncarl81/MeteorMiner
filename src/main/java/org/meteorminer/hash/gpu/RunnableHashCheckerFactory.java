package org.meteorminer.hash.gpu;

import org.meteorminer.domain.Work;

/**
 * @author John Ericksen
 */
public interface RunnableHashCheckerFactory {

    RunnableHashChecker createHashChecker(MinerResult output, Work work);

}

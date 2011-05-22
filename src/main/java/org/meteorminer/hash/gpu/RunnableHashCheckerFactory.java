package org.meteorminer.hash.gpu;

import org.meteorminer.domain.Work;
import org.meteorminer.service.WorkFoundCallback;

/**
 * @author John Ericksen
 */
public interface RunnableHashCheckerFactory {

    RunnableHashChecker createHashChecker(MinerResult output, Work work, WorkFoundCallback workFoundCallback);

}

package org.meteorminer.hash.gpu;

import com.google.inject.Inject;
import org.apache.commons.pool.ObjectPool;
import org.meteorminer.config.binding.RunnableHashCheckerPool;
import org.meteorminer.domain.Work;
import org.meteorminer.output.CLInterface;

/**
 * @author John Ericksen
 */
public class PooledRunnableHashCheckerFactory implements RunnableHashCheckerFactory {

    @Inject
    @RunnableHashCheckerPool
    private ObjectPool runnablePool;
    @Inject
    private CLInterface output;

    @Override
    public RunnableHashChecker createHashChecker(MinerResult result, Work work) {
        try {
            RunnableHashChecker hashChecker = (RunnableHashChecker) runnablePool.borrowObject();
            hashChecker.setup(result, work, runnablePool);
            return hashChecker;
        } catch (Exception e) {
            output.error(e);
            return null;
        }
    }
}

package org.meteorminer.hash.gpu;

import org.apache.commons.pool.ObjectPool;
import org.meteorminer.config.binding.Synchronous;
import org.meteorminer.domain.Work;

import javax.inject.Inject;

/**
 * Runnable implmeentation calling the HashChecker.check() method with the available arguments
 *
 * @author John Ericksen
 */
public class RunnableHashChecker implements Runnable {

    private MinerResult output;
    private Work work;
    private HashChecker delegate;
    private ObjectPool runnablePool;

    @Inject
    public RunnableHashChecker(@Synchronous HashChecker delegate) {
        this.delegate = delegate;
    }

    public void setup(MinerResult output, Work work, ObjectPool runnablePool) {
        this.output = output;
        this.work = work;
        this.runnablePool = runnablePool;
    }

    @Override
    public void run() {
        delegate.check(output, work);
        try {
            runnablePool.returnObject(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        setup(null, null, null);
    }
}

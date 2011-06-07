package org.meteorminer.hash.gpu;

import org.meteorminer.domain.Work;
import org.meteorminer.service.AsynchronousFactory;

import javax.inject.Inject;

/**
 * Asynchronous wrapper for the HashChecker interface
 *
 * @author John Ericksen
 */
public class AsynchronousHashChecker implements HashChecker {

    @Inject
    private RunnableHashCheckerFactory runnableHashCheckerFactory;
    @Inject
    private AsynchronousFactory asynchronousFactory;

    @Override
    public void check(MinerResult output, Work work) {
        if (output.getEvent() != null) {
            asynchronousFactory.startRunnable(runnableHashCheckerFactory.createHashChecker(output, work));
        }
    }

    public void setRunnableHashCheckerFactory(RunnableHashCheckerFactory runnableHashCheckerFactory) {
        this.runnableHashCheckerFactory = runnableHashCheckerFactory;
    }

    public void setAsynchronousFactory(AsynchronousFactory asynchronousFactory) {
        this.asynchronousFactory = asynchronousFactory;
    }
}

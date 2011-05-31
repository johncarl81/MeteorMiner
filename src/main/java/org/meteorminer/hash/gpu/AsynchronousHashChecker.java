package org.meteorminer.hash.gpu;

import org.meteorminer.domain.Work;

import javax.inject.Inject;

/**
 * Asynchronous wrapper for the HashChecker interface
 *
 * @author John Ericksen
 */
public class AsynchronousHashChecker implements HashChecker {

    @Inject
    private RunnableHashCheckerFactory runnableHashCheckerFactory;

    @Override
    public void check(MinerResult output, Work work) {
        output.getEvent().invokeUponCompletion(runnableHashCheckerFactory.createHashChecker(output, work));
    }

    public void setRunnableHashCheckerFactory(RunnableHashCheckerFactory runnableHashCheckerFactory) {
        this.runnableHashCheckerFactory = runnableHashCheckerFactory;
    }
}

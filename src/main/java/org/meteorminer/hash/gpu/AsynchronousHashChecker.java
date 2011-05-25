package org.meteorminer.hash.gpu;

import com.google.inject.Inject;
import org.meteorminer.domain.Work;
import org.meteorminer.service.WorkFoundCallback;

/**
 * Asynchronous wrapper for the HashChecker interface
 *
 * @author John Ericksen
 */
public class AsynchronousHashChecker implements HashChecker {

    @Inject
    private RunnableHashCheckerFactory runnableHashCheckerFactory;

    @Override
    public void check(MinerResult output, Work work, WorkFoundCallback workFoundCallback) {
        output.getEvent().invokeUponCompletion(runnableHashCheckerFactory.createHashChecker(output, work, workFoundCallback));
    }

    public void setRunnableHashCheckerFactory(RunnableHashCheckerFactory runnableHashCheckerFactory) {
        this.runnableHashCheckerFactory = runnableHashCheckerFactory;
    }
}

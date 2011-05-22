package org.meteorminer.hash.gpu;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.meteorminer.binding.AsyncPreferred;
import org.meteorminer.domain.Work;
import org.meteorminer.service.WorkFoundCallback;

/**
 * Runnable implmeentation calling the HashChecker.check() method with the available arguments
 *
 * @author John Ericksen
 */
public class RunnableHashChecker implements Runnable {

    private MinerResult output;
    private Work work;
    private WorkFoundCallback workFoundCallback;
    private HashChecker delegate;

    @Inject
    public RunnableHashChecker(@Assisted MinerResult output,
                               @Assisted Work work,
                               @Assisted WorkFoundCallback workFoundCallback,
                               @AsyncPreferred HashChecker delegate) {
        this.output = output;
        this.work = work;
        this.workFoundCallback = workFoundCallback;
        this.delegate = delegate;
    }

    @Override
    public void run() {
        delegate.check(output, work, workFoundCallback);
    }
}

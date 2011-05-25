package org.meteorminer.service;

import org.meteorminer.domain.Work;
import org.meteorminer.network.JsonCommandFactory;
import org.meteorminer.network.WorkSubmit;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class WorkFoundCallbackImpl implements WorkFoundCallback {

    @Inject
    private AsynchronousFactory asyncFactory;
    @Inject
    private JsonCommandFactory commandFactory;

    public void found(Work work, int nonce) {
        work.getData()[19] = nonce;
        WorkSubmit workSubmit = commandFactory.buildWorkSubmit(work, nonce);
        asyncFactory.startRunnable(workSubmit);
    }

    public void setAsyncFactory(AsynchronousFactory asyncFactory) {
        this.asyncFactory = asyncFactory;
    }

    public void setCommandFactory(JsonCommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }
}

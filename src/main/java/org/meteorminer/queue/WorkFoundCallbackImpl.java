package org.meteorminer.queue;

import org.meteorminer.JsonClient;
import org.meteorminer.Work;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class WorkFoundCallbackImpl implements WorkFoundCallback {

    @Inject
    JsonClient jsonClient;

    public void found(Work work) {
        new Thread(new WorkSubmit(work, jsonClient)).start();
    }
}

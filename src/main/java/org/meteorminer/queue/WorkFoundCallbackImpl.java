package org.meteorminer.queue;

import org.meteorminer.JsonClient;
import org.meteorminer.Work;
import org.meteorminer.stats.Statistics;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class WorkFoundCallbackImpl implements WorkFoundCallback {

    @Inject
    JsonClient jsonClient;
    @Inject
    Statistics stats;

    public void found(Work work, int nonce) {
        work.getData()[19] = nonce;
        new Thread(new WorkSubmit(work, jsonClient, stats)).start();
    }
}

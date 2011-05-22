package org.meteorminer.service;

import org.meteorminer.domain.Work;
import org.meteorminer.hash.HashCacheScanner;
import org.meteorminer.network.JsonClient;
import org.meteorminer.output.CLInterface;
import org.meteorminer.output.Statistics;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class WorkFoundCallbackImpl implements WorkFoundCallback {

    @Inject
    private JsonClient jsonClient;
    @Inject
    private Statistics stats;
    @Inject
    private HashCacheScanner hashCache;
    @Inject
    private CLInterface output;

    public void found(Work work, int nonce) {
        work.getData()[19] = nonce;
        new Thread(new WorkSubmit(work, jsonClient, stats, hashCache, nonce, output)).start();
    }
}

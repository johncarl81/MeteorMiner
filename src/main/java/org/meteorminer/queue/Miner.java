package org.meteorminer.queue;

import org.codehaus.jackson.map.ObjectMapper;
import org.meteorminer.JsonClient;
import org.meteorminer.Work;
import org.meteorminer.hash.ScanHash;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class Miner {

    @Inject
    ScanHash sh;
    @Inject
    JsonClient jsonClient;
    @Inject
    ObjectMapper mapper;
    @Inject
    WorkFoundCallbackImpl workFound;

    public void mine(Work work) {

        // Nonce is a number which starts at 0 and increments until 0xFFFFFFFF
        sh.scan(work, 0, 0xFFFFFFFF, workFound);
    }

}

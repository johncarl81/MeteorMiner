package org.meteorminer.hash.scanHash;

import org.meteorminer.domain.Work;
import org.meteorminer.logging.CLLogger;
import org.meteorminer.logging.Statistics;
import org.meteorminer.queue.WorkFoundCallback;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ProcessHash {

    @Inject
    private Statistics stats;
    @Inject
    private CLLogger logger;

    public boolean processHash(Work work, int[] data, int nonce, int[] midstate, int[] state, int[] buff, int[] hash, WorkFoundCallback workFoundCallback) {
        data[3] = nonce; // NONCE is _data[3]

        System.arraycopy(midstate, 0, state, 0, midstate.length);
        SHA256.processBlock(state, buff, data);
        hash = SHA256.initState();
        SHA256.processBlock(hash, buff, state);

        if (hash[7] == 0 && hash[6] < work.getTarget()[6]) {
            logger.notification("found one");
            //work.setData(work.getDataString().substring(0, 128) + encode(data));
            workFoundCallback.found(work, nonce);

            return true;
        }
        return false;
    }

    public void setStats(Statistics stats) {
        this.stats = stats;
    }
}

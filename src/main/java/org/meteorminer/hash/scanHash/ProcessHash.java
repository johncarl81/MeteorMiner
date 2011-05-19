package org.meteorminer.hash.scanHash;

import org.meteorminer.Work;
import org.meteorminer.queue.WorkFoundCallback;

import static org.meteorminer.hash.scanHash.HexUtil.encode;

/**
 * @author John Ericksen
 */
public class ProcessHash {

    public static boolean processHash(Work work, int[] data, int nonce, int[] midstate, int[] state, int[] buff, int[] hash, WorkFoundCallback workFoundCallback){
        data[3] = nonce; // NONCE is _data[3]

        System.arraycopy(midstate, 0, state, 0, midstate.length);
        SHA256.processBlock(state, buff, data);
        hash = SHA256.initState();
        SHA256.processBlock(hash, buff, state);

        if (hash[7] == 0 && hash[6] < work.getTarget()[6]) {
            System.out.println("found one");
            work.setData(work.getDataString().substring(0, 128) + encode(data));
            workFoundCallback.found(work);
            return true;
        }
        return false;
    }
}

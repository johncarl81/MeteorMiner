package org.meteorminer.hash.scanHash;

import org.meteorminer.domain.Work;
import org.meteorminer.hash.VerifyHash;
import org.meteorminer.output.CLInterface;
import org.meteorminer.service.WorkFoundCallback;

import javax.inject.Inject;

import static org.meteorminer.hash.HexUtil.decode;

/**
 * @author John Ericksen
 */
public class ProcessHashImpl implements VerifyHash {

    @Inject
    private CLInterface output;

    @Override
    public void verify(Work work, int nonce, WorkFoundCallback callback) {

        int[] data = decode(new int[16], work.getDataString().substring(128));
        int[] midstate = decode(decode(new int[16], work.getHash1()), work.getMidstateString());
        int[] state = new int[midstate.length];
        int[] buff = new int[64];
        int[] hash = SHA256.initState();

        data[3] = nonce;

        System.arraycopy(midstate, 0, state, 0, midstate.length);
        SHA256.processBlock(state, buff, data);
        SHA256.processBlock(hash, buff, state);

        if (hash[7] == 0 && hash[6] < work.getTarget()[6]) {
            output.verbose("Found Hash, proceeding to local verification");
            //work.setData(work.getDataString().substring(0, 128) + encode(data));
            callback.found(work, nonce);
        }
    }
}

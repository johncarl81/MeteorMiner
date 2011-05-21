package org.meteorminer.hash.scanHash;

import org.meteorminer.domain.Work;
import org.meteorminer.hash.AbstractHashScanner;
import org.meteorminer.hash.VerifyHash;
import org.meteorminer.queue.WorkFoundCallback;

import javax.inject.Inject;
import java.util.Random;

import static org.meteorminer.hash.scanHash.HexUtil.decode;


//4877554
public class ScanHash extends AbstractHashScanner {

    @Inject
    private VerifyHash verifyHash;

    private long nonceCount;

    public void innerScan(Work work, WorkFoundCallback workFoundCallback) {
        Random rand = new Random(System.currentTimeMillis());
        int start = rand.nextInt(Integer.MAX_VALUE);

        innerScan(work, workFoundCallback, start, start - 1);
    }

    @Override
    public long getNonceCount() {
        return nonceCount;
    }

    public void innerScan(Work work, WorkFoundCallback workFoundCallback, int start, int end) {
        nonceCount = 0;

        int[] data = decode(new int[16], work.getDataString().substring(128));
        int[] midstate = decode(decode(new int[16], work.getHash1()), work.getMidstateString());
        int[] state = new int[midstate.length];
        int[] buff = new int[64];


        int[] hash;
        for (int nonce = start; nonce != end && !getLocalController().haltProduction(); nonce++, nonceCount++) {

            data[3] = nonce;

            System.arraycopy(midstate, 0, state, 0, midstate.length);
            SHA256.processBlock(state, buff, data);
            hash = SHA256.initState();
            SHA256.processBlock(hash, buff, state);

            if (hash[7] == 0 && hash[6] < work.getTarget()[6]) {
                verifyHash.verify(work, nonce, workFoundCallback);
                break;
            }
        }
    }
}

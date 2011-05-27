package org.meteorminer.hash.scanHash;

import org.meteorminer.domain.Work;
import org.meteorminer.hash.AbstractHashScanner;
import org.meteorminer.hash.VerifyHash;
import org.meteorminer.output.Statistics;
import org.meteorminer.service.WorkFoundCallback;

import javax.inject.Inject;

import static org.meteorminer.hash.HexUtil.decode;


//4877554
public class ScanHash extends AbstractHashScanner {

    @Inject
    private VerifyHash verifyHash;
    @Inject
    private Statistics statistics;

    private long nonceCount;

    public void innerScan(Work work, WorkFoundCallback workFoundCallback) {
        innerScan(work, workFoundCallback, 0, -1);
    }

    @Override
    public long getNonceCount() {
        return nonceCount;
    }

    public void innerScan(Work work, WorkFoundCallback workFoundCallback, int start, int end) {
        nonceCount = 0;

        long startTime = System.currentTimeMillis();

        int[] data = decode(new int[16], work.getDataString().substring(128));
        int[] midstate = decode(decode(new int[16], work.getHash1()), work.getMidstateString());
        int[] state = new int[midstate.length];
        int[] buff = new int[64];


        int[] hash;
        for (int nonce = start; nonce != end && !getController().haultProduction(); nonce++, nonceCount++) {
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

        statistics.addWorkTime(System.currentTimeMillis() - startTime);
    }
}

package org.meteorminer.hash.scanHash;

import org.meteorminer.domain.Work;
import org.meteorminer.hash.AbstractHashScanner;
import org.meteorminer.hash.NonceIteratorFactory;
import org.meteorminer.hash.VerifyHash;
import org.meteorminer.hash.WorkConsumer;
import org.meteorminer.output.Statistics;

import javax.inject.Inject;
import java.util.Iterator;

import static org.meteorminer.hash.HexUtil.decode;


//4877554
public class ScanHash extends AbstractHashScanner {

    private static final int NONCE_BUFFER = 1000;

    @Inject
    private VerifyHash verifyHash;
    @Inject
    private Statistics statistics;
    @Inject
    private NonceIteratorFactory nonceIteratorFactory;
    @Inject
    private WorkConsumer workSource;

    private long nonceCount;

    public void innerScan() {
        nonceCount = 0;

        Work work = workSource.getWork();
        Work prevWork = work;

        Iterator<Integer> nonceIterator = nonceIteratorFactory.createNonceIterator(NONCE_BUFFER);

        int[] data = decode(new int[16], work.getDataString().substring(128));
        int[] midstate = decode(decode(new int[16], work.getHash1()), work.getMidstateString());
        int[] state = new int[midstate.length];
        int[] buff = new int[64];

        int[] hash;
        while (nonceIterator.hasNext() && !isStop()) {
            work = workSource.getWork();
            if (prevWork != work) {
                //update
                prevWork = work;
                data = decode(new int[16], work.getDataString().substring(128));
                midstate = decode(decode(new int[16], work.getHash1()), work.getMidstateString());
                state = new int[midstate.length];
                buff = new int[64];
            }
            int nonce = nonceIterator.next();
            int nonceEnd = nonce + NONCE_BUFFER;
            long nonceTime = System.currentTimeMillis();
            for (; nonce < nonceEnd; nonce++) {

                data[3] = nonce;

                System.arraycopy(midstate, 0, state, 0, midstate.length);
                SHA256.processBlock(state, buff, data);
                hash = SHA256.initState();
                SHA256.processBlock(hash, buff, state);

                if (hash[7] == 0 && hash[6] < work.getTarget()[6]) {
                    verifyHash.verify(work, nonce);
                    break;
                }

            }
            statistics.addWorkTime(System.currentTimeMillis() - nonceTime);
            nonceCount += NONCE_BUFFER;
        }


    }

    @Override
    public long getNonceCount() {
        return nonceCount;
    }
}

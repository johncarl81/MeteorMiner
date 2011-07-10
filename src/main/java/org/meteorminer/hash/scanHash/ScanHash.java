package org.meteorminer.hash.scanHash;

import org.meteorminer.domain.Work;
import org.meteorminer.hash.AbstractHashScanner;
import org.meteorminer.hash.NonceIteratorFactory;
import org.meteorminer.hash.VerifyHash;
import org.meteorminer.hash.WorkConsumer;

import javax.inject.Inject;
import java.util.Iterator;


public class ScanHash extends AbstractHashScanner {

    private static final int NONCE_BUFFER = 1000;

    @Inject
    private VerifyHash verifyHash;
    @Inject
    private NonceIteratorFactory nonceIteratorFactory;
    @Inject
    private WorkConsumer workSource;

    private long nonceCount;

    public void innerScan() {
        nonceCount = 0;

        Work work;
        Work prevWork = null;

        Iterator<Integer> nonceIterator = nonceIteratorFactory.createNonceIterator(NONCE_BUFFER);

        int[] data = null;
        int[] midstate = null;
        int[] state = null;
        int[] buff = null;

        int[] hash;
        while (nonceIterator.hasNext() && !isStop()) {
            work = workSource.getWork();
            if (prevWork != work) {
                //update
                ScanHashPreProcessWork preProcessWork = (ScanHashPreProcessWork) work.getPreProcessedWork().get(ScanHashPreProcessWorkFactory.PRE_PROCESS_NAME);
                prevWork = work;
                data = preProcessWork.getData();
                midstate = preProcessWork.getMidstate();
                state = new int[midstate.length];
                buff = new int[64];
            }
            int nonce = nonceIterator.next();
            int nonceEnd = nonce + NONCE_BUFFER;
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
            nonceCount += NONCE_BUFFER;
        }


    }

    @Override
    public long getNonceCount() {
        return nonceCount;
    }
}

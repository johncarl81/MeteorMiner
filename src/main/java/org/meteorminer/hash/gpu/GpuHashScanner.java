package org.meteorminer.hash.gpu;

import org.meteorminer.domain.Work;
import org.meteorminer.hash.AbstractHashScanner;
import org.meteorminer.queue.WorkFoundCallback;

import javax.inject.Inject;
import java.nio.IntBuffer;

/**
 * @author John Ericksen
 */
public class GpuHashScanner extends AbstractHashScanner {

    @Inject
    private DiabloMinerFactory diabloMinerFactory;
    @Inject
    private HashChecker hashChecker;

    private long nonceCount;
    static final int workgroupSize;
    static final int localWorkSize;

    static {
        workgroupSize = 200000;
        localWorkSize = 500;
    }

    public void innerScan(Work work, WorkFoundCallback workFoundCallback) {
        innerScan(work, workFoundCallback, 0, 0xFFFFFFFFL);
    }


    public void innerScan(Work work, WorkFoundCallback workFoundCallback, int start, long end) {

        DiabloMiner diabloMiner = diabloMinerFactory.createDiabloMiner(work);//new DiabloMiner(ocl, work, clIntBufferPool, intBufferPool);

        int startNonce = (start / workgroupSize);
        long nonceEnd = startNonce + (end / workgroupSize) + 1;

        for (int nonce = startNonce; nonce < nonceEnd && !getLocalController().haltProduction(); nonce++, nonceCount++) {

            IntBuffer output = diabloMiner.hash(nonce, workgroupSize, localWorkSize);
            hashChecker.check(output, work, workFoundCallback);
        }
    }

    @Override
    public long getNonceCount() {
        return nonceCount * workgroupSize;
    }

}

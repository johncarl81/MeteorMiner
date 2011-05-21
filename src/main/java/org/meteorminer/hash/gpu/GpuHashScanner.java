package org.meteorminer.hash.gpu;

import org.meteorminer.binding.SearchKernel;
import org.meteorminer.domain.Work;
import org.meteorminer.hash.AbstractHashScanner;
import org.meteorminer.hash.VerifyHash;
import org.meteorminer.logging.CLLogger;
import org.meteorminer.queue.WorkFoundCallback;

import javax.inject.Inject;
import java.nio.IntBuffer;

/**
 * @author John Ericksen
 */
public class GpuHashScanner extends AbstractHashScanner {

    @Inject
    @SearchKernel
    private OCL ocl;
    @Inject
    private VerifyHash verifyHash;
    @Inject
    private CLLogger logger;

    private long nonceCount;
    static final int workgroupSize;
    static final int localWorkSize;

    static {
        workgroupSize = 100000;
        localWorkSize = 500;
    }

    public void innerScan(Work work, WorkFoundCallback workFoundCallback) {
        innerScan(work, workFoundCallback, 0, 0xFFFFFFFFL);
    }


    public void innerScan(Work work, WorkFoundCallback workFoundCallback, int start, long end) {

        DiabloMiner diabloMiner = new DiabloMiner(ocl, work, 0xF);

        int startNonce = (start / workgroupSize);
        long nonceEnd = startNonce + (end / workgroupSize) + 1;

        for (int nonce = startNonce; nonce < nonceEnd && !getLocalController().haltProduction(); nonce++, nonceCount++) {

            IntBuffer output = diabloMiner.hash(nonce, workgroupSize, localWorkSize);

            for (int i = 0; i < 0xF; i++) {

                if (output.get(i) > 0) {
                    logger.verbose("Found Hash, proceeding to local verification");
                    verifyHash.verify(work, output.get(i), workFoundCallback);
                    diabloMiner.createBuffer(ocl);
                    break;
                }
            }
        }

        diabloMiner.finish();
    }

    @Override
    public long getNonceCount() {
        return nonceCount * workgroupSize;
    }

}

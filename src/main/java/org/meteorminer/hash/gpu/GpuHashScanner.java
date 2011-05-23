package org.meteorminer.hash.gpu;

import org.meteorminer.domain.Work;
import org.meteorminer.hash.AbstractHashScanner;
import org.meteorminer.output.CLInterface;
import org.meteorminer.output.Statistics;
import org.meteorminer.service.WorkFoundCallback;

import javax.inject.Inject;

/**
 * HashScanner that utilizes the GPU through the DiabloMiner implementation
 *
 * @author John Ericksen
 */
public class GpuHashScanner extends AbstractHashScanner {

    @Inject
    private DiabloMinerFactory diabloMinerFactory;
    @Inject
    private HashChecker hashChecker;
    @Inject
    private Statistics statistics;
    @Inject
    private CLInterface output;

    private long nonceCount;
    static final int workgroupSize = 3000000;
    static final int localWorkSize = 64;

    public void innerScan(Work work, WorkFoundCallback workFoundCallback) {
        innerScan(work, workFoundCallback, 0, 0xFFFFFFFFL);
    }

    public void innerScan(Work work, WorkFoundCallback workFoundCallback, int start, long end) {

        DiabloMiner diabloMiner = diabloMinerFactory.createDiabloMiner(work);

        int startNonce = (start / workgroupSize);
        long nonceEnd = startNonce + (end / workgroupSize) + 1;

        long startTime = System.currentTimeMillis();

        for (int nonce = startNonce; nonce < nonceEnd && !getLocalController().haltProduction(); nonce++, nonceCount++) {

            MinerResult output = diabloMiner.hash(nonce, workgroupSize, localWorkSize);
            hashChecker.check(output, work, workFoundCallback);
        }

        long workTime = System.currentTimeMillis() - startTime;
        statistics.addWorkTime(workTime);
        output.verbose("Scan finished after " + workTime + "ms");
    }

    @Override
    public long getNonceCount() {
        return nonceCount * workgroupSize;
    }
}

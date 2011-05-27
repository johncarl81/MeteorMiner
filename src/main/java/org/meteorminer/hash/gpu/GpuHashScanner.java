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
    private DiabloMiner diabloMiner;

    public void innerScan(Work work, WorkFoundCallback workFoundCallback) {
        innerScan(work, workFoundCallback, 0, 0xFFFFFFFFL);
    }

    public void innerScan(Work work, WorkFoundCallback workFoundCallback, int start, long end) {

        nonceCount = 0;
        long startTime = System.currentTimeMillis();

        diabloMiner = diabloMinerFactory.createDiabloMiner(work);

        int startNonce = (start / diabloMiner.getWorkgroupSize());
        long nonceEnd = startNonce + (end / diabloMiner.getWorkgroupSize()) + 1;

        for (int nonce = startNonce; nonce < nonceEnd && !getController().haultProduction(); nonce++, nonceCount++) {
            long loopTime = System.currentTimeMillis();
            MinerResult output = diabloMiner.hash(nonce);
            hashChecker.check(output, work, workFoundCallback);
            statistics.addWorkTime(System.currentTimeMillis() - loopTime);
        }

        output.verbose("Scan finished after " + (System.currentTimeMillis() - startTime) + "ms");

    }

    @Override
    public long getNonceCount() {
        if (diabloMiner == null) {
            return 0;
        }
        return nonceCount * diabloMiner.getWorkgroupSize();
    }

    public int getWorkgroupSize() {
        if (diabloMiner == null) {
            return 0;
        }
        return diabloMiner.getWorkgroupSize();
    }
}

package org.meteorminer.hash.gpu;

import org.meteorminer.domain.Work;
import org.meteorminer.hash.AbstractHashScanner;
import org.meteorminer.hash.NonceIteratorFactory;
import org.meteorminer.output.CLInterface;
import org.meteorminer.output.Statistics;

import javax.inject.Inject;
import java.util.Iterator;

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
    @Inject
    private NonceIteratorFactory nonceIteratorFactory;

    private long nonceCount;
    private DiabloMiner diabloMiner;

    public void innerScan(Work work) {

        nonceCount = 0;
        long startTime = System.currentTimeMillis();

        diabloMiner = diabloMinerFactory.createDiabloMiner(work);
        Iterator<Integer> nonceIterator = nonceIteratorFactory.createNonceIterator(diabloMiner.getWorkgroupSize());

        while (nonceIterator.hasNext() && !isStop()) {
            long loopTime = System.currentTimeMillis();
            MinerResult output = diabloMiner.hash(nonceIterator.next());
            hashChecker.check(output, work);
            statistics.addWorkTime(System.currentTimeMillis() - loopTime);
            ++nonceCount;
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

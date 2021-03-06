package org.meteorminer.hash.gpu;

import org.meteorminer.config.binding.Vectors;
import org.meteorminer.domain.Device;
import org.meteorminer.domain.Work;
import org.meteorminer.hash.AbstractHashScanner;
import org.meteorminer.hash.NonceIteratorFactory;
import org.meteorminer.hash.WorkConsumer;
import org.meteorminer.output.CLInterface;

import javax.inject.Inject;
import java.util.Iterator;

/**
 * HashScanner that utilizes the GPU
 *
 * @author John Ericksen
 */
public class GpuHashScanner extends AbstractHashScanner {

    public static final int NONCE_BUFFER = 10;

    @Inject
    private HashChecker hashChecker;
    @Inject
    private CLInterface output;
    @Inject
    private NonceIteratorFactory nonceIteratorFactory;
    @Inject
    private MinerCore minerCore;
    @Inject
    private WorkConsumer workSource;
    @Inject
    private Device device;
    @Inject
    @Vectors
    private int vectors;

    private long nonceCount;


    public void innerScan() {

        nonceCount = 0;
        int nonceWorkSize = minerCore.getWorkgroupSize() * vectors;

        Iterator<Integer> nonceIterator = nonceIteratorFactory.createNonceIterator(NONCE_BUFFER * nonceWorkSize);

        Work work;
        //outer loop that reserves the nonce ranges.  This reduces strain on the atomic reference to the
        //nonce and current work.
        while (nonceIterator.hasNext() && !isStop()) {
            work = workSource.getWork();
            int nonce = nonceIterator.next();
            //inner loop to iterate over the buffered ranges
            int nonceEnd = nonce + NONCE_BUFFER * nonceWorkSize;
            for (; nonce < nonceEnd; nonce += nonceWorkSize) {
                MinerResult output = minerCore.hash(nonce / vectors, work);
                hashChecker.check(output, work);
            }
            nonceCount += NONCE_BUFFER;
        }

        output.notification("Shutdown down GPU Hash Scanner on " + device.getName());
    }

    @Override
    public long getNonceCount() {
        if (minerCore == null) {
            return 0;
        }
        return nonceCount * minerCore.getWorkgroupSize() * vectors;
    }
}

package org.meteorminer.hash.gpu;

import org.meteorminer.domain.Device;
import org.meteorminer.domain.Work;
import org.meteorminer.hash.AbstractHashScanner;
import org.meteorminer.hash.NonceIteratorFactory;
import org.meteorminer.hash.WorkConsumer;
import org.meteorminer.output.CLInterface;

import javax.inject.Inject;
import java.util.Iterator;

/**
 * HashScanner that utilizes the GPU through the DiabloMiner implementation
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
    private DiabloMiner diabloMiner;
    @Inject
    private WorkConsumer workSource;
    @Inject
    private Device device;

    private long nonceCount;

    public void innerScan() {

        nonceCount = 0;

        Iterator<Integer> nonceIterator = nonceIteratorFactory.createNonceIterator(diabloMiner.getWorkgroupSize() * NONCE_BUFFER);

        Work work;
        //outer loop that reserves the nonce ranges.  This reduces strain on the atomic reference to the
        //nonce and current work.
        while (nonceIterator.hasNext() && !isStop()) {
            work = workSource.getWork();
            int nonce = nonceIterator.next();
            //inner loop to iterate over the buffered ranges
            int nonceEnd = nonce + diabloMiner.getWorkgroupSize() * NONCE_BUFFER;
            for (; nonce < nonceEnd; nonce += diabloMiner.getWorkgroupSize()) {
                MinerResult output = diabloMiner.hash(nonce, work);
                hashChecker.check(output, work);
            }
            nonceCount += NONCE_BUFFER;
        }

        output.notification("Shutdown down GPU Hash Scanner on " + device.getName());
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

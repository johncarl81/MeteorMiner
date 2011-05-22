package org.meteorminer.hash.gpu;

import com.google.inject.Inject;
import org.apache.commons.pool.ObjectPool;
import org.meteorminer.binding.IntBufferPool;
import org.meteorminer.domain.Work;
import org.meteorminer.hash.VerifyHash;
import org.meteorminer.logging.CLInterface;
import org.meteorminer.queue.WorkFoundCallback;

import java.nio.IntBuffer;

/**
 * @author John Ericksen
 */
public class HashCheckerImpl implements HashChecker {

    @Inject
    @IntBufferPool
    private ObjectPool intBufferPool;
    @Inject
    private CLInterface output;
    @Inject
    private VerifyHash verifyHash;

    @Override
    public void check(IntBuffer output, Work work, WorkFoundCallback workFoundCallback) {
        for (int i = 0; i < 0xF; i++) {

            if (output.get(i) > 0) {
                this.output.verbose("Found Hash, proceeding to local verification");
                verifyHash.verify(work, output.get(i), workFoundCallback);
            }
        }
        try {
            intBufferPool.invalidateObject(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package org.meteorminer.hash.gpu;

import com.google.inject.Inject;
import org.apache.commons.pool.ObjectPool;
import org.meteorminer.binding.CLIntBufferPool;
import org.meteorminer.binding.IntBufferPool;
import org.meteorminer.domain.Work;
import org.meteorminer.hash.VerifyHash;
import org.meteorminer.output.CLInterface;
import org.meteorminer.service.WorkFoundCallback;

import java.nio.IntBuffer;

/**
 * @author John Ericksen
 */
public class HashCheckerImpl implements HashChecker {

    @Inject
    @IntBufferPool
    private ObjectPool intBufferPool;
    @Inject
    @CLIntBufferPool
    private ObjectPool clIntBufferPool;
    @Inject
    private CLInterface output;
    @Inject
    private VerifyHash verifyHash;

    @Override
    public void check(MinerResult output, Work work, WorkFoundCallback workFoundCallback) {
        IntBuffer buffer = output.getBuffer();
        try {
            clIntBufferPool.returnObject(output.getClBuffer());

            for (int i = 0; i < 0xF; i++) {

                if (buffer.get(i) > 0) {
                    this.output.verbose("Found Hash, proceeding to local verification");
                    verifyHash.verify(work, buffer.get(i), workFoundCallback);
                }
            }

            intBufferPool.returnObject(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

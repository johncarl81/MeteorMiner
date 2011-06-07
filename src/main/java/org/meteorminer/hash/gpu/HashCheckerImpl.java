package org.meteorminer.hash.gpu;

import org.apache.commons.pool.ObjectPool;
import org.meteorminer.config.binding.CLIntBufferPool;
import org.meteorminer.config.binding.IntBufferPool;
import org.meteorminer.domain.Work;
import org.meteorminer.hash.VerifyHash;
import org.meteorminer.output.CLInterface;

import javax.inject.Inject;
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
    public void check(MinerResult result, Work work) {
        result.getEvent().waitFor();
        IntBuffer buffer = result.getBuffer();
        try {
            clIntBufferPool.returnObject(result.getClBuffer());

            for (int i = 0; i < 0xFF; i++) {

                if (buffer.get(i) > 0) {
                    this.output.verbose("Found Hash, proceeding to local verification");
                    verifyHash.verify(work, buffer.get(i));
                }
            }

            intBufferPool.returnObject(buffer);
        } catch (Exception e) {
            output.error(e);
        }
    }
}

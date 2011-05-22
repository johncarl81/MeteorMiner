package org.meteorminer.hash.gpu;

import com.nativelibs4java.opencl.CLIntBuffer;
import com.nativelibs4java.opencl.CLMem;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.meteorminer.binding.SearchKernel;

import javax.inject.Inject;
import java.nio.IntBuffer;
import java.util.Arrays;

/**
 * CLIntBuffer ObjectPool Factory.  Creates, passivates and destroys buffers for easy reuse of a set of CLIntBuffers.
 * <p/>
 * For use specifically with an Apache ObjectPool
 *
 * @author John Ericksen
 */
public class CLIntBufferPoolFactory extends BasePoolableObjectFactory {

    private IntBuffer emptyBuffer;
    @Inject
    @SearchKernel
    private OCL ocl;

    public CLIntBufferPoolFactory() {
        int[] emptyArray = new int[0xF];
        Arrays.fill(emptyArray, 0);
        emptyBuffer = IntBuffer.wrap(emptyArray);
    }

    /**
     * Create a new Buffer, using the empty buffer as a template
     *
     * @return
     * @throws Exception
     */
    public Object makeObject() throws Exception {
        return ocl.getContext().createIntBuffer(CLMem.Usage.InputOutput, emptyBuffer, true);
    }

    /**
     * Passivates the given buffer by writing over its contents with the empty buffer.
     *
     * @param passivate
     * @throws Exception
     */
    @Override
    public void passivateObject(Object passivate) throws Exception {
        ((CLIntBuffer) passivate).write(ocl.getQueue(), emptyBuffer, false);
    }

    /**
     * Destroys the given buffer by notifying the GPU it is able to discard it.
     *
     * @param buffer
     * @throws Exception
     */
    public void destroyObject(Object buffer) throws Exception {
        ((CLIntBuffer) buffer).release();
    }
}

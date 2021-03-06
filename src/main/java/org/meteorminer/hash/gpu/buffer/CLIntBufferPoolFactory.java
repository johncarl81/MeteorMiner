package org.meteorminer.hash.gpu.buffer;

import com.nativelibs4java.opencl.CLBuffer;
import com.nativelibs4java.opencl.CLMem;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.bridj.Pointer;
import org.meteorminer.config.binding.BufferSize;
import org.meteorminer.config.binding.SearchKernel;
import org.meteorminer.hash.gpu.KernelContext;

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
    private KernelContext kernelContext;

    @Inject
    public CLIntBufferPoolFactory(@BufferSize int bufferSize) {
        int[] emptyArray = new int[bufferSize];
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
        return kernelContext.getContext().createIntBuffer(CLMem.Usage.InputOutput, emptyBuffer, true);
    }

    /**
     * Passivates the given buffer by writing over its contents with the empty buffer.
     *
     * @param passivate
     * @throws Exception
     */
    @Override
    public void passivateObject(Object passivate) throws Exception {
        ((CLBuffer<Integer>) passivate).write(kernelContext.getQueue(), Pointer.pointerToInts(emptyBuffer), false);
    }

    /**
     * Destroys the given buffer by notifying the GPU it is able to discard it.
     *
     * @param buffer
     * @throws Exception
     */
    public void destroyObject(Object buffer) throws Exception {
        ((CLBuffer<Integer>) buffer).release();
    }
}

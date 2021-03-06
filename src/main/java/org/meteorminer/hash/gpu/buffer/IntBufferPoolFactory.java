package org.meteorminer.hash.gpu.buffer;

import com.nativelibs4java.util.NIOUtils;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.meteorminer.config.binding.BufferSize;
import org.meteorminer.config.binding.SearchKernel;
import org.meteorminer.hash.gpu.KernelContext;

import javax.inject.Inject;
import java.nio.IntBuffer;

/**
 * @author John Ericksen
 */
public class IntBufferPoolFactory extends BasePoolableObjectFactory {

    private int[] emptyArray;
    @Inject
    @SearchKernel
    private KernelContext kernelContext;

    private int bufferSize;

    @Inject
    public IntBufferPoolFactory(@BufferSize int bufferSize) {
        emptyArray = new int[bufferSize];
        this.bufferSize = bufferSize;
    }

    /**
     * Creates a new Direct IntBuffer.
     *
     * @return IntBuffer
     * @throws Exception
     */
    public Object makeObject() throws Exception {
        return NIOUtils.directInts(bufferSize, kernelContext.getContext().getByteOrder());
    }

    /**
     * Passivates the given object by filling it with the empty array.
     *
     * @param passivated
     * @throws Exception
     */
    public void passivateObject(Object passivated) throws Exception {
        ((IntBuffer) passivated).put(emptyArray);
    }

    /**
     * Noop destroy
     *
     * @param destroyed
     * @throws Exception
     */
    public void destroyObject(Object destroyed) {
        //noop
    }
}

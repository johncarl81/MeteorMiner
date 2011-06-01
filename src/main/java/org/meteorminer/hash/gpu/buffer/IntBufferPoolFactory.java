package org.meteorminer.hash.gpu.buffer;

import com.nativelibs4java.util.NIOUtils;
import org.apache.commons.pool.BasePoolableObjectFactory;
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

    private int size = 0xF;

    public IntBufferPoolFactory() {
        emptyArray = new int[size];
    }

    public Object makeObject() throws Exception {
        return NIOUtils.directInts(size, kernelContext.getContext().getByteOrder());
    }

    public void passivateObject(Object passivated) throws Exception {
        ((IntBuffer) passivated).put(emptyArray);
    }

    public void destroyObject(Object destroyed) {
        ((IntBuffer) destroyed).put(emptyArray);
    }
}

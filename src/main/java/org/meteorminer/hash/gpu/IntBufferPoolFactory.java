package org.meteorminer.hash.gpu;

import com.nativelibs4java.util.NIOUtils;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.meteorminer.binding.SearchKernel;

import javax.inject.Inject;
import java.nio.IntBuffer;

/**
 * @author John Ericksen
 */
public class IntBufferPoolFactory extends BasePoolableObjectFactory {

    private int[] emptyArray;
    @Inject
    @SearchKernel
    private OCL ocl;

    private int size = 0xF;

    public IntBufferPoolFactory() {
        emptyArray = new int[size];
    }

    public Object makeObject() throws Exception {
        return NIOUtils.directInts(size, ocl.getContext().getByteOrder());
    }

    public void passivateObject(Object passivated) throws Exception {
        ((IntBuffer) passivated).put(emptyArray);
    }

    public void destroyObject(Object destroyed) {
        ((IntBuffer) destroyed).put(emptyArray);
    }
}

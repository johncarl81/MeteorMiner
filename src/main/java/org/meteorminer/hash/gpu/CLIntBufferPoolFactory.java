package org.meteorminer.hash.gpu;

import com.nativelibs4java.opencl.CLIntBuffer;
import com.nativelibs4java.opencl.CLMem;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.meteorminer.binding.SearchKernel;

import javax.inject.Inject;
import java.nio.IntBuffer;
import java.util.Arrays;

/**
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

    public Object makeObject() throws Exception {
        return ocl.getContext().createIntBuffer(CLMem.Usage.InputOutput, emptyBuffer, true);
    }

    @Override
    public void passivateObject(Object passivate) throws Exception {
        ((CLIntBuffer) passivate).write(ocl.getQueue(), emptyBuffer, false);
    }

    public void destroyObject(Object buffer) throws Exception {
        ((CLIntBuffer) buffer).write(ocl.getQueue(), emptyBuffer, false);
        ((CLIntBuffer) buffer).release();
    }
}

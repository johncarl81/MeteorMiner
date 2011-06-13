package org.meteorminer.hash.gpu;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.nativelibs4java.opencl.JavaCL;
import org.apache.commons.pool.ObjectPool;
import org.junit.Before;
import org.junit.Test;
import org.meteorminer.config.MeteorAdvice;
import org.meteorminer.config.MeteorMinerInjector;
import org.meteorminer.config.binding.BufferSize;
import org.meteorminer.config.binding.CLIntBufferPool;
import org.meteorminer.config.binding.IntBufferPool;
import org.meteorminer.config.binding.SearchKernel;
import org.meteorminer.domain.GPUDevice;
import org.meteorminer.domain.Work;
import org.meteorminer.hash.WorkMockFactory;
import org.meteorminer.output.CLInterface;

import java.net.MalformedURLException;
import java.nio.IntBuffer;

import static junit.framework.Assert.assertEquals;

/**
 * @author John Ericksen
 */
public class MinerCoreTest {

    private KernelContext kernelContext;
    private ObjectPool intBufferPool;
    private ObjectPool clIntBufferPool;
    private GPUDevice device;
    private CLInterface output;
    private WorkMockFactory workMockFactory;
    private MeteorAdvice advice;
    private int bufferSize;

    @Before
    public void setup() throws MalformedURLException {
        Injector injector = MeteorMinerInjector.getGPUDeviceInjector(JavaCL.getBestDevice(), 0);
        kernelContext = injector.getInstance(Key.get(KernelContext.class, SearchKernel.class));
        intBufferPool = injector.getInstance(Key.get(ObjectPool.class, IntBufferPool.class));
        clIntBufferPool = injector.getInstance(Key.get(ObjectPool.class, CLIntBufferPool.class));
        device = injector.getInstance(GPUDevice.class);
        output = injector.getInstance(CLInterface.class);
        workMockFactory = injector.getInstance(WorkMockFactory.class);
        advice = injector.getInstance(MeteorAdvice.class);
        bufferSize = injector.getInstance(Key.get(Integer.class, BufferSize.class));
    }

    @Test
    public void testHash() throws Exception {
        Work work = workMockFactory.getSuccessfulWork2();

        int nonce = 563799816;

        MinerCore minerCore = new MinerCore(device, 4, -1, kernelContext, clIntBufferPool, intBufferPool, output, bufferSize);

        MinerResult result = minerCore.hash(nonce / advice.getVectors(), work);
        result.getEvent().waitFor();

        IntBuffer buffer = result.getBuffer();

        assertEquals(nonce, buffer.get(nonce & (bufferSize - 1)));
        assertEquals(nonce, buffer.get(bufferSize - 1));
    }
}

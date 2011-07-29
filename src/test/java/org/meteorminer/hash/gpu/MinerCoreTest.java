package org.meteorminer.hash.gpu;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.nativelibs4java.opencl.JavaCL;
import org.apache.commons.pool.ObjectPool;
import org.junit.Before;
import org.junit.Test;
import org.meteorminer.config.MeteorMinerInjector;
import org.meteorminer.config.advice.GPUDeviceAdvice;
import org.meteorminer.config.advice.MeteorAdvice;
import org.meteorminer.config.binding.*;
import org.meteorminer.domain.GPUDevice;
import org.meteorminer.domain.Work;
import org.meteorminer.hash.MockAdviceFactory;
import org.meteorminer.hash.WorkMockFactory;
import org.meteorminer.output.CLInterface;

import java.net.MalformedURLException;
import java.nio.IntBuffer;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author John Ericksen
 */
public class MinerCoreTest {

    private KernelContext kernelContext;
    private ObjectPool intBufferPool;
    private ObjectPool clIntBufferPool;
    private ObjectPool resultPool;
    private GPUDevice device;
    private CLInterface output;
    private WorkMockFactory workMockFactory;
    private int bufferSize;

    @Before
    public void setup() throws MalformedURLException {
        GPUDeviceAdvice gpuAdvice = MockAdviceFactory.getInstance().buildDefaultGPUAdvice();
        MeteorAdvice meteorAdvice = MockAdviceFactory.getInstance().buildDefaultMeteorAdvice();

        MeteorMinerInjector.getInjector(meteorAdvice);
        Injector injector = MeteorMinerInjector.getGPUDeviceInjector(JavaCL.getBestDevice(), gpuAdvice);
        kernelContext = injector.getInstance(Key.get(KernelContext.class, SearchKernel.class));
        intBufferPool = injector.getInstance(Key.get(ObjectPool.class, IntBufferPool.class));
        clIntBufferPool = injector.getInstance(Key.get(ObjectPool.class, CLIntBufferPool.class));
        resultPool = injector.getInstance(Key.get(ObjectPool.class, ResultPool.class));
        device = injector.getInstance(GPUDevice.class);
        output = injector.getInstance(CLInterface.class);
        workMockFactory = injector.getInstance(WorkMockFactory.class);
        bufferSize = injector.getInstance(Key.get(Integer.class, BufferSize.class));
    }

    @Test
    public void testHash() throws Exception {
        Work work = workMockFactory.getSuccessfulWork2();

        int nonce = 563799816;

        MinerCore minerCore = new MinerCore(device, 4, -1, kernelContext, clIntBufferPool, intBufferPool, output, bufferSize, resultPool);

        //todo:fix vector count
        //MinerResult result = minerCore.hash(nonce / advice.getVectors(), work);
        MinerResult result = minerCore.hash(nonce, work);
        result.getEvent().waitFor();

        IntBuffer buffer = result.getBuffer();

        boolean found = false;
        for (int i = 0; i < bufferSize; i++) {
            found |= buffer.get(i) == nonce;
        }

        assertTrue(found);
        assertEquals(nonce, buffer.get(bufferSize - 1));
    }
}

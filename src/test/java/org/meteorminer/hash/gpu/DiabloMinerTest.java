package org.meteorminer.hash.gpu;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.nativelibs4java.opencl.JavaCL;
import org.apache.commons.pool.ObjectPool;
import org.junit.Before;
import org.junit.Test;
import org.meteorminer.config.MeteorMinerInjector;
import org.meteorminer.config.binding.CLIntBufferPool;
import org.meteorminer.config.binding.IntBufferPool;
import org.meteorminer.config.binding.SearchKernel;
import org.meteorminer.domain.GPUDevice;
import org.meteorminer.domain.Work;

import java.net.MalformedURLException;
import java.nio.IntBuffer;

import static junit.framework.Assert.assertEquals;

/**
 * @author John Ericksen
 */
public class DiabloMinerTest {

    private KernelContext kernelContext;
    private ObjectPool intBufferPool;
    private ObjectPool clIntBufferPool;
    private GPUDevice device;

    @Before
    public void setup() throws MalformedURLException {
        Injector injector = MeteorMinerInjector.getGPUDeviceInjector(JavaCL.getBestDevice(), 0);
        kernelContext = injector.getInstance(Key.get(KernelContext.class, SearchKernel.class));
        intBufferPool = injector.getInstance(Key.get(ObjectPool.class, IntBufferPool.class));
        clIntBufferPool = injector.getInstance(Key.get(ObjectPool.class, CLIntBufferPool.class));
        device = injector.getInstance(GPUDevice.class);
    }

    @Test
    public void testHash() throws Exception {
        Work work = new Work(
                "00000001c9d358447ba95319a19300bfc94a286ed6a12856f8e4775e00005de6000000009c64db358b88376c70d0101aafaace8c46fb7988e9e3f070c234903fa7ed5aa24dd341741a6a93b300000000000000800000000000000000000000000000000000000000000000000000000000000000000000000000000080020000",
                "c94675051186fcd6fb7f92f20c7248da15efcc56924c77712220240573183c17",
                "00000000000000000000000000000000000000000000000000000000000000000000008000000000000000000000000000000000000000000000000000010000",
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffff00000000");

        int nonce = 563799816;

        DiabloMiner miner = new DiabloMiner(work, device, 10, -1, kernelContext, clIntBufferPool, intBufferPool);

        MinerResult result = miner.hash(nonce);
        result.getEvent().waitFor();

        IntBuffer buffer = result.getBuffer();

        assertEquals(nonce, buffer.get(nonce & 0xF));
    }
}

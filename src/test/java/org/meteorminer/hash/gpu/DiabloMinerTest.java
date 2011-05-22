package org.meteorminer.hash.gpu;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import junit.framework.TestCase;
import org.apache.commons.pool.ObjectPool;
import org.meteorminer.binding.CLIntBufferPool;
import org.meteorminer.binding.IntBufferPool;
import org.meteorminer.binding.SearchKernel;
import org.meteorminer.config.MeteorAdvice;
import org.meteorminer.config.MeteorMinerModule;
import org.meteorminer.domain.Work;

import java.net.MalformedURLException;
import java.nio.IntBuffer;

/**
 * @author John Ericksen
 */
public class DiabloMinerTest extends TestCase {

    private OCL ocl;
    private ObjectPool intBufferPool;
    private ObjectPool clIntBufferPool;

    public void setUp() throws MalformedURLException {
        Injector injector = Guice.createInjector(new MeteorMinerModule(new MeteorAdvice()));
        ocl = injector.getInstance(Key.get(OCL.class, SearchKernel.class));
        intBufferPool = injector.getInstance(Key.get(ObjectPool.class, IntBufferPool.class));
        clIntBufferPool = injector.getInstance(Key.get(ObjectPool.class, CLIntBufferPool.class));
    }

    public void testHash() throws Exception {
        Work work = new Work(
                "00000001c9d358447ba95319a19300bfc94a286ed6a12856f8e4775e00005de6000000009c64db358b88376c70d0101aafaace8c46fb7988e9e3f070c234903fa7ed5aa24dd341741a6a93b300000000000000800000000000000000000000000000000000000000000000000000000000000000000000000000000080020000",
                "c94675051186fcd6fb7f92f20c7248da15efcc56924c77712220240573183c17",
                "000000 00000000000000000000000000000000000000000000000000000000000000008000000000000000000000000000000000000000000000000000010000",
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffff00000000");

        int nonce = 563799816;

        DiabloMiner miner = new DiabloMiner(work, ocl, clIntBufferPool, intBufferPool);

        IntBuffer buffer = miner.hash(nonce, 1, 1);

        assertEquals(nonce, buffer.get(nonce & 0xF));
    }
}

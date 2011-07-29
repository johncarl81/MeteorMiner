package org.meteorminer.config.module;

import com.google.inject.AbstractModule;
import com.nativelibs4java.opencl.CLDevice;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.meteorminer.config.advice.GPUDeviceAdvice;
import org.meteorminer.config.binding.*;
import org.meteorminer.domain.Device;
import org.meteorminer.domain.GPUDevice;
import org.meteorminer.hash.HashScanner;
import org.meteorminer.hash.gpu.*;
import org.meteorminer.hash.gpu.buffer.CLIntBufferPoolFactory;
import org.meteorminer.hash.gpu.buffer.IntBufferPoolFactory;
import org.meteorminer.output.GPUStatistics;
import org.meteorminer.output.Statistics;

/**
 * Module defining the GPU device specific configurations.
 *
 * @author John Ericksen
 */
public class GPUDeviceModule extends AbstractModule {

    private CLDevice device;
    private GPUDeviceAdvice advice;

    public GPUDeviceModule(CLDevice device, GPUDeviceAdvice advice) {
        this.device = device;
        this.advice = advice;
    }

    @Override
    protected void configure() {

        bind(GPUDeviceAdvice.class).toInstance(advice);
        bind(CLDevice.class).toInstance(device);
        GPUDevice gpuDevice = new GPUDevice(device, advice.getId());
        bind(Device.class).toInstance(gpuDevice);
        bind(GPUDevice.class).toInstance(gpuDevice);

        bind(HashScanner.class).to(GpuHashScanner.class);

        bind(KernelContext.class).annotatedWith(SearchKernel.class).toProvider(SearchKernelContextProvider.class).asEagerSingleton();
        bind(HashChecker.class).annotatedWith(Synchronous.class).to(HashCheckerImpl.class);

        bind(Statistics.class).toInstance(new GPUStatistics());

        int bufferSize = 0xFF;

        bind(Integer.class).annotatedWith(BufferSize.class).toInstance(bufferSize);

        //ObjectPool setup
        IntBufferPoolFactory intBufferPoolFactory = new IntBufferPoolFactory(bufferSize);
        CLIntBufferPoolFactory clIntBufferPoolFactory = new CLIntBufferPoolFactory(bufferSize);
        RunnableHashCheckerPoolFactory runnableHashCheckerFactory = new RunnableHashCheckerPoolFactory();
        MinerResultPoolFactory minerResultPoolFactory = new MinerResultPoolFactory();

        requestInjection(intBufferPoolFactory);
        requestInjection(clIntBufferPoolFactory);
        requestInjection(runnableHashCheckerFactory);
        requestInjection(minerResultPoolFactory);

        GenericObjectPool.Config config = new GenericObjectPool.Config();
        config.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_GROW;

        bind(ObjectPool.class).annotatedWith(IntBufferPool.class).toInstance(new GenericObjectPool(intBufferPoolFactory, config));
        bind(ObjectPool.class).annotatedWith(CLIntBufferPool.class).toInstance(new GenericObjectPool(clIntBufferPoolFactory, config));
        bind(ObjectPool.class).annotatedWith(RunnableHashCheckerPool.class).toInstance(new GenericObjectPool(runnableHashCheckerFactory, config));
        bind(ObjectPool.class).annotatedWith(ResultPool.class).toInstance(new GenericObjectPool(minerResultPoolFactory, config));

        bind(Integer.class).annotatedWith(Intensity.class).toInstance(advice.getIntensity());
        bind(Integer.class).annotatedWith(WorkSize.class).toInstance(advice.getWorksize());
        bind(Integer.class).annotatedWith(Vectors.class).toInstance(advice.getVectors());


    }
}

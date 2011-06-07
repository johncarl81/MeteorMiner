package org.meteorminer.config;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.nativelibs4java.opencl.CLDevice;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.meteorminer.config.binding.CLIntBufferPool;
import org.meteorminer.config.binding.IntBufferPool;
import org.meteorminer.config.binding.SearchKernel;
import org.meteorminer.config.binding.Synchronous;
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
    private int id;

    public GPUDeviceModule(CLDevice device, int id) {
        this.device = device;
        this.id = id;
    }

    @Override
    protected void configure() {

        //Assisted injection factories
        FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder();

        install(factoryModuleBuilder
                .build((RunnableHashCheckerFactory.class)));

        bind(CLDevice.class).toInstance(device);
        GPUDevice gpuDevice = new GPUDevice(device, id);
        bind(Device.class).toInstance(gpuDevice);
        bind(GPUDevice.class).toInstance(gpuDevice);

        bind(HashScanner.class).to(GpuHashScanner.class);

        bind(KernelContext.class).annotatedWith(SearchKernel.class).toProvider(SearchKernelContextProvider.class).asEagerSingleton();

        bind(HashChecker.class).annotatedWith(Synchronous.class).to(HashCheckerImpl.class);

        bind(Statistics.class).toInstance(new GPUStatistics());

        //ObjectPool setup
        IntBufferPoolFactory intBufferPoolFactory = new IntBufferPoolFactory();
        CLIntBufferPoolFactory clIntBufferPoolFactory = new CLIntBufferPoolFactory();

        requestInjection(intBufferPoolFactory);
        requestInjection(clIntBufferPoolFactory);

        GenericObjectPool.Config config = new GenericObjectPool.Config();
        config.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_GROW;
        //config.lifo = false;

        bind(ObjectPool.class).annotatedWith(IntBufferPool.class).toInstance(new GenericObjectPool(intBufferPoolFactory, config));
        bind(ObjectPool.class).annotatedWith(CLIntBufferPool.class).toInstance(new GenericObjectPool(clIntBufferPoolFactory, config));

    }
}

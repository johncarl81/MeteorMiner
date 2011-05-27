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
import org.meteorminer.output.GPUStatistics;
import org.meteorminer.output.Statistics;

/**
 * @author John Ericksen
 */
public class GPUDeviceModule extends AbstractModule {

    private CLDevice device;

    public GPUDeviceModule(CLDevice device) {
        this.device = device;
    }

    @Override
    protected void configure() {

        //Assisted injection factories
        FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder();

        install(factoryModuleBuilder
                .build((DiabloMinerFactory.class)));

        install(factoryModuleBuilder
                .build((RunnableHashCheckerFactory.class)));

        bind(CLDevice.class).toInstance(device);
        bind(Device.class).toInstance(new GPUDevice(device));

        bind(HashScanner.class).to(GpuHashScanner.class);

        bind(KernelContext.class).annotatedWith(SearchKernel.class).toProvider(KernelContextProvider.class).asEagerSingleton();

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

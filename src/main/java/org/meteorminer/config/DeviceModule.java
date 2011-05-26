package org.meteorminer.config;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.meteorminer.hash.HashStatisticsOutputTimerTaskFactory;
import org.meteorminer.hash.InteruptTimerTaskFactory;
import org.meteorminer.hash.MinerController;
import org.meteorminer.network.*;
import org.meteorminer.service.*;

/**
 * @author John Ericksen
 */
public class DeviceModule extends AbstractModule {
    @Override
    protected void configure() {
        //Assisted injection factories
        FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder();

        install(factoryModuleBuilder
                .build((InteruptTimerTaskFactory.class)));

        install(factoryModuleBuilder
                .implement(WorkProducer.class, WorkProducerMultiplex.class)
                .build(WorkProducerFactory.class));

        install(factoryModuleBuilder
                .implement(Runnable.class, LongPollWorker.class)
                .build((LongPollWorkerFactory.class)));

        install(factoryModuleBuilder
                .build((HashStatisticsOutputTimerTaskFactory.class)));

        install(factoryModuleBuilder
                .build((JsonCommandFactory.class)));

        bind(Miner.class).asEagerSingleton();

        bind(LongPollAdaptor.class).asEagerSingleton();
        bind(MinerController.class).asEagerSingleton();
        bind(DelayedWorkProducer.class).asEagerSingleton();
        bind(LongPollWorkProducer.class).asEagerSingleton();
        bind(WorkProducerMultiplex.class).asEagerSingleton();
    }
}

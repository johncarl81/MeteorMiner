package org.meteorminer.config;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.meteorminer.hash.*;
import org.meteorminer.network.LongPollAdaptor;
import org.meteorminer.network.LongPollWorkProducer;
import org.meteorminer.network.LongPollWorker;
import org.meteorminer.network.LongPollWorkerFactory;
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
                .implement(InteruptTimerTask.class, InteruptTimerTask.class)
                .build((InteruptTimerTaskFactory.class)));

        install(factoryModuleBuilder
                .implement(WorkProducer.class, WorkProducerMultiplex.class)
                .build(WorkProducerFactory.class));

        install(factoryModuleBuilder
                .implement(WorkConsumer.class, WorkConsumer.class)
                .build((WorkConsumerFactory.class)));

        install(factoryModuleBuilder
                .implement(Runnable.class, LongPollWorker.class)
                .build((LongPollWorkerFactory.class)));

        install(factoryModuleBuilder
                .implement(HashStatisticsOutputTimerTask.class, HashStatisticsOutputTimerTask.class)
                .build((HashStatisticsOutputTimerTaskFactory.class)));

        bind(LongPollAdaptor.class).asEagerSingleton();
        bind(MinerController.class).asEagerSingleton();
        bind(DelayedWorkProducer.class).asEagerSingleton();
        bind(LongPollWorkProducer.class).asEagerSingleton();
    }
}

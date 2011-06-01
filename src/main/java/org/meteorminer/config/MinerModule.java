package org.meteorminer.config;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.meteorminer.hash.WorkConsumer;
import org.meteorminer.network.LongPollWorker;
import org.meteorminer.network.LongPollWorkerFactory;
import org.meteorminer.service.MinerFactory;

/**
 * @author John Ericksen
 */
public class MinerModule extends AbstractModule {
    @Override
    protected void configure() {

        FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder();

        install(factoryModuleBuilder
                .build((MinerFactory.class)));

        install(factoryModuleBuilder
                .implement(Runnable.class, LongPollWorker.class)
                .build((LongPollWorkerFactory.class)));

        bind(WorkConsumer.class).asEagerSingleton();
    }
}

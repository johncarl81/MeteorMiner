package org.meteorminer.config;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.meteorminer.domain.Work;
import org.meteorminer.hash.WorkConsumer;
import org.meteorminer.network.LongPollWorker;
import org.meteorminer.network.LongPollWorkerFactory;
import org.meteorminer.service.MinerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

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

        bind(new TypeLiteral<BlockingQueue<Work>>() {
        }).toInstance(new ArrayBlockingQueue<Work>(1));

        bind(WorkConsumer.class).asEagerSingleton();
    }
}

package org.meteorminer.config;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;
import org.meteorminer.network.RPCExtension;
import org.meteorminer.network.longpoll.LongPollExtension;
import org.meteorminer.network.longpoll.LongPollWorker;
import org.meteorminer.network.longpoll.LongPollWorkerFactory;

/**
 * @author John Ericksen
 */
public class LongPollExtensionModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<RPCExtension> uriBinder = Multibinder.newSetBinder(binder(), RPCExtension.class);
        uriBinder.addBinding().to(LongPollExtension.class);

        FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder();

        install(factoryModuleBuilder
                .implement(Runnable.class, LongPollWorker.class)
                .build((LongPollWorkerFactory.class)));
    }
}

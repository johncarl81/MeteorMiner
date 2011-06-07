package org.meteorminer.config;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;
import org.meteorminer.network.RPCExtension;
import org.meteorminer.network.longpoll.LongPollExtension;
import org.meteorminer.network.longpoll.LongPollWorker;
import org.meteorminer.network.longpoll.LongPollWorkerFactory;

/**
 * Long Polling RPC extension module.
 *
 * @author John Ericksen
 */
public class LongPollExtensionModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<RPCExtension> rpcExtensionMultibinder = Multibinder.newSetBinder(binder(), RPCExtension.class);
        rpcExtensionMultibinder.addBinding().to(LongPollExtension.class).asEagerSingleton();

        FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder();

        install(factoryModuleBuilder
                .implement(Runnable.class, LongPollWorker.class)
                .build((LongPollWorkerFactory.class)));
    }
}

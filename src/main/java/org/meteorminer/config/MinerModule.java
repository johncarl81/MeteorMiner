package org.meteorminer.config;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.meteorminer.hash.NonceSource;
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

        bind(NonceSource.class).asEagerSingleton();
    }
}

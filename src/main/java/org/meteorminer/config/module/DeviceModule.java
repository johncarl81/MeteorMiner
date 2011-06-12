package org.meteorminer.config.module;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.meteorminer.hash.HashStatisticsOutputTimerTaskFactory;
import org.meteorminer.hash.NonceIteratorFactory;
import org.meteorminer.hash.NonceSourceIterator;
import org.meteorminer.network.JsonCommandFactory;

import java.util.Iterator;

/**
 * Module defining general device configuration.  This should be used as a base for all device modules.
 *
 * @author John Ericksen
 */
public class DeviceModule extends AbstractModule {
    @Override
    protected void configure() {
        //Assisted injection factories
        FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder();

        install(factoryModuleBuilder
                .build((HashStatisticsOutputTimerTaskFactory.class)));

        install(factoryModuleBuilder
                .build((JsonCommandFactory.class)));

        install(factoryModuleBuilder
                .implement(new TypeLiteral<Iterator<Integer>>() {
                        }, NonceSourceIterator.class)
                .build(NonceIteratorFactory.class));
    }
}

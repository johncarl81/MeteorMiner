package org.meteorminer.hash;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * @author John Ericksen
 */
public class SynchronousModule extends AbstractModule {

    @Override
    protected void configure() {
        FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder();

        install(factoryModuleBuilder
                .implement(org.meteorminer.hash.WorkFoundCallbackTester.class, org.meteorminer.hash.WorkFoundCallbackTester.class)
                .build(WorkFoundCallbackTesterFactory.class));
    }
}

package org.meteorminer.hash;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.meteorminer.hash.gpu.HashChecker;
import org.meteorminer.hash.gpu.HashCheckerImpl;

/**
 * @author John Ericksen
 */
public class SynchronousModule extends AbstractModule {

    @Override
    protected void configure() {
        FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder();

        install(factoryModuleBuilder
                .implement(org.meteorminer.hash.WorkFoundCallbackTester.class, org.meteorminer.hash.WorkFoundCallbackTester.class)
                .build(WorkFoundCallbackFactory.class));

        //bind directly to avoid asynchronous behaviour with
        bind(HashChecker.class).to(HashCheckerImpl.class);
    }
}

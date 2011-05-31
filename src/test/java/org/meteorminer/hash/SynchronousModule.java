package org.meteorminer.hash;

import com.google.inject.AbstractModule;
import org.meteorminer.service.WorkFoundCallback;

/**
 * @author John Ericksen
 */
public class SynchronousModule extends AbstractModule {

    @Override
    protected void configure() {
        WorkFoundCallbackTester callbackTester = new WorkFoundCallbackTester();
        bind(WorkFoundCallback.class).toInstance(callbackTester);
        bind(WorkFoundCallbackTester.class).toInstance(callbackTester);

        MockNonceIteratorFactory nonceIteratorFactory = new MockNonceIteratorFactory();
        bind(NonceIteratorFactory.class).toInstance(nonceIteratorFactory);
        bind(MockNonceIteratorFactory.class).toInstance(nonceIteratorFactory);
    }
}

package org.meteorminer.hash;

import com.google.inject.AbstractModule;
import org.meteorminer.hash.gpu.HashChecker;
import org.meteorminer.hash.gpu.HashCheckerImpl;

/**
 * @author John Ericksen
 */
public class GPUSynchronousModule extends AbstractModule {

    @Override
    protected void configure() {
        //bind directly to avoid asynchronous behaviour with
        bind(HashChecker.class).to(HashCheckerImpl.class);
    }
}

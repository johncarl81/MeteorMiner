package org.meteorminer.hash.gpu;

import com.google.inject.Inject;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.meteorminer.config.binding.Synchronous;

/**
 * @author John Ericksen
 */
public class RunnableHashCheckerPoolFactory extends BasePoolableObjectFactory {

    @Inject
    @Synchronous
    private HashChecker delegate;

    @Override
    public Object makeObject() throws Exception {
        return new RunnableHashChecker(delegate);
    }

    @Override
    public void passivateObject(Object obj) throws Exception {
        ((RunnableHashChecker) obj).clear();
    }
}

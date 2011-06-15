package org.meteorminer.hash.gpu;

import org.apache.commons.pool.BasePoolableObjectFactory;

/**
 * @author John Ericksen
 */
public class MinerResultPoolFactory extends BasePoolableObjectFactory {

    @Override
    public Object makeObject() throws Exception {
        return new MinerResult();
    }

    @Override
    public void passivateObject(Object obj) throws Exception {
        ((MinerResult) obj).clear();
    }
}

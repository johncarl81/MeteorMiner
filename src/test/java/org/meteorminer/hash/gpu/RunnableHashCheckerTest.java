package org.meteorminer.hash.gpu;

import org.apache.commons.pool.ObjectPool;
import org.junit.Before;
import org.junit.Test;
import org.meteorminer.domain.Work;

import static org.easymock.EasyMock.*;

/**
 * @author John Ericksen
 */
public class RunnableHashCheckerTest {

    private RunnableHashChecker hashChecker;

    private MinerResult output;
    private Work work;
    private HashChecker delegate;
    private ObjectPool runnablePool;

    @Before
    public void setup() {

        output = createMock(MinerResult.class);
        work = createMock(Work.class);
        delegate = createMock(HashChecker.class);
        runnablePool = createMock(ObjectPool.class);

        hashChecker = new RunnableHashChecker(delegate);
        hashChecker.setup(output, work, runnablePool);
    }

    @Test
    public void testRun() throws Exception {
        reset(output, work, delegate);

        delegate.check(output, work);
        runnablePool.returnObject(hashChecker);

        replay(output, work, delegate);

        hashChecker.run();

        verify(output, work, delegate);
    }
}

package org.meteorminer.hash.gpu;

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

    @Before
    public void setup() {

        output = createMock(MinerResult.class);
        work = createMock(Work.class);
        delegate = createMock(HashChecker.class);

        hashChecker = new RunnableHashChecker(output, work, delegate);
    }

    @Test
    public void testRun() {
        reset(output, work, delegate);

        delegate.check(output, work);

        replay(output, work, delegate);

        hashChecker.run();

        verify(output, work, delegate);
    }
}

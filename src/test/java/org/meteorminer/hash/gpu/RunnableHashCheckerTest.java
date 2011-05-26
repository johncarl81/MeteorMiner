package org.meteorminer.hash.gpu;

import org.junit.Before;
import org.junit.Test;
import org.meteorminer.domain.Work;
import org.meteorminer.service.WorkFoundCallback;

import static org.easymock.EasyMock.*;

/**
 * @author John Ericksen
 */
public class RunnableHashCheckerTest {

    private RunnableHashChecker hashChecker;

    private MinerResult output;
    private Work work;
    private WorkFoundCallback workFoundCallback;
    private HashChecker delegate;

    @Before
    public void setup() {

        output = createMock(MinerResult.class);
        work = createMock(Work.class);
        workFoundCallback = createMock(WorkFoundCallback.class);
        delegate = createMock(HashChecker.class);

        hashChecker = new RunnableHashChecker(output, work, workFoundCallback, delegate);
    }

    @Test
    public void testRun() {
        reset(output, work, workFoundCallback, delegate);

        delegate.check(output, work, workFoundCallback);

        replay(output, work, workFoundCallback, delegate);

        hashChecker.run();

        verify(output, work, workFoundCallback, delegate);
    }
}

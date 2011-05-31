package org.meteorminer.hash.gpu;

import com.nativelibs4java.opencl.CLEvent;
import org.junit.Before;
import org.junit.Test;
import org.meteorminer.domain.Work;

import static org.easymock.EasyMock.*;

/**
 * @author John Ericksen
 */
public class AsynchronousHashCheckerTest {

    private AsynchronousHashChecker hashChecker;
    private RunnableHashCheckerFactory hashCheckerFactory;
    private RunnableHashChecker runnableHashChecker;

    private MinerResult output;
    private Work work;
    private CLEvent event;

    @Before
    public void setup() {
        hashChecker = new AsynchronousHashChecker();

        hashCheckerFactory = createMock(RunnableHashCheckerFactory.class);
        runnableHashChecker = createMock(RunnableHashChecker.class);
        work = new Work(
                "00000001c9d358447ba95319a19300bfc94a286ed6a12856f8e4775e00005de6000000009c64db358b88376c70d0101aafaace8c46fb7988e9e3f070c234903fa7ed5aa24dd341741a6a93b300000000000000800000000000000000000000000000000000000000000000000000000000000000000000000000000080020000",
                "c94675051186fcd6fb7f92f20c7248da15efcc56924c77712220240573183c17",
                "00000000000000000000000000000000000000000000000000000000000000000000008000000000000000000000000000000000000000000000000000010000",
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffff00000000");

        output = createMock(MinerResult.class);
        event = createMock(CLEvent.class);

        hashChecker.setRunnableHashCheckerFactory(hashCheckerFactory);
    }

    @Test
    public void testCheck() {
        reset(hashCheckerFactory, runnableHashChecker, output);

        expect(hashCheckerFactory.createHashChecker(eq(output), eq(work))).andReturn(runnableHashChecker);
        expect(output.getEvent()).andReturn(event);
        event.invokeUponCompletion(runnableHashChecker);

        replay(hashCheckerFactory, runnableHashChecker, output);

        hashChecker.check(output, work);

        verify(hashCheckerFactory, runnableHashChecker, output);
    }
}

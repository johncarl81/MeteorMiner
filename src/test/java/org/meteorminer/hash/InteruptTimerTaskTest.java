package org.meteorminer.hash;

import org.junit.Before;
import org.junit.Test;
import org.meteorminer.service.DelayedWorkProducer;

import static org.easymock.EasyMock.*;

/**
 * @author John Ericksen
 */
public class InteruptTimerTaskTest {

    private InteruptTimerTask interuptTimerTask;

    private MinerController minerController;
    private DelayedWorkProducer workProducer;

    @Before
    public void setup() {

        minerController = createMock(MinerController.class);
        workProducer = createMock(DelayedWorkProducer.class);

        interuptTimerTask = new InteruptTimerTask(minerController, workProducer);
    }

    @Test
    public void testRun() {
        reset(minerController, workProducer);

        workProducer.delayedProduce();
        minerController.interruptProduction();

        replay(minerController, workProducer);

        interuptTimerTask.run();

        verify(minerController, workProducer);
    }
}

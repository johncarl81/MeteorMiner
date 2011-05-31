package org.meteorminer.hash;

import org.junit.Before;
import org.junit.Test;
import org.meteorminer.service.DelayedWorkProducer;

import java.util.Collections;

import static org.easymock.EasyMock.*;

/**
 * @author John Ericksen
 */
public class InteruptTimerTaskTest {

    private InterruptTimerTask interuptTimerTask;

    private HashScanner hashScanner;
    private DelayedWorkProducer workProducer;

    @Before
    public void setup() {

        hashScanner = createMock(HashScanner.class);
        workProducer = createMock(DelayedWorkProducer.class);

        interuptTimerTask = new InterruptTimerTask(Collections.singleton(hashScanner), workProducer);
    }

    @Test
    public void testRun() {
        reset(hashScanner, workProducer);

        workProducer.delayedProduce();
        hashScanner.stop();

        replay(hashScanner, workProducer);

        interuptTimerTask.run();

        verify(hashScanner, workProducer);
    }
}

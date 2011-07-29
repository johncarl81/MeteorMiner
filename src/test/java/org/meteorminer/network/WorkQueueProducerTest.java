package org.meteorminer.network;

import org.junit.Before;
import org.junit.Test;
import org.meteorminer.domain.Work;
import org.meteorminer.output.CLInterface;
import org.meteorminer.service.WorkProducer;

import java.util.concurrent.BlockingQueue;

import static org.easymock.EasyMock.*;

/**
 * @author John Ericksen
 */
public class WorkQueueProducerTest {

    private WorkQueueProducer workQueueProducer;
    private CLInterface output;
    private WorkProducer workProducer;
    private BlockingQueue<Work> workQueue;
    private Work testWork;

    @Before
    public void setup() {

        testWork = createMock(Work.class);
        output = createMock(CLInterface.class);
        workProducer = createMock(WorkProducer.class);
        workQueue = createMock(BlockingQueue.class);


        workQueueProducer = new WorkQueueProducer();
        //only one loop
        workQueueProducer.setRunning(false);
        workQueueProducer.setOutput(output);
        workQueueProducer.setWorkProducer(workProducer);
        workQueueProducer.setWorkQueue(workQueue);
    }

    @Test
    public void runTest() throws InterruptedException {
        reset(output, workProducer, workQueue);

        expect(workProducer.produce()).andReturn(testWork);
        workQueue.put(testWork);

        replay(output, workProducer, workQueue);

        workQueueProducer.run();

        verify(output, workProducer, workQueue);
    }

    @Test
    public void runNullWorkErrorTest() throws InterruptedException {

        reset(output, workProducer, workQueue);

        expect(workProducer.produce()).andReturn(null);
        //nothing happens with this work.

        replay(output, workProducer, workQueue);

        workQueueProducer.run();

        verify(output, workProducer, workQueue);
    }

    @Test
    public void runThrowExceptionTest() throws InterruptedException {
        reset(output, workProducer, workQueue);

        expect(workProducer.produce()).andReturn(testWork);
        InterruptedException interruptedException = new InterruptedException();
        workQueue.put(testWork);
        expectLastCall().andThrow(interruptedException);
        output.error(interruptedException);

        replay(output, workProducer, workQueue);

        workQueueProducer.run();

        verify(output, workProducer, workQueue);
    }
}

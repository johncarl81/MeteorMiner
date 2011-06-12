package org.meteorminer.service;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.meteorminer.config.module.ThreadPoolFactory;
import org.meteorminer.hash.HashScanner;
import org.meteorminer.hash.WorkConsumer;
import org.meteorminer.network.WorkQueueProducer;
import org.meteorminer.output.CLInterface;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;

/**
 * @author John Ericksen
 */
public class MinerTest {

    private Miner miner;
    private Set<HashScanner> scanners;
    private WorkConsumer workSource;
    private WorkQueueProducer workQueueProducer;
    private AsynchronousFactory asynchronousFactory;
    private CLInterface output;
    private ScannerShutdownFactory scannerShutdownFactory;
    private Runtime runtime;
    private ThreadPoolFactory threadPoolFactory;
    private ExecutorService executor;
    private int testSize;

    @Before
    public void setup() {

        scanners = new HashSet<HashScanner>();

        for (int i = 0; i < 10; i++) {
            scanners.add(createMock(HashScanner.class));
        }

        workSource = createMock(WorkConsumer.class);
        workQueueProducer = createMock(WorkQueueProducer.class);
        asynchronousFactory = createMock(AsynchronousFactory.class);
        output = createMock(CLInterface.class);
        scannerShutdownFactory = createMock(ScannerShutdownFactory.class);
        runtime = createMock(Runtime.class);
        threadPoolFactory = createMock(ThreadPoolFactory.class);
        executor = createMock(ExecutorService.class);

        testSize = scanners.size();

        ScannerShutdownThread scannerShutdownThread = createMock(ScannerShutdownThread.class);

        //the following test in this before setup method both tests the construction of the miner, and
        //sets the executor into the miner.
        reset();

        expect(threadPoolFactory.getFixedThreadPool(testSize)).andReturn(executor);
        expect(scannerShutdownFactory.buildScannerShutdown(executor, scanners)).andReturn(scannerShutdownThread);
        runtime.addShutdownHook(scannerShutdownThread);

        replay();

        miner = new Miner(scanners, workSource, workQueueProducer, asynchronousFactory, output, scannerShutdownFactory, runtime, threadPoolFactory);

        verify();
    }

    @Test
    public void runTest() {
        reset();

        asynchronousFactory.startRunnable(workQueueProducer);
        workSource.updateWork();

        for (HashScanner scanner : scanners) {
            executor.execute(scanner);
        }

        replay();

        miner.run();

        verify();
    }

    private void reset() {
        EasyMock.reset(executor, workSource, workQueueProducer, asynchronousFactory, output, scannerShutdownFactory, runtime, threadPoolFactory);
    }

    private void replay() {
        EasyMock.replay(executor, workSource, workQueueProducer, asynchronousFactory, output, scannerShutdownFactory, runtime, threadPoolFactory);
    }

    private void verify() {
        EasyMock.verify(executor, workSource, workQueueProducer, asynchronousFactory, output, scannerShutdownFactory, runtime, threadPoolFactory);
    }
}

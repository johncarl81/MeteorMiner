package org.meteorminer.service;

import org.junit.Before;
import org.junit.Test;
import org.meteorminer.hash.HashScanner;

import java.util.Collections;
import java.util.Set;

import static org.easymock.EasyMock.*;

/**
 * @author John Ericksen
 */
public class ParallelMinerStrategyTest {

    private ParallelMinerStrategy parallelMinerStrategy;
    private AsynchronousFactory asynchronousFactory;
    private MinerFactory minerFactory;
    private Set<HashScanner> scanners;
    private Miner miner;

    @Before
    public void setup() {

        asynchronousFactory = createMock(AsynchronousFactory.class);
        minerFactory = createMock(MinerFactory.class);
        miner = createMock(Miner.class);

        parallelMinerStrategy = new ParallelMinerStrategy();
        for (int i = 0; i < 10; i++) {
            parallelMinerStrategy.add(createMock(HashScanner.class));
        }
        scanners = parallelMinerStrategy.getScanners();
        parallelMinerStrategy.setMinerFactory(minerFactory);
        parallelMinerStrategy.setAsynchronousFactory(asynchronousFactory);
    }

    @Test
    public void startTest() {
        reset(asynchronousFactory, minerFactory);

        for (HashScanner scanner : scanners) {
            expect(minerFactory.createMiner(eq(Collections.singleton(scanner)))).andReturn(miner);
            asynchronousFactory.startRunnable(miner);
        }

        replay(asynchronousFactory, minerFactory);

        parallelMinerStrategy.start();

        verify(asynchronousFactory, minerFactory);
    }
}

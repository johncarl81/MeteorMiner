package org.meteorminer.service;

import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import org.meteorminer.config.MeteorMinerInjector;
import org.meteorminer.config.advice.MeteorAdvice;
import org.meteorminer.hash.HashScanner;
import org.meteorminer.hash.MockAdviceFactory;

import java.util.Set;

import static org.easymock.EasyMock.*;

/**
 * @author John Ericksen
 */
public class TandemMinerStrategyTest {

    private TandemMinerStrategy tandemMinerStrategy;
    private AsynchronousFactory asynchronousFactory;
    private MinerFactory minerFactory;
    private Set<HashScanner> scanners;
    private Miner miner;

    @Before
    public void setup() {
        MeteorAdvice meteorAdvice = MockAdviceFactory.getInstance().buildDefaultMeteorAdvice();
        meteorAdvice.setTandem(true);
        Injector injector = MeteorMinerInjector.getInjector(meteorAdvice);

        asynchronousFactory = createMock(AsynchronousFactory.class);
        minerFactory = createMock(MinerFactory.class);
        miner = createMock(Miner.class);

        tandemMinerStrategy = injector.getInstance(TandemMinerStrategy.class);
        for (int i = 0; i < 10; i++) {
            tandemMinerStrategy.add(createMock(HashScanner.class));
        }
        scanners = tandemMinerStrategy.getScanners();
        tandemMinerStrategy.setMinerFactory(minerFactory);
        tandemMinerStrategy.setAsynchronousFactory(asynchronousFactory);
    }

    @Test
    public void startTest() {
        reset(asynchronousFactory, minerFactory);

        expect(minerFactory.createMiner(eq(scanners))).andReturn(miner);
        asynchronousFactory.startRunnable(miner);

        replay(asynchronousFactory, minerFactory);

        tandemMinerStrategy.start();

        verify(asynchronousFactory, minerFactory);
    }
}

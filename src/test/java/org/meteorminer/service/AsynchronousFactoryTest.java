package org.meteorminer.service;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutorService;

import static org.easymock.EasyMock.*;

/**
 * @author John Ericksen
 */
public class AsynchronousFactoryTest {

    private AsynchronousFactory asynchronousFactory;
    private ExecutorService executor;
    private Runnable runnable;

    @Before
    public void setup() {
        executor = createMock(ExecutorService.class);
        runnable = createMock(Runnable.class);

        asynchronousFactory = new AsynchronousFactory();
        asynchronousFactory.setExecutor(executor);
    }

    @Test
    public void startRunnableTest() {
        reset(executor, runnable);

        executor.execute(runnable);

        replay(executor, runnable);

        asynchronousFactory.startRunnable(runnable);

        verify(executor, runnable);
    }
}

package org.meteorminer.service;

import org.junit.Before;
import org.junit.Test;
import org.meteorminer.domain.Work;
import org.meteorminer.network.JsonCommandFactory;
import org.meteorminer.network.WorkSubmit;

import static junit.framework.Assert.assertEquals;
import static org.easymock.EasyMock.*;

/**
 * @author John Ericksen
 */
public class WorkFoundCallbackImplTest {

    private WorkFoundCallbackImpl workFoundCallback;

    private AsynchronousFactory asyncFactory;
    private JsonCommandFactory commandFactory;
    private WorkSubmit workSubmit;

    private Work work;
    private int nonce;

    @Before
    public void setup() {
        workFoundCallback = new WorkFoundCallbackImpl();

        asyncFactory = createMock(AsynchronousFactory.class);
        commandFactory = createMock(JsonCommandFactory.class);
        workSubmit = createMock(WorkSubmit.class);

        workFoundCallback.setAsyncFactory(asyncFactory);
        workFoundCallback.setCommandFactory(commandFactory);

        work = new org.meteorminer.domain.Work(
                "00000001569be4f2b5b23e745240aaa149084029850973b78b0c5ce40002f41600000000f1c3d9c8a8a701275715da32e577521340180146e3517c8ebb1d0044feaa9f3f4d1d355d1b04864c00000000000000800000000000000000000000000000000000000000000000000000000000000000000000000000000080020000",
                "babe3a22106e8bd2274bcb1571042c5dde2ee927a3fb62606938ab8ae7b241ba",
                "00000000000000000000000000000000000000000000000000000000000000000000008000000000000000000000000000000000000000000000000000010000",
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffff00000000");
        nonce = 123456;

    }

    @Test
    public void testFound() {
        reset(asyncFactory, commandFactory, workSubmit);

        expect(commandFactory.buildWorkSubmit(work, nonce)).andReturn(workSubmit);

        asyncFactory.startRunnable(workSubmit);

        replay(asyncFactory, commandFactory, workSubmit);

        workFoundCallback.found(work, nonce);

        assertEquals(work.getData()[19], nonce);

        verify(asyncFactory, commandFactory, workSubmit);
    }
}

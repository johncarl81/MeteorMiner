package org.meteorminer.network.failover;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static junit.framework.Assert.assertEquals;
import static org.easymock.EasyMock.*;

/**
 * @author John Ericksen
 */
public class FailoverServerMaintenanceDecoratorTest {

    private static final String TEST_DOMAIN = "localhost";
    private static final int TEST_PORT = 80;

    @Test
    public void basicEqualsTest() {
        FailoverServer failoverServer = new FailoverServer(TEST_DOMAIN, TEST_PORT, 40);
        FailoverServerMaintenanceDecorator failoverServerMaintenanceDecorator = new FailoverServerMaintenanceDecorator(failoverServer);

        URL url = failoverServerMaintenanceDecorator.getUrl();

        assertEquals(TEST_DOMAIN, url.getHost());
        assertEquals(TEST_PORT, url.getPort());
        assertEquals(failoverServer.getUrl(), url);
    }

    @Test
    public void failoverExpireTest() throws InterruptedException, MalformedURLException {
        FailoverServer failoverServer = new FailoverServer(TEST_DOMAIN, TEST_PORT, 1);
        FailoverServerMaintenanceDecorator failoverServerMaintenanceDecorator = new FailoverServerMaintenanceDecorator(failoverServer);

        FailoverServerDecoratorFactory decorated = createMock(FailoverServerDecoratorFactory.class);

        failoverServerMaintenanceDecorator.decorate(decorated);

        assertEquals(failoverServer.getUrl(), failoverServerMaintenanceDecorator.getUrl());

        Thread.sleep(1000);

        URL testUrl = new URL("http://localhost:8080");
        //should have expired
        reset(decorated);

        expect(decorated.update()).andReturn(decorated);
        expect(decorated.getUrl()).andReturn(testUrl);

        replay(decorated);

        FailoverServerDecoratorFactory failoverServerDecoratorFactory = failoverServerMaintenanceDecorator.update();
        assertEquals(testUrl, failoverServerDecoratorFactory.getUrl());

        verify(decorated);

    }

    @Test
    public void getNullUrlError() throws MalformedURLException {
        FailoverServer failoverServer = createMock(FailoverServer.class);
        FailoverServerMaintenanceDecorator failoverServerMaintenanceDecorator = new FailoverServerMaintenanceDecorator(failoverServer);

        FailoverServerDecoratorFactory decorated = createMock(FailoverServerDecoratorFactory.class);

        failoverServerMaintenanceDecorator.decorate(decorated);

        URL testUrl = new URL("http://localhost:8080");

        reset(decorated, failoverServer);

        expect(failoverServer.getTimeToReturn()).andReturn(100000);
        expect(failoverServer.getUrl()).andReturn(null);
        expect(decorated.getUrl()).andReturn(testUrl);

        replay(decorated, failoverServer);

        FailoverServerDecoratorFactory failoverServerDecoratorFactory = failoverServerMaintenanceDecorator.update();
        assertEquals(testUrl, failoverServerDecoratorFactory.getUrl());

        verify(decorated, failoverServer);
    }

    @Test
    public void noopErrorUpdateTest() {
        FailoverServer failoverServer = createMock(FailoverServer.class);
        FailoverServerMaintenanceDecorator failoverServerMaintenanceDecorator = new FailoverServerMaintenanceDecorator(failoverServer);

        FailoverServerDecoratorFactory decorated = createMock(FailoverServerDecoratorFactory.class);

        failoverServerMaintenanceDecorator.decorate(decorated);

        reset(decorated, failoverServer);

        //noop

        replay(decorated, failoverServer);

        failoverServerMaintenanceDecorator.updateError();

        verify(decorated, failoverServer);
    }
}

package org.meteorminer.network.failover;

import org.junit.Test;
import org.meteorminer.output.CLInterface;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.easymock.EasyMock.*;

/**
 * @author John Ericksen
 */
public class FailoverServerSwitcherDecoratorTest {

    private static final String TEST_HOST = "localhost";
    private static final int TEST_PORT_ONE = 80;
    private static final int TEST_PORT_TWO = 8080;

    @Test
    public void lifecycleTest() throws InterruptedException {
        //setup
        FailoverServerDecoratorFactory decorated = createMock(FailoverServerDecoratorFactory.class);
        CLInterface output = createMock(CLInterface.class);

        FailoverServer serverOne = new FailoverServer(TEST_HOST, TEST_PORT_ONE, 0, output);
        FailoverServer serverTwo = new FailoverServer(TEST_HOST, TEST_PORT_TWO, 1, output);

        List<FailoverServer> servers = new ArrayList<FailoverServer>();
        servers.add(serverOne);
        servers.add(serverTwo);

        FailoverServerSwitcherDecorator failoverServerSwitcherDecorator = new FailoverServerSwitcherDecorator(servers);

        failoverServerSwitcherDecorator.decorate(decorated);

        //start
        URL urlOne = failoverServerSwitcherDecorator.getUrl();

        assertEquals(serverOne.getUrl(), urlOne);

        //error occurs, switch to backup server
        failoverServerSwitcherDecorator.updateError();

        URL urlTwo = failoverServerSwitcherDecorator.getUrl();

        assertEquals(serverTwo.getUrl(), urlTwo);

        //server expires, switch to next (original)
        Thread.sleep(1100);
        failoverServerSwitcherDecorator.update();

        URL urlThree = failoverServerSwitcherDecorator.getUrl();

        assertEquals(serverOne.getUrl(), urlThree);
    }

    @Test
    public void nullUrlErrorTest() throws MalformedURLException {
        FailoverServerDecoratorFactory decorated = createMock(FailoverServerDecoratorFactory.class);
        CLInterface output = createMock(CLInterface.class);

        FailoverServer serverOne = createMock(FailoverServer.class);

        FailoverServerSwitcherDecorator failoverServerSwitcherDecorator = new FailoverServerSwitcherDecorator(Collections.singletonList(serverOne));

        failoverServerSwitcherDecorator.decorate(decorated);

        URL testUrl = new URL("http://" + TEST_HOST + ":" + TEST_PORT_ONE);

        reset(decorated, output, serverOne);

        expect(serverOne.getUrl()).andReturn(null);
        expect(decorated.getUrl()).andReturn(testUrl);

        replay(decorated, output, serverOne);

        URL url = failoverServerSwitcherDecorator.getUrl();
        assertEquals(testUrl, url);

        verify(decorated, output, serverOne);
    }
}

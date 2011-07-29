package org.meteorminer.network.failover;

import org.junit.Before;
import org.junit.Test;
import org.meteorminer.config.ServerProvider;
import org.meteorminer.config.advice.MeteorAdvice;
import org.meteorminer.hash.MockAdviceFactory;

import java.net.MalformedURLException;
import java.net.URL;

import static junit.framework.Assert.assertEquals;

/**
 * @author John Ericksen
 */
public class BasicServerFactoryTest {

    private BasicServerFactory serverFactory;
    private URL testUrl;


    @Before
    public void setup() throws MalformedURLException {
        MeteorAdvice meteorAdvice = MockAdviceFactory.getInstance().buildDefaultMeteorAdvice();
        testUrl = meteorAdvice.getServers().get(0).getBitcoinUrl();
        serverFactory = new BasicServerFactory();
        serverFactory.setServerProvider(new ServerProvider(meteorAdvice));
    }

    @Test
    public void urlTest() {
        URL url = serverFactory.getUrl();
        assertEquals(testUrl, url);
    }

    @Test
    public void updateTest() {
        assertEquals(serverFactory, serverFactory.update());
    }

    @Test
    public void decorateTest() {
        assertEquals(serverFactory, serverFactory.decorate(null));
    }

    @Test
    public void updateErrorTest() {
        serverFactory.updateError();
        //noop test
    }
}

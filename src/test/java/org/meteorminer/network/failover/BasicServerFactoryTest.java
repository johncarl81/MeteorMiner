package org.meteorminer.network.failover;

import org.junit.Before;
import org.junit.Test;

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
        testUrl = new URL("http://localhost:80");
        serverFactory = new BasicServerFactory();
        serverFactory.setBitcoind(testUrl);
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

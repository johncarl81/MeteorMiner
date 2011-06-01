package org.meteorminer.config;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import org.meteorminer.MeteorMiner;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * @author John Ericksen
 */
public class MeteorAdviceTest {

    private static final String HOST = "testhost.org";
    private static final String PORT = "8080";
    private static final String USERNAME = "tester";
    private static final String PASSWORD = "pass";
    private static final String WORK_TIMEOUT = "1000";

    @Test
    public void testDomain() throws MalformedURLException, ParseException {
        MeteorAdvice advice = buildAdvice("-host", HOST);

        URL url = advice.getBitcoinUrl();

        assertEquals(HOST, url.getHost());
    }

    @Test
    public void testPort() throws MalformedURLException, ParseException {
        MeteorAdvice advice = buildAdvice("-port", PORT);

        URL url = advice.getBitcoinUrl();

        assertEquals(Integer.parseInt(PORT), url.getPort());
    }

    @Test
    public void testProxy() throws MalformedURLException, ParseException {
        MeteorAdvice advice = buildAdvice("-proxy", HOST + ":" + PORT);

        Proxy proxy = advice.getProxy();

        assertEquals(Integer.parseInt(PORT), ((InetSocketAddress) proxy.address()).getPort());
        assertEquals(HOST, ((InetSocketAddress) proxy.address()).getHostName());
    }

    @Test(expected = MalformedURLException.class)
    public void testMalformedProxy() throws MalformedURLException, ParseException {
        MeteorAdvice advice = buildAdvice("-proxy", USERNAME);
    }

    @Test
    public void testVerbose() throws MalformedURLException, ParseException {
        MeteorAdvice advice = buildAdvice("-v");

        assertTrue(advice.isVerbose());
    }

    @Test
    public void testUsername() throws MalformedURLException, ParseException {
        MeteorAdvice advice = buildAdvice("-user", USERNAME);

        assertEquals(USERNAME, advice.getUsername());
    }

    @Test
    public void testPassword() throws MalformedURLException, ParseException {
        MeteorAdvice advice = buildAdvice("-pass", PASSWORD);

        assertEquals(PASSWORD, advice.getPassword());
    }

    @Test
    public void testWorkTimeout() throws MalformedURLException, ParseException {
        MeteorAdvice advice = buildAdvice("-getwork", WORK_TIMEOUT);

        assertEquals(Long.parseLong(WORK_TIMEOUT) * 1000, advice.getGetWorkTimeout());
    }

    @Test
    public void testHelp() throws MalformedURLException, ParseException {
        MeteorAdvice advice = buildAdvice("-help");

        assertTrue(advice.help());
    }

    private MeteorAdvice buildAdvice(String... args) throws ParseException, MalformedURLException {
        return new MeteorAdvice(
                new PosixParser().parse(MeteorMiner.buildOptions(), args));
    }

}

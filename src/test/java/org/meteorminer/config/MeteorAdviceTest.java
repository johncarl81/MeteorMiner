package org.meteorminer.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import org.meteorminer.MeteorMiner;
import org.meteorminer.config.advice.CommandLineAdviceFactory;
import org.meteorminer.config.advice.MeteorAdvice;
import org.meteorminer.config.module.BootstrapModule;

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

        URL url = advice.getServers().get(0).getBitcoinUrl();

        assertEquals(HOST, url.getHost());
    }

    @Test
    public void testPort() throws MalformedURLException, ParseException {
        MeteorAdvice advice = buildAdvice("-port", PORT);

        URL url = advice.getServers().get(0).getBitcoinUrl();

        assertEquals(Integer.parseInt(PORT), url.getPort());
    }

    @Test
    public void testProxy() throws MalformedURLException, ParseException {
        MeteorAdvice advice = buildAdvice("-proxy", HOST + ":" + PORT);

        Proxy proxy = advice.getServers().get(0).getProxy();

        assertEquals(Integer.parseInt(PORT), ((InetSocketAddress) proxy.address()).getPort());
        assertEquals(HOST, ((InetSocketAddress) proxy.address()).getHostName());
    }

    @Test
    public void testProxyUsername() throws MalformedURLException, ParseException {
        MeteorAdvice advice = buildAdvice("-proxy", HOST + ":" + PORT + ":" + USERNAME);

        Proxy proxy = advice.getServers().get(0).getProxy();

        assertEquals(Integer.parseInt(PORT), ((InetSocketAddress) proxy.address()).getPort());
        assertEquals(HOST, ((InetSocketAddress) proxy.address()).getHostName());
        assertEquals(USERNAME, advice.getServers().get(0).getProxyUsername());
        assertEquals("", advice.getServers().get(0).getProxyPassword());
    }

    @Test
    public void testProxyUsernamePassword() throws MalformedURLException, ParseException {
        MeteorAdvice advice = buildAdvice("-proxy", HOST + ":" + PORT + ":" + USERNAME + ":" + PASSWORD);

        Proxy proxy = advice.getServers().get(0).getProxy();

        assertEquals(Integer.parseInt(PORT), ((InetSocketAddress) proxy.address()).getPort());
        assertEquals(HOST, ((InetSocketAddress) proxy.address()).getHostName());
        assertEquals(USERNAME, advice.getServers().get(0).getProxyUsername());
        assertEquals(PASSWORD, advice.getServers().get(0).getProxyPassword());
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

        assertEquals(USERNAME, advice.getServers().get(0).getUsername());
    }

    @Test
    public void testPassword() throws MalformedURLException, ParseException {
        MeteorAdvice advice = buildAdvice("-pass", PASSWORD);

        assertEquals(PASSWORD, advice.getServers().get(0).getPassword());
    }

    @Test
    public void testWorkTimeout() throws MalformedURLException, ParseException {
        MeteorAdvice advice = buildAdvice("-getwork", WORK_TIMEOUT);

        assertEquals(Long.parseLong(WORK_TIMEOUT) * 1000, advice.getServers().get(0).getGetWorkTimeout());
    }

    private MeteorAdvice buildAdvice(String... args) throws ParseException, MalformedURLException {

        Injector bootstrapInjector = Guice.createInjector(new BootstrapModule());

        CommandLineAdviceFactory adviceFactory = bootstrapInjector.getInstance(CommandLineAdviceFactory.class);

        return adviceFactory.buildAdvice(new PosixParser().parse(MeteorMiner.buildOptions(), args));
    }

}

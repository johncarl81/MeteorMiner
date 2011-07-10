package org.meteorminer.network.failover;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;
import org.meteorminer.output.CLInterface;
import org.meteorminer.service.URLFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.easymock.EasyMock.createMock;

/**
 * @author John Ericksen
 */
public class FailoverServerFactoryTest {

    private static final String TEST_HOST = "localhost";
    private static final int TEST_PORT_ONE = 80;
    private static final int TEST_PORT_TWO = 8080;

    private static final String HOST_LIST =
            "[{\"host\":\"" + TEST_HOST + "\",\"port\":" + TEST_PORT_ONE + ",\"ttr\":0}," +
                    "{\"host\":\"" + TEST_HOST + "\",\"port\":" + TEST_PORT_TWO + ",\"ttr\":10}]";
    private static final String HOST_SINGLE =
            "{\"host\":\"" + TEST_HOST + "\",\"port\":" + TEST_PORT_ONE + ",\"ttr\":0}";

    private FailoverServerFactory serverFactory;
    private Gson gson;
    private CLInterface output;
    private URL urlOne;
    private URL urlTwo;

    @Before
    public void setup() throws MalformedURLException {

        output = createMock(CLInterface.class);
        gson = new GsonBuilder().create();
        URLFactory urlFactory = new URLFactory();

        serverFactory = new FailoverServerFactory();

        serverFactory.setGson(gson);
        serverFactory.setOutput(output);
        serverFactory.setUrlFactory(urlFactory);

        urlOne = urlFactory.buildUrl(TEST_HOST, TEST_PORT_ONE);
        urlTwo = urlFactory.buildUrl(TEST_HOST, TEST_PORT_TWO);
    }

    @Test
    public void buildFailoverListTest() {
        List<FailoverServer> failoverServers = serverFactory.buildFailoverServers(HOST_LIST);

        assertEquals(urlOne, failoverServers.get(0).getUrl());
        assertEquals(urlTwo, failoverServers.get(1).getUrl());
    }

    @Test
    public void buildFailoverSingleTest() {
        FailoverServer failoverServer = serverFactory.buildFailoverServer(HOST_SINGLE);

        assertEquals(urlOne, failoverServer.getUrl());
    }
}

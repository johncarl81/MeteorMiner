package org.meteorminer.network.failover;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.meteorminer.output.CLInterface;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.easymock.EasyMock.*;

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
            "{\"host\":\"" + TEST_HOST + "\",\"port\":" + TEST_PORT_ONE + ",\"ttr\":0},";

    private FailoverServerFactory serverFactory;
    private ObjectMapper mapper;
    private CLInterface output;
    private URL urlOne;
    private URL urlTwo;
    private IOException ioException;

    @Before
    public void setup() throws MalformedURLException {

        output = createMock(CLInterface.class);
        mapper = new ObjectMapper();

        serverFactory = new FailoverServerFactory();

        serverFactory.setMapper(mapper);
        serverFactory.setOutput(output);

        urlOne = new URL("http://" + TEST_HOST + ":" + TEST_PORT_ONE);
        urlTwo = new URL("http://" + TEST_HOST + ":" + TEST_PORT_TWO);

        ioException = new IOException("test exception");
    }

    public void setupMockMapper() {
        mapper = createMock(ObjectMapper.class);
        serverFactory.setMapper(mapper);
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

    @Test
    public void buildFailoverListErrorTest() throws IOException {
        setupMockMapper();

        reset(output, mapper);

        expect(mapper.readTree(eq(HOST_LIST))).andThrow(ioException);
        output.error(eq(ioException));

        replay(output, mapper);

        List<FailoverServer> servers = serverFactory.buildFailoverServers(HOST_LIST);

        assertEquals(0, servers.size());

        verify(output, mapper);
    }

    @Test
    public void buildFailoverSingleErrorTest() throws IOException {
        setupMockMapper();

        reset(output, mapper);

        expect(mapper.readTree(eq(HOST_SINGLE))).andThrow(ioException);
        output.error(eq(ioException));

        replay(output, mapper);

        FailoverServer server = serverFactory.buildFailoverServer(HOST_SINGLE);

        assertNull(server);

        verify(output, mapper);
    }
}

package org.meteorminer.network;

import org.codehaus.jackson.JsonNode;
import org.junit.Before;
import org.junit.Test;
import org.meteorminer.domain.Work;
import org.meteorminer.domain.WorkFactory;
import org.meteorminer.hash.MinerController;
import org.meteorminer.output.CLInterface;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.easymock.EasyMock.*;

/**
 * @author John Ericksen
 */
public class LongPollWorkerTest {

    private LongPollWorker longPollWorker;

    private URL longPollWorkerUrl;
    private JsonClient jsonClient;
    private String getWorkRequest;
    private WorkFactory workFactory;
    private MinerController minerController;
    private CLInterface output;
    private LongPollWorkProducer longPollFactory;
    private Work work;
    private JsonNode jsonNode;

    @Before
    public void setup() throws MalformedURLException {

        longPollWorkerUrl = new URL("http://localhost");
        getWorkRequest = "GetWork";
        jsonClient = createMock(JsonClient.class);
        longPollFactory = new LongPollWorkProducer();

        workFactory = createMock(WorkFactory.class);
        minerController = createMock(MinerController.class);
        output = createMock(CLInterface.class);
        work = createMock(Work.class);
        jsonNode = createMock(JsonNode.class);

        longPollWorker = new LongPollWorker(longPollWorkerUrl, jsonClient, getWorkRequest, workFactory, minerController, output, longPollFactory);
    }

    @Test
    public void testExecution() throws IOException {
        reset(jsonClient, workFactory, minerController, output);

        expect(jsonClient.execute(anyObject(String.class), eq(getWorkRequest), anyObject(URL.class))).andReturn(jsonNode);
        output.notification(anyObject(String.class));
        expectLastCall().anyTimes();

        expect(workFactory.buildWork(jsonNode)).andReturn(work);

        minerController.interruptProduction();

        replay(jsonClient, workFactory, minerController, output);

        longPollWorker.setRunning(false); // will only execute once
        longPollWorker.run();

        assertTrue(longPollFactory.hasWork());
        assertEquals(work, longPollFactory.produce());

        verify(jsonClient, workFactory, minerController, output);
    }

    //todo: test jsonclient throwing exception

}

package org.meteorminer.network;

import org.codehaus.jackson.JsonNode;
import org.junit.Before;
import org.junit.Test;
import org.meteorminer.domain.Work;
import org.meteorminer.domain.WorkFactory;
import org.meteorminer.hash.HashScanner;
import org.meteorminer.hash.WorkConsumer;
import org.meteorminer.network.longpoll.LongPollWorkProducer;
import org.meteorminer.network.longpoll.LongPollWorker;
import org.meteorminer.output.CLInterface;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
    private HashScanner hashScanner;
    private CLInterface output;
    private LongPollWorkProducer longPollFactory;
    private Work work;
    private JsonNode jsonNode;
    private WorkConsumer workSource;

    @Before
    public void setup() throws MalformedURLException {

        longPollWorkerUrl = new URL("http://localhost");
        getWorkRequest = "GetWork";
        jsonClient = createMock(JsonClient.class);
        longPollFactory = new LongPollWorkProducer();

        workFactory = createMock(WorkFactory.class);
        hashScanner = createMock(HashScanner.class);
        output = createMock(CLInterface.class);
        work = createMock(Work.class);
        jsonNode = createMock(JsonNode.class);
        workSource = createMock(WorkConsumer.class);

        longPollWorker = new LongPollWorker(longPollWorkerUrl, jsonClient, getWorkRequest, workFactory, output, workSource, 10);
    }

    @Test
    public void testExecution() throws IOException {
        reset(jsonClient, workFactory, output, hashScanner);

        expect(jsonClient.execute(anyObject(String.class), eq(getWorkRequest), anyObject(URL.class))).andReturn(jsonNode);
        output.notification(anyObject(String.class));
        expectLastCall().anyTimes();

        expect(workFactory.buildWork(jsonNode)).andReturn(work);
        workSource.pushWork(work);

        replay(jsonClient, workFactory, output, hashScanner);

        longPollWorker.setRunning(false); // will only execute once
        longPollWorker.run();

        verify(jsonClient, workFactory, output, hashScanner);
    }

    //todo: test jsonclient throwing exception

}

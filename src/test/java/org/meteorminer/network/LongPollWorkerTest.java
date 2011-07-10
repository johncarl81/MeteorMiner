package org.meteorminer.network;

import org.junit.Before;
import org.junit.Test;
import org.meteorminer.domain.Work;
import org.meteorminer.domain.WorkFactory;
import org.meteorminer.hash.HashScanner;
import org.meteorminer.hash.WorkConsumer;
import org.meteorminer.network.longpoll.LongPollMessageStrategy;
import org.meteorminer.network.longpoll.LongPollWorker;
import org.meteorminer.output.CLInterface;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static org.easymock.EasyMock.*;

/**
 * @author John Ericksen
 */
public class LongPollWorkerTest {

    private LongPollWorker longPollWorker;

    private LongPollMessageStrategy longPollMessageStrategy;
    private URL longPollWorkerUrl;
    private JsonClient jsonClient;
    private String getWorkRequest;
    private WorkFactory workFactory;
    private HashScanner hashScanner;
    private CLInterface output;
    private Work work;
    private InputStream inputStream;
    private WorkConsumer workSource;

    @Before
    public void setup() throws MalformedURLException {

        longPollWorkerUrl = new URL("http://localhost");
        getWorkRequest = "GetWork";
        jsonClient = createMock(JsonClient.class);

        workFactory = createMock(WorkFactory.class);
        hashScanner = createMock(HashScanner.class);
        output = createMock(CLInterface.class);
        work = createMock(Work.class);
        inputStream = createMock(InputStream.class);
        workSource = createMock(WorkConsumer.class);
        longPollMessageStrategy = new LongPollMessageStrategy();

        longPollWorker = new LongPollWorker(longPollWorkerUrl, jsonClient, output, workSource, 10, longPollMessageStrategy);
    }

    @Test
    public void testExecution() throws IOException {
        reset(jsonClient, workFactory, output, hashScanner, inputStream);

        expect(jsonClient.execute(anyObject(URL.class), eq(longPollMessageStrategy))).andReturn(work);
        output.notification(anyObject(String.class));
        expectLastCall().anyTimes();

        workSource.pushWork(work);

        replay(jsonClient, workFactory, output, hashScanner, inputStream);

        longPollWorker.setRunning(false); // will only execute once
        longPollWorker.run();

        verify(jsonClient, workFactory, output, hashScanner, inputStream);
    }

    //todo: test jsonclient throwing exception

}

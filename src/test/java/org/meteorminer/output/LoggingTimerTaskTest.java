package org.meteorminer.output;

import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;

/**
 * @author John Ericksen
 */
public class LoggingTimerTaskTest {

    private LoggingTimerTask loggingTimerTask;
    private StatisticsHolder statisticsHolder;
    private CLInterface output;

    @Before
    public void setup() {

        statisticsHolder = createMock(StatisticsHolder.class);
        output = createMock(CLInterface.class);

        loggingTimerTask = new LoggingTimerTask();
        loggingTimerTask.setOutput(output);
        loggingTimerTask.setStatisticsHolder(statisticsHolder);
    }

    @Test
    public void runTest() {

        reset(statisticsHolder, output);

        statisticsHolder.updateInstants();
        output.outputMain();

        replay(statisticsHolder, output);

        loggingTimerTask.run();

        verify(statisticsHolder, output);
    }
}

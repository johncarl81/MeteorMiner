package org.meteorminer.hash;

import org.junit.Before;
import org.junit.Test;
import org.meteorminer.output.Statistics;

import static org.easymock.EasyMock.*;

/**
 * @author John Ericksen
 */
public class HashStatisticsOutputTimerTaskTest {

    private HashStatisticsOutputTimerTask timerTask;
    private HashScanner hashScanner;
    private Statistics statistics;

    @Before
    public void setup() {

        hashScanner = createMock(HashScanner.class);
        statistics = createMock(Statistics.class);

        timerTask = new HashStatisticsOutputTimerTask(hashScanner, statistics);
    }

    @Test
    public void runTest() {

        long testNonceCount = 10;
        long testNonceCount2 = 20;

        reset(hashScanner, statistics);

        expect(hashScanner.getNonceCount()).andReturn(testNonceCount);
        statistics.incrementHashCount(testNonceCount);

        expect(hashScanner.getNonceCount()).andReturn(testNonceCount2);
        statistics.incrementHashCount(testNonceCount2 - testNonceCount);

        replay(hashScanner, statistics);

        timerTask.run();
        timerTask.run();

        verify(hashScanner, statistics);
    }
}

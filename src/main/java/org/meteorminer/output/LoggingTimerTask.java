package org.meteorminer.output;

import javax.inject.Inject;
import java.util.TimerTask;

/**
 * @author John Ericksen
 */
public class LoggingTimerTask extends TimerTask {
    @Inject
    private StatisticsHolder statisticsHolder;
    @Inject
    private CLInterface output;

    @Override
    public void run() {
        statisticsHolder.updateInstants();
        output.outputMain();
    }

    public void setStatisticsHolder(StatisticsHolder statisticsHolder) {
        this.statisticsHolder = statisticsHolder;
    }

    public void setOutput(CLInterface output) {
        this.output = output;
    }
}

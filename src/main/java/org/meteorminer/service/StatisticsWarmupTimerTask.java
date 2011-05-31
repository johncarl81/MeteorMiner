package org.meteorminer.service;

import org.meteorminer.output.StatisticsHolder;

import javax.inject.Inject;
import java.util.TimerTask;

/**
 * @author John Ericksen
 */
public class StatisticsWarmupTimerTask extends TimerTask {

    @Inject
    private StatisticsHolder statisticsHolder;

    @Override
    public void run() {
        statisticsHolder.reset();
    }
}

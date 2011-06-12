package org.meteorminer.output;


import com.google.inject.Singleton;
import org.meteorminer.domain.Device;

import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
@Singleton
public class StatisticsHolder {

    private Map<Device, Statistics> statisticsMap = new HashMap<Device, Statistics>();

    public Map<Device, Statistics> getStatistics() {
        return statisticsMap;
    }

    public void updateInstants() {
        for (Statistics statistics : statisticsMap.values()) {
            statistics.updateInstants();
        }
    }

    public void reset() {
        for (Statistics statistics : statisticsMap.values()) {
            statistics.reset();
        }
    }

    public Statistics getStatisticsSum() {
        Statistics sampleStatistics = statisticsMap.values().iterator().next();

        long startTime = sampleStatistics.getStartTime();
        Statistics statSum = new Statistics(startTime);

        for (Statistics statistics : statisticsMap.values()) {
            statSum.add(statistics);
        }

        return statSum;
    }

    public String toString() {
        Statistics statistics = getStatisticsSum();
        return new Formatter().format("%1.2fmh/s %1d pass %1d fail",
                statistics.getHashRate(), statistics.getWorkPassed(), statistics.getWorkFailed()).toString();
    }


}

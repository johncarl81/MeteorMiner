package org.meteorminer.output;


import com.google.inject.Singleton;
import org.meteorminer.domain.Device;

import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
@Singleton
public class StatisticsHolder {

    private Map<Device, Statistics> statisticsList = new HashMap<Device, Statistics>();

    public Map<Device, Statistics> getStatistics() {
        return statisticsList;
    }
}

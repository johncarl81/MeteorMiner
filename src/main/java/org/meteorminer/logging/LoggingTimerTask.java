package org.meteorminer.logging;

import javax.inject.Inject;
import java.util.TimerTask;

/**
 * @author John Ericksen
 */
public class LoggingTimerTask extends TimerTask {
    @Inject
    private CLLogger logger;

    @Override
    public void run() {
        logger.outputMain();
    }
}

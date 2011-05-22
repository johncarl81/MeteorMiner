package org.meteorminer.logging;

import javax.inject.Inject;
import java.util.TimerTask;

/**
 * @author John Ericksen
 */
public class LoggingTimerTask extends TimerTask {
    @Inject
    private CLInterface output;

    @Override
    public void run() {
        output.outputMain();
    }
}

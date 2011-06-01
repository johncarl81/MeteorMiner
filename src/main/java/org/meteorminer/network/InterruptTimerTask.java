package org.meteorminer.network;

import org.meteorminer.hash.WorkConsumer;
import org.meteorminer.output.CLInterface;

import javax.inject.Inject;
import java.util.TimerTask;

/**
 * @author John Ericksen
 */
public class InterruptTimerTask extends TimerTask {

    @Inject
    private WorkConsumer workSource;
    @Inject
    private CLInterface output;

    @Override
    public void run() {
        //request new work..
        workSource.updateWork();

        output.verbose("Interrupt Timer Task produced work");
    }
}

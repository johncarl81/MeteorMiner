package org.meteorminer.hash;

import java.util.TimerTask;

/**
 * @author John Ericksen
 */
public class InteruptTimerTask extends TimerTask {

    private LocalMinerController localController;

    public InteruptTimerTask(LocalMinerController localController) {
        this.localController = localController;
    }

    @Override
    public void run() {
        localController.stopProduction();
    }
}

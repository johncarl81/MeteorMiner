package org.meteorminer.output;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author John Ericksen
 */
@Singleton
public class ShutdownDetection extends Thread {

    @Inject
    private CLInterface output;

    @Override
    public void run() {
        output.notification("Shutdown Initiated");
    }
}

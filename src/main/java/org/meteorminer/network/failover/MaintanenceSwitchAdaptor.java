package org.meteorminer.network.failover;

import org.meteorminer.network.BitcoinUrlFactory;
import org.meteorminer.output.CLInterface;

import javax.inject.Inject;
import java.net.HttpURLConnection;

/**
 * @author John Ericksen
 */
public class MaintanenceSwitchAdaptor {

    @Inject
    private FailoverServerFactory failoverServerFactory;
    @Inject
    private BitcoinUrlFactory bitcoinUrlFactory;
    @Inject
    private CLInterface output;

    public void setupMaintanenceSwitch(HttpURLConnection connection) {
        String switchTo = connection.getHeaderField("X-Switch-To");

        if (switchTo != null) {

            FailoverServer failoverServer = failoverServerFactory.buildFailoverServer(switchTo);

            if (failoverServer != null) {
                bitcoinUrlFactory.pushFailoverDecoratorFactory(new FailoverServerMaintenanceDecorator(failoverServer));
                output.notification("Maintenance server provided: " + failoverServer.getUrl());
            }
        }
    }
}

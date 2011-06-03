package org.meteorminer.network.failover;

import com.google.inject.Inject;
import org.meteorminer.network.BitcoinUrlFactory;
import org.meteorminer.output.CLInterface;

import java.net.HttpURLConnection;
import java.util.List;

/**
 * @author John Ericksen
 */
public class HostFailoverAdaptor {

    private static final String X_HOST_LIST_FIELD = "X-Host-List";

    @Inject
    private BitcoinUrlFactory bitcoinUrlFactory;
    @Inject
    private FailoverServerFactory failoverServerFactory;
    @Inject
    private CLInterface output;

    private boolean hostsDefined = false;

    public void setupFailover(HttpURLConnection connection) {
        if (!hostsDefined) {
            String hostList = connection.getHeaderField(X_HOST_LIST_FIELD);
            if (hostList != null) {

                List<FailoverServer> servers = failoverServerFactory.buildFailoverServers(hostList);

                if (servers != null && !servers.isEmpty()) {
                    bitcoinUrlFactory.pushFailoverDecoratorFactory(new FailoverServerSwitcherDecorator(servers));
                    output.notification("Failover Servers Extension: Enabled, Provided: ", servers);
                    hostsDefined = true;
                }
            }
        }
    }
}

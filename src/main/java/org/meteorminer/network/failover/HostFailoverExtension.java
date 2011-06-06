package org.meteorminer.network.failover;

import com.google.inject.Inject;
import org.meteorminer.network.BitcoinUrlFactory;
import org.meteorminer.network.RPCExtension;
import org.meteorminer.output.CLInterface;

import java.net.HttpURLConnection;
import java.util.List;

/**
 * @author John Ericksen
 */
public class HostFailoverExtension implements RPCExtension {

    private static final String X_HOST_LIST_FIELD = "X-Host-List";

    @Inject
    private BitcoinUrlFactory bitcoinUrlFactory;
    @Inject
    private FailoverServerFactory failoverServerFactory;
    @Inject
    private CLInterface output;

    private boolean hostsDefined = false;

    @Override
    public void setup(HttpURLConnection connection) {
        if (!hostsDefined) {
            String hostList = connection.getHeaderField(X_HOST_LIST_FIELD);
            if (hostList != null) {

                List<FailoverServer> servers = failoverServerFactory.buildFailoverServers(hostList);

                if (servers != null && !servers.isEmpty()) {
                    bitcoinUrlFactory.pushFailoverDecoratorFactory(new FailoverServerSwitcherDecorator(servers));
                    output.notification("Failover Servers Extension: Enabled");
                    for (FailoverServer failoverServer : servers) {
                        output.verbose("Added Server: " + failoverServer.toString());
                    }
                    hostsDefined = true;
                }
            }
        }
    }
}

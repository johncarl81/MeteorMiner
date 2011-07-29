package org.meteorminer.network.failover;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import org.meteorminer.output.CLInterface;
import org.meteorminer.service.URLFactory;

import javax.inject.Singleton;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author John Ericksen
 */
@Singleton
public class FailoverServerFactory {

    @Inject
    private Gson gson;
    @Inject
    private CLInterface output;
    @Inject
    private URLFactory urlFactory;

    public List<FailoverServer> buildFailoverServers(String hostList) {

        Type collectionType = new TypeToken<List<FailoverServer>>() {
        }.getType();

        List<FailoverServer> servers = gson.fromJson(hostList, collectionType);

        for (FailoverServer server : servers) {
            addServerParameters(server);
        }

        return servers;
    }

    private FailoverServer addServerParameters(FailoverServer server) {
        if (server != null) {
            server.setOutput(output);
            server.setUrlFactory(urlFactory);
        }

        return server;
    }

    public FailoverServer buildFailoverServer(String server) {
        return addServerParameters(gson.fromJson(server, FailoverServer.class));
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public void setOutput(CLInterface output) {
        this.output = output;
    }

    public void setUrlFactory(URLFactory urlFactory) {
        this.urlFactory = urlFactory;
    }
}

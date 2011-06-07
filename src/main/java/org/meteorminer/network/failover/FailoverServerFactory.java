package org.meteorminer.network.failover;

import com.google.inject.Inject;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.meteorminer.output.CLInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author John Ericksen
 */
public class FailoverServerFactory {

    @Inject
    private ObjectMapper mapper;
    @Inject
    private CLInterface output;

    public List<FailoverServer> buildFailoverServers(String hostList) {

        List<FailoverServer> servers = new ArrayList<FailoverServer>();

        try {
            ArrayNode nodes = (ArrayNode) mapper.readTree(hostList);

            for (JsonNode node : nodes) {
                FailoverServer server = buildFailoverServer((ObjectNode) node);
                if (server != null) {
                    servers.add(server);
                }
            }

        } catch (IOException e) {
            output.error(e);
            return Collections.emptyList();
        }

        return servers;
    }

    public FailoverServer buildFailoverServer(String server) {
        try {
            return buildFailoverServer((ObjectNode) mapper.readTree(server));
        } catch (IOException e) {
            output.error(e);
            return null;
        }
    }

    private FailoverServer buildFailoverServer(ObjectNode node) {

        FailoverServer server = null;
        if (node.has("host") &&
                node.has("port") &&
                node.has("ttr")) {
            server = new FailoverServer(node.get("host").getTextValue(),
                    node.get("port").getIntValue(),
                    node.get("ttr").getIntValue(), output);
        }

        return server;
    }

    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void setOutput(CLInterface output) {
        this.output = output;
    }
}

package org.meteorminer.queue;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.meteorminer.domain.Work;
import org.meteorminer.hash.HashCacheScanner;
import org.meteorminer.logging.CLLogger;
import org.meteorminer.logging.Statistics;
import org.meteorminer.network.JsonClient;

import java.io.IOException;

/**
 * @author John Ericksen
 */
public class WorkSubmit implements Runnable {

    private Work work;
    private JsonClient jsonClient;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private Statistics stats;
    private HashCacheScanner hashCache;
    private int nonce;
    private CLLogger logger;

    public WorkSubmit(Work work, JsonClient jsonClient, Statistics stats, HashCacheScanner hashCache, int nonce, CLLogger logger) {
        this.work = work;
        this.jsonClient = jsonClient;
        this.stats = stats;
        this.hashCache = hashCache;
        this.nonce = nonce;
        this.logger = logger;
    }

    public void run() {
        try {
            logger.notification("Work Passed, found after:" + work.getFoundAge());

            boolean success = parseJsonResult(jsonClient.execute(buildSubmitMessage(work)));

            if (success) {
                logger.notification("Submitted");
                stats.incrementWorkPass(1);
                hashCache.add(work, nonce);
            } else {
                logger.notification("Rejected");
                stats.incrementWorkFail(1);
            }

        } catch (IOException e) {
            logger.notification("Exception while submitting the following work:");
            logger.notification(work.getDataString());
            e.printStackTrace();
        }
    }

    private boolean parseJsonResult(JsonNode node) {
        return node.getBooleanValue();
    }

    private String buildSubmitMessage(Work work) {
        ObjectNode sendworkMessage = MAPPER.createObjectNode();
        sendworkMessage.put("method", "getwork");
        ArrayNode params = sendworkMessage.putArray("params");
        params.add(encodeBlock(work));
        sendworkMessage.put("id", 1);

        return sendworkMessage.toString();
    }

    private String encodeBlock(Work work) {
        StringBuilder builder = new StringBuilder();

        for (int d : work.getData())
            builder.append(String.format("%08x", Integer.reverseBytes(d)));

        return builder.toString();
    }
}

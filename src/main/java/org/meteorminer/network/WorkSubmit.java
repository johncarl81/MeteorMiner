package org.meteorminer.network;

import com.google.inject.assistedinject.Assisted;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.meteorminer.domain.Work;
import org.meteorminer.hash.WorkConsumer;
import org.meteorminer.output.CLInterface;
import org.meteorminer.output.Statistics;

import javax.inject.Inject;
import java.io.IOException;

/**
 * @author John Ericksen
 */
public class WorkSubmit implements Runnable {

    private Work work;
    private JsonClient jsonClient;
    private ObjectMapper mapper;
    private Statistics stats;
    private int nonce;
    private CLInterface output;
    private WorkConsumer workSource;

    @Inject
    public WorkSubmit(@Assisted Work work, @Assisted int nonce,
                      JsonClient jsonClient, Statistics stats,
                      CLInterface output, WorkConsumer workSource, ObjectMapper mapper) {
        this.work = work;
        this.jsonClient = jsonClient;
        this.stats = stats;
        this.nonce = nonce;
        this.output = output;
        this.workSource = workSource;
        this.mapper = mapper;
    }

    public void run() {
        try {
            output.verbose("Work passed local verification.  Proceeding to submit.");

            String submitMessage = buildSubmitMessage(work);
            //last chance to check for stale work
            if (!work.isStale()) {
                boolean success = parseJsonResult(jsonClient.execute("SendWork", submitMessage));

                if (success) {
                    output.notification("Hash Submitted: %08x", nonce);
                    stats.incrementWorkPass(1);
                    workSource.updateWork();

                } else {
                    output.notification("Hash Rejected: %08x", nonce);
                    stats.incrementWorkFail(1);
                }
            }

        } catch (IOException e) {
            output.notification("Exception while submitting the following work:");
            output.notification(work.toString());
            output.error(e);
        }
    }

    private boolean parseJsonResult(JsonNode node) {
        return node.getBooleanValue();
    }

    private String buildSubmitMessage(Work work) {
        ObjectNode sendworkMessage = mapper.createObjectNode();
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

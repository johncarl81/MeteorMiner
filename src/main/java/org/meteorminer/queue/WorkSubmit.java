package org.meteorminer.queue;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.meteorminer.JsonClient;
import org.meteorminer.Work;

import java.io.IOException;

/**
 * @author John Ericksen
 */
public class WorkSubmit implements Runnable {

    private Work work;
    private JsonClient jsonClient;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public WorkSubmit(Work work, JsonClient jsonClient) {
        this.work = work;
        this.jsonClient = jsonClient;
    }

    public void run() {
        try {
            System.out.println("Work Passed");

            boolean success = parseJsonResult(jsonClient.execute(buildSubmitMessage(work)));

            if (success) {
                System.out.println("Submitted");
            } else {
                System.out.println("Rejected");
            }

        } catch (IOException e) {
            System.out.println("Exception while submitting the following work:");
            System.out.println(work.getDataString());
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

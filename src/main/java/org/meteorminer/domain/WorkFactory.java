package org.meteorminer.domain;

import org.codehaus.jackson.JsonNode;

import javax.inject.Singleton;

/**
 * @author John Ericksen
 */
@Singleton
public class WorkFactory {

    public Work buildWork(JsonNode responseNode) {
        Work work = null;

        if (responseNode.has("midstate") &&
                responseNode.has("data") &&
                responseNode.has("hash1") &&
                responseNode.has("target")) {
            work = new Work(responseNode.get("data").getValueAsText(),
                    responseNode.get("midstate").getValueAsText(),
                    responseNode.get("hash1").getValueAsText(),
                    responseNode.get("target").getValueAsText());
        }

        return work;
    }
}

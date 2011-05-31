package org.meteorminer.domain;

import org.codehaus.jackson.JsonNode;
import org.meteorminer.output.CLInterface;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Factory building the Work object
 *
 * @author John Ericksen
 */
@Singleton
public class WorkFactory {

    @Inject
    private CLInterface output;

    /**
     * Build a work object from the given JSON response node from the GetWork request.
     *
     * @param responseNode
     * @return Work
     */
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
            output.verbose("Work built: " + work.getDataString());
        }

        return work;
    }
}

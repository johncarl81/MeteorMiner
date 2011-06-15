package org.meteorminer.domain;

import org.codehaus.jackson.JsonNode;
import org.meteorminer.hash.PreProcessWorkFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

/**
 * Factory building the Work object
 *
 * @author John Ericksen
 */
@Singleton
public class WorkFactory {

    @Inject
    private Set<PreProcessWorkFactory> preProcessWorkFactories;

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
            return buildWork(responseNode.get("data").getValueAsText(),
                    responseNode.get("midstate").getValueAsText(),
                    responseNode.get("hash1").getValueAsText(),
                    responseNode.get("target").getValueAsText());
        }

        return work;
    }

    public Work buildWork(String midstate, String data, String hash1, String target) {
        Work work = new Work(midstate, data, hash1, target);
        //preprocessing
        for (PreProcessWorkFactory preProcessWorkFactory : preProcessWorkFactories) {
            work.getPreProcessedWork().put(preProcessWorkFactory.getPreProcessWorkName(),
                    preProcessWorkFactory.buildPreProcessWork(work));
        }

        return work;
    }

    public void setPreProcessWorkFactories(Set<PreProcessWorkFactory> preProcessWorkFactories) {
        this.preProcessWorkFactories = preProcessWorkFactories;
    }
}

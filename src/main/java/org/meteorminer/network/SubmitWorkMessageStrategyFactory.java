package org.meteorminer.network;

import com.google.gson.Gson;
import org.meteorminer.domain.Work;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class SubmitWorkMessageStrategyFactory {

    @Inject
    private Gson gson;
    @Inject
    private SubmitWorkResponseFactory submitWorkResponseFactory;

    public SubmitWorkMessageStrategy buildSubmitWorkStrategy(Work work) {
        return new SubmitWorkMessageStrategy(buildSubmitMessage(work), submitWorkResponseFactory);
    }

    private String buildSubmitMessage(Work work) {

        SubmitWorkMessage message = new SubmitWorkMessage(work);

        return gson.toJson(message);
    }
}

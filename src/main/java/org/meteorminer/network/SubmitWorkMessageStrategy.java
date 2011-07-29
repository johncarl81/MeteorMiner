package org.meteorminer.network;

/**
 * @author John Ericksen
 */
public class SubmitWorkMessageStrategy implements MessageStrategy<SubmitWorkResponse> {

    private static final String REQUEST_TYPE = "SubmitWork";

    private String requestMessage;
    private SubmitWorkResponseFactory submitWorkResponseFactory;

    public SubmitWorkMessageStrategy(String requestMessage, SubmitWorkResponseFactory submitWorkResponseFactory) {
        this.requestMessage = requestMessage;
        this.submitWorkResponseFactory = submitWorkResponseFactory;
    }

    @Override
    public String getRequestType() {
        return REQUEST_TYPE;
    }

    @Override
    public String getRequestMessage() {
        return requestMessage;
    }

    @Override
    public ResponseFactory<SubmitWorkResponse> getResponseFactory() {
        return submitWorkResponseFactory;
    }
}

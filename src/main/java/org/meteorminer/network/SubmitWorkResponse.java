package org.meteorminer.network;

/**
 * @author John Ericksen
 */
public class SubmitWorkResponse {

    //{"result":true,"error":null,"id":"1"}
    private boolean result;
    private String error;
    private long id;

    public SubmitWorkResponse() {
        //empty bean constructor
    }

    public SubmitWorkResponse(boolean result, String error, long id) {
        this.result = result;
        this.error = error;
        this.id = id;
    }

    public boolean isResult() {
        return result;
    }

    public String getError() {
        return error;
    }

    public long getId() {
        return id;
    }
}

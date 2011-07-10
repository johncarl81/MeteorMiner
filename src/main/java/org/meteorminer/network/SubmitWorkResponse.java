package org.meteorminer.network;

/**
 * @author John Ericksen
 */
public class SubmitWorkResponse {

    //{"result":true,"error":null,"id":"1"}
    private boolean result;
    private String error;
    private long id;

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

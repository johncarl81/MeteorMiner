package org.meteorminer.domain;

/**
 * @author John Ericksen
 */
public class GetWorkResponse {

    private Work result;
    private String error;
    private long id;

    public Work getResult() {
        return result;
    }

    public String getError() {
        return error;
    }

    public long getId() {
        return id;
    }
}

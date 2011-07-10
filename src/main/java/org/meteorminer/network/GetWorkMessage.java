package org.meteorminer.network;

import java.util.ArrayList;
import java.util.List;

/**
 * @author John Ericksen
 */
public class GetWorkMessage {

    private String method = "getwork";
    private List<String> params = new ArrayList<String>();
    private long id = 1;

    public String getMethod() {
        return method;
    }

    public List<String> getParams() {
        return params;
    }

    public long getId() {
        return id;
    }
}

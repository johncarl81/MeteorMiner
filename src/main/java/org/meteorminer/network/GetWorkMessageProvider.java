package org.meteorminer.network;


import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class GetWorkMessageProvider implements Provider<String> {

    @Inject
    private Gson gson;

    @Override
    public String get() {
        return gson.toJson(new GetWorkMessage());
    }
}

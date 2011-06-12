package org.meteorminer.config.factory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Provider;

import javax.inject.Inject;

/**
 * Basic extension to the GsonBuilder to provide the Gson class.
 *
 * @author John Ericksen
 */
public class GsonProvider implements Provider<Gson> {

    @Inject
    private GsonBuilder gsonBuilder;

    @Override
    public Gson get() {
        return gsonBuilder.create();
    }
}

package org.meteorminer.config.factory;

import com.google.gson.GsonBuilder;

import javax.inject.Provider;

/**
 * GSON Builder factory.  Place to configure all type based factories
 *
 * @author John Ericksen
 */
public class GsonBuilderConfiguration implements Provider<GsonBuilder> {
    @Override
    public GsonBuilder get() {
        return new GsonBuilder();
    }
}

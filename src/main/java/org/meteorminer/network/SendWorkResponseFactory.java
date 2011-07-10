package org.meteorminer.network;

import com.google.gson.Gson;

import javax.inject.Inject;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author John Ericksen
 */
public class SendWorkResponseFactory implements ResponseFactory<Boolean> {

    @Inject
    private Gson gson;

    @Override
    public Boolean buildResponse(InputStream inputStream) {
        return gson.fromJson(new InputStreamReader(inputStream), Boolean.class);
    }
}

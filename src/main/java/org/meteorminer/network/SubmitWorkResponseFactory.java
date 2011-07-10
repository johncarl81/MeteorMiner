package org.meteorminer.network;

import com.google.gson.Gson;

import javax.inject.Inject;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author John Ericksen
 */
class SubmitWorkResponseFactory implements ResponseFactory<SubmitWorkResponse> {

    @Inject
    private Gson gson;

    @Override
    public SubmitWorkResponse buildResponse(InputStream inputStream) {
        return gson.fromJson(new InputStreamReader(inputStream), SubmitWorkResponse.class);
    }
}

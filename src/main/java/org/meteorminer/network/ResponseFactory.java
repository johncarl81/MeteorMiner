package org.meteorminer.network;

import java.io.InputStream;

/**
 * @author John Ericksen
 */
public interface ResponseFactory<T> {

    T buildResponse(InputStream inputStream);
}

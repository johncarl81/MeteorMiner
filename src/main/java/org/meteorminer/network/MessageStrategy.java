package org.meteorminer.network;

/**
 * @author John Ericksen
 */
public interface MessageStrategy<T> {

    String getRequestType();

    String getRequestMessage();

    ResponseFactory<T> getResponseFactory();
}

package org.meteorminer.service;

import org.meteorminer.config.binding.GetWorkMessage;
import org.meteorminer.domain.Work;
import org.meteorminer.domain.WorkFactory;
import org.meteorminer.network.MessageStrategy;
import org.meteorminer.network.ResponseFactory;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class GetWorkMessageStrategy implements MessageStrategy<Work> {

    private static final String REQUEST_TYPE = "GetWork";
    @Inject
    @GetWorkMessage
    private String getWorkMessage;
    @Inject
    private WorkFactory workFactory;


    @Override
    public String getRequestType() {
        return REQUEST_TYPE;
    }

    @Override
    public String getRequestMessage() {
        return getWorkMessage;
    }

    @Override
    public ResponseFactory<Work> getResponseFactory() {
        return workFactory;
    }
}

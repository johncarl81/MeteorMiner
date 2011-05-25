package org.meteorminer.config;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.meteorminer.binding.*;
import org.meteorminer.domain.Work;

import java.net.Proxy;
import java.net.URL;
import java.text.DateFormat;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * Guice module class for Meteor Miner configuration.
 *
 * @author John Ericksen
 */
public class MeteorMinerModule extends AbstractModule {


    private MeteorAdvice meteorAdvice;

    public MeteorMinerModule(MeteorAdvice meteorAdvice) {
        this.meteorAdvice = meteorAdvice;
    }

    @Override
    protected void configure() {

        //Annotated @Injections
        bind(String.class).annotatedWith(Authorization.class)
                .toInstance("Basic " + Base64.encodeBase64String((meteorAdvice.getUsername() + ":" + meteorAdvice.getPassword()).getBytes()).trim());

        bind(URL.class).annotatedWith(BitcoinUrl.class)
                .toInstance(meteorAdvice.getBitcoinUrl());

        bind(new TypeLiteral<BlockingQueue<Work>>() {
        }).toInstance(new SynchronousQueue<Work>());

        bind(Proxy.class).annotatedWith(BitcoinProxy.class).toProvider(new ProxyProvider(meteorAdvice.getProxy()));
        bind(String.class).annotatedWith(GetWorkMessage.class).toInstance(createGetWorkMessage());
        bind(Integer.class).annotatedWith(GetWorkTimeout.class).toInstance(meteorAdvice.getGetWorkTimeout());
        bind(Boolean.class).annotatedWith(Verbose.class).toInstance(meteorAdvice.isVerbose());
        bind(Integer.class).annotatedWith(CPUCount.class).toInstance(meteorAdvice.getCpuCount());

        //additional singletons
        bind(Timer.class).toInstance(new Timer());
        bind(DateFormat.class).toInstance(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM));
    }

    private String createGetWorkMessage() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode getWorkNode = mapper.createObjectNode();

        getWorkNode.put("method", "getwork");
        getWorkNode.putArray("params");
        getWorkNode.put("id", 1);

        return getWorkNode.toString();
    }
}

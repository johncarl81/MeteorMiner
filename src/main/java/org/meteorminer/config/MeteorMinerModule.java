package org.meteorminer.config;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.meteorminer.config.binding.*;
import org.meteorminer.domain.Work;

import java.net.Authenticator;
import java.net.Proxy;
import java.net.URL;
import java.text.DateFormat;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;

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

        FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder();

        install(factoryModuleBuilder
                .build((ThreadFactory.class)));

        //Annotated @Injections
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
        bind(Timer.class).toInstance(new Timer(true));
        bind(DateFormat.class).toInstance(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM));

        Authenticator.setDefault(new ServerAuthenticator(meteorAdvice.getUsername(), meteorAdvice.getPassword(), meteorAdvice.getProxyUsername(), meteorAdvice.getProxyUsername()));
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

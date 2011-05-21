package org.meteorminer.config;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.meteorminer.LongPollWorker;
import org.meteorminer.LongPollWorkerFactory;
import org.meteorminer.Work;
import org.meteorminer.binding.*;
import org.meteorminer.queue.*;

import java.net.Proxy;
import java.net.URL;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * @author John Ericksen
 */
public class MeteorMinerModule extends AbstractModule {

    private MeteorAdvice meteorAdvice;

    private int queueSize = 1; //todo: externalize?

    public MeteorMinerModule(MeteorAdvice meteorAdvice) {
        this.meteorAdvice = meteorAdvice;
    }

    @Override
    protected void configure() {

        FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder();

        install(factoryModuleBuilder
                .implement(new TypeLiteral<Producer<Work>>() {
                        }, WorkProducer.class)
                .build(WorkProducerFactory.class));

        install(factoryModuleBuilder
                .implement(new TypeLiteral<Consumer<Work>>() {
                        }, WorkConsumer.class)
                .build((WorkConsumerFactory.class)));

        install(factoryModuleBuilder
                .implement(Runnable.class, LongPollWorker.class)
                .build((LongPollWorkerFactory.class)));


        bind(String.class).annotatedWith(Authorization.class)
                .toInstance("Basic " + Base64.encodeBase64String((meteorAdvice.getUsername() + ":" + meteorAdvice.getPassword()).getBytes()).trim());

        bind(URL.class).annotatedWith(BitcoinUrl.class)
                .toInstance(meteorAdvice.getBitcoinUrl());

        bind(new TypeLiteral<BlockingQueue<Work>>() {
        })
                .toInstance(new SynchronousQueue<Work>());

        bind(Proxy.class).annotatedWith(BitcoinProxy.class)
                .toProvider(new ProxyProvider(meteorAdvice.getProxy()));

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode getWorkNode = mapper.createObjectNode();

        getWorkNode.put("method", "getwork");
        getWorkNode.putArray("params");
        getWorkNode.put("id", 1);

        bind(String.class).annotatedWith(GetWorkMessage.class).toInstance(getWorkNode.toString());

        bind(Integer.class).annotatedWith(GetWorkTimeout.class).toInstance(meteorAdvice.getGetWorkTimeout());

        bind(Timer.class).toInstance(new Timer());
    }
}

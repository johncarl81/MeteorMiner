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
import org.meteorminer.binding.Authorization;
import org.meteorminer.binding.BitcoinProxy;
import org.meteorminer.binding.BitcoinUrl;
import org.meteorminer.binding.GetWorkMessage;
import org.meteorminer.queue.*;

import java.net.Proxy;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author John Ericksen
 */
public class MeteorMinerModule extends AbstractModule {

    private MeteorAdvice meteorAdvice;

    private int queueSize = 2; //todo: externalize?

    public MeteorMinerModule(MeteorAdvice meteorAdvice) {
        this.meteorAdvice = meteorAdvice;
    }

    @Override
    protected void configure() {

        FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder();

        install(factoryModuleBuilder
                .implement(new TypeLiteral<Producer<Work>>(){}, WorkProducer.class)
                .build(WorkProducerFactory.class));

        install(factoryModuleBuilder
                .implement(new TypeLiteral<Consumer<Work>>(){}, WorkConsumer.class)
                .build((WorkConsumerFactory.class)));

         install(factoryModuleBuilder
                .implement(Runnable.class, LongPollWorker.class)
                .build((LongPollWorkerFactory.class)));


        bind(String.class).annotatedWith(Authorization.class)
                .toInstance("Basic " + Base64.encodeBase64String((meteorAdvice.getUsername() + ":" + meteorAdvice.getPassword()).getBytes()).trim());

        bind(URL.class).annotatedWith(BitcoinUrl.class)
                .toInstance(meteorAdvice.getBitcoinUrl());

        bind(new TypeLiteral<ArrayBlockingQueue<Work>>() {
        })
                .toInstance(new ArrayBlockingQueue<Work>(queueSize));

        bind(Proxy.class).annotatedWith(BitcoinProxy.class)
                .toProvider(new ProxyProvider(meteorAdvice.getProxy()));

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode getWorkNode = mapper.createObjectNode();

        getWorkNode.put("method", "getwork");
        getWorkNode.putArray("params");
        getWorkNode.put("id", 1);

        bind(String.class).annotatedWith(GetWorkMessage.class).toInstance(getWorkNode.toString());
    }
}

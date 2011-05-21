package org.meteorminer.config;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.meteorminer.LongPollWorker;
import org.meteorminer.LongPollWorkerFactory;
import org.meteorminer.binding.*;
import org.meteorminer.domain.Work;
import org.meteorminer.hash.HashCacheScanner;
import org.meteorminer.hash.HashScanner;
import org.meteorminer.hash.gpu.GpuHashScanner;
import org.meteorminer.queue.WorkConsumer;
import org.meteorminer.queue.WorkConsumerFactory;
import org.meteorminer.queue.WorkProducer;
import org.meteorminer.queue.WorkProducerFactory;

import java.net.Proxy;
import java.net.URL;
import java.text.DateFormat;
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
                .implement(WorkProducer.class, WorkProducer.class)
                .build(WorkProducerFactory.class));

        install(factoryModuleBuilder
                .implement(WorkConsumer.class, WorkConsumer.class)
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

        GpuHashScanner hashScanner = new GpuHashScanner();
        requestInjection(hashScanner);
        HashCacheScanner scanner = new HashCacheScanner(hashScanner);

        bind(HashScanner.class).toInstance(scanner);
        bind(HashCacheScanner.class).toInstance(scanner);

        bind(DateFormat.class).toInstance(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT));
    }
}

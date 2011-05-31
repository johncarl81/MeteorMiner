package org.meteorminer.config;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.meteorminer.config.binding.*;
import org.meteorminer.hash.InterruptTimerTaskFactory;
import org.meteorminer.network.LongPollWorker;
import org.meteorminer.network.LongPollWorkerFactory;
import org.meteorminer.service.MinerStrategy;
import org.meteorminer.service.ParallelMinerStrategy;
import org.meteorminer.service.TandemMinerStrategy;

import java.net.Authenticator;
import java.net.Proxy;
import java.net.URL;
import java.text.DateFormat;
import java.util.List;
import java.util.Timer;
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

        install(factoryModuleBuilder
                .build((InterruptTimerTaskFactory.class)));

        install(factoryModuleBuilder
                .implement(Runnable.class, LongPollWorker.class)
                .build((LongPollWorkerFactory.class)));

        bind(MeteorAdvice.class).toInstance(meteorAdvice);

        //Annotated @Injections
        bind(URL.class).annotatedWith(BitcoinUrl.class)
                .toInstance(meteorAdvice.getBitcoinUrl());

        if (meteorAdvice.isTandem()) {
            bind(MinerStrategy.class).to(TandemMinerStrategy.class);
        } else {
            bind(MinerStrategy.class).to(ParallelMinerStrategy.class);
        }

        bind(Proxy.class).annotatedWith(BitcoinProxy.class).toProvider(new ProxyProvider(meteorAdvice.getProxy()));
        bind(String.class).annotatedWith(GetWorkMessage.class).toInstance(createGetWorkMessage());
        bind(Integer.class).annotatedWith(GetWorkTimeout.class).toInstance(meteorAdvice.getGetWorkTimeout());
        bind(Boolean.class).annotatedWith(Verbose.class).toInstance(meteorAdvice.isVerbose());
        bind(Integer.class).annotatedWith(CPUCount.class).toInstance(meteorAdvice.getCpuCount());
        bind(Integer.class).annotatedWith(Intensity.class).toInstance(meteorAdvice.getIntensity());
        bind(Integer.class).annotatedWith(WorkSize.class).toInstance(meteorAdvice.getWorksize());
        bind(new TypeLiteral<List<Integer>>() {
        }).annotatedWith(GPUIds.class).toInstance(meteorAdvice.getGpuIds());

        //additional singletons
        bind(Timer.class).toInstance(new Timer(true));
        bind(DateFormat.class).toInstance(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM));

        Authenticator.setDefault(new ServerAuthenticator(meteorAdvice.getUsername(), meteorAdvice.getPassword(), meteorAdvice.getProxyUsername(), meteorAdvice.getProxyPassword()));
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

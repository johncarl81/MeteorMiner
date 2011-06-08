package org.meteorminer.config;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.meteorminer.config.binding.*;
import org.meteorminer.hash.PreProcessWorkFactory;
import org.meteorminer.hash.gpu.GPUPreProcessWorkFactory;
import org.meteorminer.hash.scanHash.ScanHashPreProcessWorkFactory;
import org.meteorminer.service.MinerStrategy;
import org.meteorminer.service.ParallelMinerStrategy;
import org.meteorminer.service.TandemMinerStrategy;

import java.net.Authenticator;
import java.net.Proxy;
import java.net.URL;
import java.text.DateFormat;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * Guice module class for Meteor Miner configuration.
 *
 * @author John Ericksen
 */
public class MeteorApplicationModule extends AbstractModule {


    private MeteorAdvice meteorAdvice;

    public MeteorApplicationModule(MeteorAdvice meteorAdvice) {
        this.meteorAdvice = meteorAdvice;
    }

    @Override
    protected void configure() {

        FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder();

        install(factoryModuleBuilder
                .build((ThreadFactory.class)));

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
        bind(Long.class).annotatedWith(GetWorkTimeout.class).toInstance(meteorAdvice.getGetWorkTimeout());
        bind(Boolean.class).annotatedWith(Verbose.class).toInstance(meteorAdvice.isVerbose());
        bind(Integer.class).annotatedWith(CPUCount.class).toInstance(meteorAdvice.getCpuCount());
        bind(Integer.class).annotatedWith(Intensity.class).toInstance(meteorAdvice.getIntensity());
        bind(Integer.class).annotatedWith(WorkSize.class).toInstance(meteorAdvice.getWorksize());
        bind(Long.class).annotatedWith(NetworkErrorPause.class).toInstance(meteorAdvice.getNetworkErrorPause());
        bind(new TypeLiteral<List<Integer>>() {
        }).annotatedWith(GPUIds.class).toInstance(meteorAdvice.getGpuIds());

        //additional singletons
        final Timer timer = new Timer(true);
        bind(Timer.class).toInstance(timer);

        //runtime shutdown
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                timer.cancel();
            }
        });

        bind(DateFormat.class).toInstance(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM));

        Authenticator.setDefault(new ServerAuthenticator(meteorAdvice.getUsername(), meteorAdvice.getPassword(), meteorAdvice.getProxyUsername(), meteorAdvice.getProxyPassword()));

        //is this the right place for these?
        Multibinder<PreProcessWorkFactory> preProcessedWorkMultibinder = Multibinder.newSetBinder(binder(), PreProcessWorkFactory.class);
        preProcessedWorkMultibinder.addBinding().to(ScanHashPreProcessWorkFactory.class);
        preProcessedWorkMultibinder.addBinding().to(GPUPreProcessWorkFactory.class);

        bind(Runtime.class).toInstance(Runtime.getRuntime());

        bind(ExecutorService.class).annotatedWith(CachedThreadPool.class).toProvider(CachedThreadPoolProvider.class);
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
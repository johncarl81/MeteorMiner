package org.meteorminer.config.module;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import org.meteorminer.config.MeteorAdvice;
import org.meteorminer.config.MeteorMinerRuntimeException;
import org.meteorminer.config.ServerAuthenticator;
import org.meteorminer.config.binding.*;
import org.meteorminer.config.factory.CachedThreadPoolProvider;
import org.meteorminer.hash.PreProcessWorkFactory;
import org.meteorminer.hash.gpu.GPUPreProcessWorkFactory;
import org.meteorminer.hash.scanHash.ScanHashPreProcessWorkFactory;
import org.meteorminer.network.GetWorkMessageProvider;
import org.meteorminer.service.MinerStrategy;
import org.meteorminer.service.ParallelMinerStrategy;
import org.meteorminer.service.TandemMinerStrategy;

import java.net.Authenticator;
import java.net.Proxy;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutorService;

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
        bind(String.class).annotatedWith(GetWorkMessage.class).toProvider(GetWorkMessageProvider.class).asEagerSingleton();
        bind(Long.class).annotatedWith(GetWorkTimeout.class).toInstance(meteorAdvice.getGetWorkTimeout());
        bind(Boolean.class).annotatedWith(Verbose.class).toInstance(meteorAdvice.isVerbose());
        bind(Integer.class).annotatedWith(CPUCount.class).toInstance(meteorAdvice.getCpuCount());
        bind(Integer.class).annotatedWith(Intensity.class).toInstance(meteorAdvice.getIntensity());
        bind(Integer.class).annotatedWith(WorkSize.class).toInstance(meteorAdvice.getWorksize());
        bind(Long.class).annotatedWith(NetworkErrorPause.class).toInstance(meteorAdvice.getNetworkErrorPause());
        bind(Integer.class).annotatedWith(Vectors.class).toInstance(meteorAdvice.getVectors());
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

        try {
            bind(MessageDigest.class).toInstance(MessageDigest.getInstance("SHA-256"));
        } catch (NoSuchAlgorithmException e) {
            throw new MeteorMinerRuntimeException(e);
        }
    }
}

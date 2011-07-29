package org.meteorminer.config.module;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.meteorminer.config.MeteorMinerRuntimeException;
import org.meteorminer.config.ServerAuthenticator;
import org.meteorminer.config.ServerProvider;
import org.meteorminer.config.advice.MeteorAdvice;
import org.meteorminer.config.advice.ServerAdvice;
import org.meteorminer.config.binding.CachedThreadPool;
import org.meteorminer.config.binding.GetWorkMessage;
import org.meteorminer.config.binding.NetworkErrorPause;
import org.meteorminer.config.binding.Verbose;
import org.meteorminer.config.factory.CachedThreadPoolProvider;
import org.meteorminer.hash.PreProcessWorkFactory;
import org.meteorminer.hash.gpu.GPUPreProcessWorkFactory;
import org.meteorminer.hash.scanHash.ScanHashPreProcessWorkFactory;
import org.meteorminer.network.BitcoinUrlFactory;
import org.meteorminer.network.GetWorkMessageProvider;
import org.meteorminer.service.MinerStrategy;
import org.meteorminer.service.ParallelMinerStrategy;
import org.meteorminer.service.TandemMinerStrategy;

import java.net.Authenticator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
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
        if (meteorAdvice.isTandem()) {
            bind(MinerStrategy.class).to(TandemMinerStrategy.class);
        } else {
            bind(MinerStrategy.class).to(ParallelMinerStrategy.class);
        }

        bind(String.class).annotatedWith(GetWorkMessage.class).toProvider(GetWorkMessageProvider.class).asEagerSingleton();
        bind(Boolean.class).annotatedWith(Verbose.class).toInstance(meteorAdvice.isVerbose());
        bind(Long.class).annotatedWith(NetworkErrorPause.class).toInstance(meteorAdvice.getNetworkErrorPause());
        bind(ServerAdvice.class).toProvider(ServerProvider.class);
        bind(BitcoinUrlFactory.class).asEagerSingleton();

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

        Authenticator.setDefault(new ServerAuthenticator(meteorAdvice.getServers()));

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

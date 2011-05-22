package org.meteorminer.config;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.nativelibs4java.opencl.CLBuildException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.meteorminer.binding.*;
import org.meteorminer.domain.Work;
import org.meteorminer.hash.*;
import org.meteorminer.hash.gpu.*;
import org.meteorminer.network.LongPollWorker;
import org.meteorminer.network.LongPollWorkerFactory;
import org.meteorminer.queue.*;

import java.io.IOException;
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

    private static final String SEARCH_KERNEL = "search";
    private static final String SEARCH_KERNEL_FILE = "search.cl";

    private MeteorAdvice meteorAdvice;

    public MeteorMinerModule(MeteorAdvice meteorAdvice) {
        this.meteorAdvice = meteorAdvice;
    }

    @Override
    protected void configure() {

        FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder();

        install(factoryModuleBuilder
                .implement(WorkProducer.class, WorkProducerMultiplex.class)
                .build(WorkProducerFactory.class));

        install(factoryModuleBuilder
                .implement(WorkConsumer.class, WorkConsumer.class)
                .build((WorkConsumerFactory.class)));

        install(factoryModuleBuilder
                .implement(Runnable.class, LongPollWorker.class)
                .build((LongPollWorkerFactory.class)));

        install(factoryModuleBuilder
                .implement(DiabloMiner.class, DiabloMiner.class)
                .build((DiabloMinerFactory.class)));

        install(factoryModuleBuilder
                .implement(InteruptTimerTask.class, InteruptTimerTask.class)
                .build((InteruptTimerTaskFactory.class)));

        install(factoryModuleBuilder
                .implement(HashStatisticsOutputTimerTask.class, HashStatisticsOutputTimerTask.class)
                .build((HashStatisticsOutputTimerTaskFactory.class)));


        bind(String.class).annotatedWith(Authorization.class)
                .toInstance("Basic " + Base64.encodeBase64String((meteorAdvice.getUsername() + ":" + meteorAdvice.getPassword()).getBytes()).trim());

        bind(URL.class).annotatedWith(BitcoinUrl.class)
                .toInstance(meteorAdvice.getBitcoinUrl());

        bind(new TypeLiteral<BlockingQueue<Work>>() {
        }).toInstance(new SynchronousQueue<Work>());

        bind(Proxy.class).annotatedWith(BitcoinProxy.class).toProvider(new ProxyProvider(meteorAdvice.getProxy()));
        bind(String.class).annotatedWith(GetWorkMessage.class).toInstance(createGetWorkMessage());
        bind(Integer.class).annotatedWith(GetWorkTimeout.class).toInstance(meteorAdvice.getGetWorkTimeout());
        bind(HashScanner.class).annotatedWith(Preferred.class).to(GpuHashScanner.class);
        bind(HashChecker.class).annotatedWith(Preferred.class).to(HashCheckerImpl.class);
        bind(Boolean.class).annotatedWith(Verbose.class).toInstance(meteorAdvice.isVerbose());

        IntBufferPoolFactory intBufferPoolFactory = new IntBufferPoolFactory();
        CLIntBufferPoolFactory clIntBufferPoolFactory = new CLIntBufferPoolFactory();

        requestInjection(intBufferPoolFactory);
        requestInjection(clIntBufferPoolFactory);

        bind(ObjectPool.class).annotatedWith(IntBufferPool.class).toInstance(new GenericObjectPool(intBufferPoolFactory));
        bind(ObjectPool.class).annotatedWith(CLIntBufferPool.class).toInstance(new GenericObjectPool(clIntBufferPoolFactory));

        try {
            bind(OCL.class).annotatedWith(SearchKernel.class).toInstance(new OCL(SEARCH_KERNEL_FILE, SEARCH_KERNEL));
        } catch (CLBuildException e) {
            throw new RuntimeException("Error Creating Kernel", e);
        } catch (IOException e) {
            throw new RuntimeException("Error Creating Kernel", e);
        }

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

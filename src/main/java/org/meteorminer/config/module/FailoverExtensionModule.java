package org.meteorminer.config.module;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.meteorminer.network.RPCExtension;
import org.meteorminer.network.failover.MaintanenceSwitchExtension;

/**
 * Module defining the Failover RPC Extension
 *
 * @author John Ericksen
 */
public class FailoverExtensionModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<RPCExtension> rpcExtensionMultibinder = Multibinder.newSetBinder(binder(), RPCExtension.class);
        //rpcExtensionMultibinder.addBinding().to(HostFailoverExtension.class).asEagerSingleton();
        rpcExtensionMultibinder.addBinding().to(MaintanenceSwitchExtension.class).asEagerSingleton();
    }
}

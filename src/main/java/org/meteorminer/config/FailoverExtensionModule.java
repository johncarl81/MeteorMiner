package org.meteorminer.config;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.meteorminer.network.RPCExtension;
import org.meteorminer.network.failover.HostFailoverExtension;
import org.meteorminer.network.failover.MaintanenceSwitchExtension;

/**
 * @author John Ericksen
 */
public class FailoverExtensionModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<RPCExtension> rpcExtensionMultibinder = Multibinder.newSetBinder(binder(), RPCExtension.class);
        rpcExtensionMultibinder.addBinding().to(HostFailoverExtension.class);
        rpcExtensionMultibinder.addBinding().to(MaintanenceSwitchExtension.class);
    }
}
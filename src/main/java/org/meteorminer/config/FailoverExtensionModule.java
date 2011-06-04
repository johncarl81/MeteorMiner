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
        Multibinder<RPCExtension> uriBinder = Multibinder.newSetBinder(binder(), RPCExtension.class);
        uriBinder.addBinding().to(HostFailoverExtension.class);
        uriBinder.addBinding().to(MaintanenceSwitchExtension.class);
    }
}

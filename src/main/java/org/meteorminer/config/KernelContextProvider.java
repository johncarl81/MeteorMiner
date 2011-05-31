package org.meteorminer.config;

import com.nativelibs4java.opencl.CLBuildException;
import com.nativelibs4java.opencl.CLDevice;
import org.meteorminer.hash.gpu.KernelContext;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.IOException;

/**
 * @author John Ericksen
 */
public class KernelContextProvider implements Provider<KernelContext> {

    private static final String SEARCH_KERNEL = "search";
    private static final String SEARCH_KERNEL_FILE = "search.cl";
    @Inject
    private CLDevice device;

    @Override
    public KernelContext get() {
        KernelContext kernelContext;

        try {
            kernelContext = new KernelContext(SEARCH_KERNEL_FILE, SEARCH_KERNEL, device);
        } catch (CLBuildException e) {
            throw new MeteorMinerRuntimeException("Error Creating Kernel", e);
        } catch (IOException e) {
            throw new MeteorMinerRuntimeException("Error Creating Kernel", e);
        }

        return kernelContext;
    }
}

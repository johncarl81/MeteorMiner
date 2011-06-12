package org.meteorminer.hash.gpu;


import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;
import com.nativelibs4java.opencl.JavaCL;
import org.junit.Before;
import org.junit.Test;
import org.meteorminer.config.MeteorAdvice;
import org.meteorminer.config.module.*;
import org.meteorminer.domain.Work;
import org.meteorminer.hash.*;

import java.net.MalformedURLException;

import static junit.framework.Assert.*;

public class GpuHashTest {

    private GpuHashScanner scanHash;
    private WorkFoundCallbackTester callbackTester;
    private MockNonceIteratorFactory nonceFactory;
    private WorkConsumer workSource;
    private WorkMockFactory workMockFactory;

    @Before
    public void setup() throws MalformedURLException {
        Injector injector = Guice.createInjector(
                Modules.override(new MeteorApplicationModule(new MeteorAdvice()),
                        new MinerModule(),
                        new FailoverExtensionModule(),
                        new LongPollExtensionModule(),
                        new DeviceModule(),
                        new GPUDeviceModule(JavaCL.getBestDevice(), 0)).with(
                        new SynchronousModule(),
                        new GPUSynchronousModule()));
        scanHash = injector.getInstance(GpuHashScanner.class);
        callbackTester = injector.getInstance(WorkFoundCallbackTester.class);
        nonceFactory = injector.getInstance(MockNonceIteratorFactory.class);
        workSource = injector.getInstance(WorkConsumer.class);
        workMockFactory = injector.getInstance(WorkMockFactory.class);
    }

    @Test
    public void testScanHashNegative() {
        Work work = workMockFactory.getUnsuccessfulWork();

        callbackTester.setExpectedNonce(0);
        nonceFactory.setRange(1, 0xffff);
        workSource.pushWork(work);
        scanHash.innerScan();

        assertFalse("No match for casial hash", callbackTester.isFound());
    }

    @Test
    public void testScanHashPositive() {
        Work work = workMockFactory.getSuccessfulWork();

        callbackTester.setExpectedNonce(30911318);
        nonceFactory.setRange(0x1d70bd0, 0xffff);
        workSource.pushWork(work);
        scanHash.innerScan();

        assertTrue("Known sol'n", callbackTester.isFound());
        assertEquals(
                "Known sol'n",
                work.getDataString(),
                "0000000114cbad4d7252a937cb65437645722fa3c6cf16cfd3eaa3fc0001e6f6000000008249f5c8ee2f04f0cdca30b97949373d00db1b34d45253407567df2ce552a9ed4d1d5c9c1b04864c00000000000000800000000000000000000000000000000000000000000000000000000000000000000000000000000080020000");
    }

    @Test
    public void testScanHashPositive2() {
        Work work = workMockFactory.getSuccessfulWork2();

        callbackTester.setExpectedNonce(563799816);
        //solution is 563799816
        //            110297600
        nonceFactory.setRange(563799000, 1);
        workSource.pushWork(work);
        scanHash.innerScan();

        assertTrue("Known sol'n", callbackTester.isFound());

    }
}
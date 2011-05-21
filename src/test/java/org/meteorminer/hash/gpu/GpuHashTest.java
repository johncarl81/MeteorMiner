package org.meteorminer.hash.gpu;


import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.util.Modules;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.meteorminer.config.MeteorAdvice;
import org.meteorminer.config.MeteorMinerModule;
import org.meteorminer.domain.Work;
import org.meteorminer.hash.ShortCircuitException;
import org.meteorminer.hash.VerifyHash;
import org.meteorminer.hash.WorkFoundCallbackFactory;
import org.meteorminer.hash.WorkFoundCallbackTester;
import org.meteorminer.hash.scanHash.DigestProcessHashImpl;

import java.net.MalformedURLException;

public class GpuHashTest extends TestCase {

    private GpuHashScanner scanHash;
    private WorkFoundCallbackFactory callbackFactory;

    @Override
    public void setUp() throws MalformedURLException {
        Injector injector = Guice.createInjector(
                Modules.override(new MeteorMinerModule(new MeteorAdvice())).with(
                        new AbstractModule() {

                            @Override
                            protected void configure() {
                                FactoryModuleBuilder factoryModuleBuilder = new FactoryModuleBuilder();

                                install(factoryModuleBuilder
                                        .implement(WorkFoundCallbackTester.class, WorkFoundCallbackTester.class)
                                        .build(WorkFoundCallbackFactory.class));

                                //bind directly to avoid asynchronous behaviour with
                                bind(VerifyHash.class).to(DigestProcessHashImpl.class);
                            }
                        }
                ));
        scanHash = injector.getInstance(GpuHashScanner.class);
        callbackFactory = injector.getInstance(WorkFoundCallbackFactory.class);
    }

    public void testScanHashNegative() {
        Work work = new Work(
                "00000001569be4f2b5b23e745240aaa149084029850973b78b0c5ce40002f41600000000f1c3d9c8a8a701275715da32e577521340180146e3517c8ebb1d0044feaa9f3f4d1d355d1b04864c00000000000000800000000000000000000000000000000000000000000000000000000000000000000000000000000080020000",
                "babe3a22106e8bd2274bcb1571042c5dde2ee927a3fb62606938ab8ae7b241ba",
                "00000000000000000000000000000000000000000000000000000000000000000000008000000000000000000000000000000000000000000000000000010000",
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffff00000000");

        WorkFoundCallbackTester tester = callbackFactory.buildCallback(0, false);
        scanHash.innerScan(work, tester, 1, 0xffffL);

        Assert.assertFalse("No match for casial hash", tester.isFound());
    }

    public void testScanHashPositive() {
        Work work = new Work(
                "0000000114cbad4d7252a937cb65437645722fa3c6cf16cfd3eaa3fc0001e6f6000000008249f5c8ee2f04f0cdca30b97949373d00db1b34d45253407567df2ce552a9ed4d1d5c9c1b04864c00000000000000800000000000000000000000000000000000000000000000000000000000000000000000000000000080020000",
                "f5cb759978f54c15cef60cc43fa510bb5621fc1ddb4bb285efe6e4c55aa3fb85",
                "00000000000000000000000000000000000000000000000000000000000000000000008000000000000000000000000000000000000000000000000000010000",
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffff00000000");

        WorkFoundCallbackTester tester = callbackFactory.buildCallback(30911318, true);

        try {
            scanHash.innerScan(work, tester, 0x1d70bd0, 0xffffff);
        } catch (ShortCircuitException e) {/*short circuit*/}

        Assert.assertTrue("Known sol'n", tester.isFound());
        Assert.assertEquals(
                "Known sol'n",
                work.getDataString(),
                "0000000114cbad4d7252a937cb65437645722fa3c6cf16cfd3eaa3fc0001e6f6000000008249f5c8ee2f04f0cdca30b97949373d00db1b34d45253407567df2ce552a9ed4d1d5c9c1b04864c00000000000000800000000000000000000000000000000000000000000000000000000000000000000000000000000080020000");
    }

    public void testScanHashPositive2() {
        Work work = new Work(
                "00000001c9d358447ba95319a19300bfc94a286ed6a12856f8e4775e00005de6000000009c64db358b88376c70d0101aafaace8c46fb7988e9e3f070c234903fa7ed5aa24dd341741a6a93b300000000000000800000000000000000000000000000000000000000000000000000000000000000000000000000000080020000",
                "c94675051186fcd6fb7f92f20c7248da15efcc56924c77712220240573183c17",
                "00000000000000000000000000000000000000000000000000000000000000000000008000000000000000000000000000000000000000000000000000010000",
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffff00000000");

        WorkFoundCallbackTester tester = callbackFactory.buildCallback(563799816, true);
        //solution is 563799816
        //            110297600
        try {
            scanHash.innerScan(work, tester, 563700000, 563799817);
        } catch (ShortCircuitException e) {/*short circuit*/}

        Assert.assertTrue("Known sol'n", tester.isFound());

    }
}
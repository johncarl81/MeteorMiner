package org.meteorminer.hash.gpu;


import junit.framework.Assert;
import junit.framework.TestCase;
import org.meteorminer.Work;
import org.meteorminer.hash.MinerController;
import org.meteorminer.hash.scanHash.ProcessHash;
import org.meteorminer.queue.WorkFoundCallback;
import org.meteorminer.stats.Statistics;

import java.util.Timer;

public class GpuHashTest extends TestCase {

	private GpuHashScanner scanHash;

	@Override
	public void setUp() {
		scanHash = new GpuHashScanner();
        scanHash.setStatistics(new Statistics());
        scanHash.setGetWorkTimeout(1000);
        scanHash.setMinerController(new MinerController());
        scanHash.setTimer(new Timer());
        ProcessHash hash = new ProcessHash();
        hash.setStats(new Statistics());
        scanHash.setProcessHash(hash);


	}

	public void testScanHashNegative() {
		Work work = new Work(
				"00000001569be4f2b5b23e745240aaa149084029850973b78b0c5ce40002f41600000000f1c3d9c8a8a701275715da32e577521340180146e3517c8ebb1d0044feaa9f3f4d1d355d1b04864c00000000000000800000000000000000000000000000000000000000000000000000000000000000000000000000000080020000",
				"babe3a22106e8bd2274bcb1571042c5dde2ee927a3fb62606938ab8ae7b241ba",
				"00000000000000000000000000000000000000000000000000000000000000000000008000000000000000000000000000000000000000000000000000010000",
				"ffffffffffffffffffffffffffffffffffffffffffffffffffffffff00000000");

        WorkFoundCallbackTester tester = new WorkFoundCallbackTester(0);
        scanHash.scan(work, tester, 1, 0xffffL);

		Assert.assertFalse("No match for casial hash", tester.isFound());
	}

	public void testScanHashPositive() {
		Work work = new Work(
				"0000000114cbad4d7252a937cb65437645722fa3c6cf16cfd3eaa3fc0001e6f6000000008249f5c8ee2f04f0cdca30b97949373d00db1b34d45253407567df2ce552a9ed4d1d5c9c1b04864c00000000000000800000000000000000000000000000000000000000000000000000000000000000000000000000000080020000",
				"f5cb759978f54c15cef60cc43fa510bb5621fc1ddb4bb285efe6e4c55aa3fb85",
				"00000000000000000000000000000000000000000000000000000000000000000000008000000000000000000000000000000000000000000000000000010000",
				"ffffffffffffffffffffffffffffffffffffffffffffffffffffffff00000000");

        WorkFoundCallbackTester tester = new WorkFoundCallbackTester(30911318);

        try{

            scanHash.scan(work, tester, 0x1d70bd0, 0xffffff);
        }
        catch(RuntimeException e){//good
        }

		Assert.assertTrue("Known sol'n", tester.isFound());
		Assert.assertEquals(
                "Known sol'n",
                work.getDataString(),
                "0000000114cbad4d7252a937cb65437645722fa3c6cf16cfd3eaa3fc0001e6f6000000008249f5c8ee2f04f0cdca30b97949373d00db1b34d45253407567df2ce552a9ed4d1d5c9c1b04864c00000000000000800000000000000000000000000000000000000000000000000000000000000000000000000000000080020000");
	}

    public void testScanHashPositive2(){
        Work work = new Work(
                "00000001c9d358447ba95319a19300bfc94a286ed6a12856f8e4775e00005de6000000009c64db358b88376c70d0101aafaace8c46fb7988e9e3f070c234903fa7ed5aa24dd341741a6a93b300000000000000800000000000000000000000000000000000000000000000000000000000000000000000000000000080020000",
                "c94675051186fcd6fb7f92f20c7248da15efcc56924c77712220240573183c17",
                "00000000000000000000000000000000000000000000000000000000000000000000008000000000000000000000000000000000000000000000000000010000",
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffff00000000");

        WorkFoundCallbackTester tester = new WorkFoundCallbackTester(563799816);
        //solution is 563799816
        //            110297600
        try{
            scanHash.scan(work, tester, 563700000, 563799817);
        }
        catch(RuntimeException e){//good
        }

		Assert.assertTrue("Known sol'n", tester.isFound());

    }


    private class WorkFoundCallbackTester implements WorkFoundCallback{

        private boolean found = false;
        private int expectedNonce;

        private WorkFoundCallbackTester(int expectedNonce) {
            this.expectedNonce = expectedNonce;
        }

        public void found(Work work, int nonce){
            found = true;
            assertEquals(expectedNonce, nonce);
            throw new RuntimeException(); //short circuit
        }

        public boolean isFound(){
            return found;
        }
    }
}
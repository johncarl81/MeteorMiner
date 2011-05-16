package org.meteorminer.hash;


import junit.framework.Assert;
import junit.framework.TestCase;
import org.meteorminer.Work;
import org.meteorminer.queue.WorkFoundCallback;
import org.meteorminer.stats.Statistics;

public class ScanHashTest extends TestCase {
	protected ScanHash scanHash;

    private static final String DATA = "00000001569be4f2b5b23e745240aaa149084029850973b78b0c5ce40002f41600000000f1c3d9c8a8a701275715da32e577521340180146e3517c8ebb1d0044feaa9f3f4d1d355d1b04864c00000000000000800000000000000000000000000000000000000000000000000000000000000000000000000000000080020000";
    private static final String STATE = "babe3a22106e8bd2274bcb1571042c5dde2ee927a3fb62606938ab8ae7b241ba";

	@Override
	public void setUp() {
		scanHash = new ScanHash();
        scanHash.setStatistics(new Statistics());
	}

	public void testScanHashNegative() {
		Work work = new Work(
				"00000001569be4f2b5b23e745240aaa149084029850973b78b0c5ce40002f41600000000f1c3d9c8a8a701275715da32e577521340180146e3517c8ebb1d0044feaa9f3f4d1d355d1b04864c00000000000000800000000000000000000000000000000000000000000000000000000000000000000000000000000080020000",
				"babe3a22106e8bd2274bcb1571042c5dde2ee927a3fb62606938ab8ae7b241ba",
				"00000000000000000000000000000000000000000000000000000000000000000000008000000000000000000000000000000000000000000000000000010000",
				"ffffffffffffffffffffffffffffffffffffffffffffffffffffffff00000000");

        WorkFoundCallbackTester tester = new WorkFoundCallbackTester();

        scanHash.scan(work, 1, 0xffff, tester);

		Assert.assertFalse("No match for casial hash", tester.isFound());
	}

	public void testScanHashPositive() {
		Work work = new Work(
				"0000000114cbad4d7252a937cb65437645722fa3c6cf16cfd3eaa3fc0001e6f6000000008249f5c8ee2f04f0cdca30b97949373d00db1b34d45253407567df2ce552a9ed4d1d5c9c1b04864c00000000000000800000000000000000000000000000000000000000000000000000000000000000000000000000000080020000",
				"f5cb759978f54c15cef60cc43fa510bb5621fc1ddb4bb285efe6e4c55aa3fb85",
				"00000000000000000000000000000000000000000000000000000000000000000000008000000000000000000000000000000000000000000000000000010000",
				"ffffffffffffffffffffffffffffffffffffffffffffffffffffffff00000000");

        WorkFoundCallbackTester tester = new WorkFoundCallbackTester();

        scanHash.scan(work, 0x1d70bd0, 0xffff, tester);

		Assert.assertTrue("Known sol'n", tester.isFound());
		Assert.assertEquals(
                "Known sol'n",
                work.getDataString(),
                "0000000114cbad4d7252a937cb65437645722fa3c6cf16cfd3eaa3fc0001e6f6000000008249f5c8ee2f04f0cdca30b97949373d00db1b34d45253407567df2ce552a9ed4d1d5c9c1b04864c56abd701000000800000000000000000000000000000000000000000000000000000000000000000000000000000000080020000");
	}

    private class WorkFoundCallbackTester implements WorkFoundCallback{

        private boolean found = false;

        public void found(Work work) {
            found = true;
        }

        public boolean isFound(){
            return found;
        }
    }
}
package org.meteorminer.hash;

import com.google.inject.Inject;
import org.meteorminer.domain.Work;
import org.meteorminer.domain.WorkFactory;

import javax.inject.Singleton;

/**
 * @author John Ericksen
 */
@Singleton
public class WorkMockFactory {

    private static final String TARGET = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffff00000000";
    private static final String NONCE = "00000000000000000000000000000000000000000000000000000000000000000000008000000000000000000000000000000000000000000000000000010000";

    private Work succcessfulWork;
    private Work successsfulWork2;
    private Work unsuccessfulWork;

    @Inject
    public WorkMockFactory(WorkFactory workFactory) {
        succcessfulWork = workFactory.buildWork(
                "0000000114cbad4d7252a937cb65437645722fa3c6cf16cfd3eaa3fc0001e6f6000000008249f5c8ee2f04f0cdca30b97949373d00db1b34d45253407567df2ce552a9ed4d1d5c9c1b04864c00000000000000800000000000000000000000000000000000000000000000000000000000000000000000000000000080020000",
                "f5cb759978f54c15cef60cc43fa510bb5621fc1ddb4bb285efe6e4c55aa3fb85",
                NONCE, TARGET);

        successsfulWork2 = workFactory.buildWork(
                "00000001c9d358447ba95319a19300bfc94a286ed6a12856f8e4775e00005de6000000009c64db358b88376c70d0101aafaace8c46fb7988e9e3f070c234903fa7ed5aa24dd341741a6a93b300000000000000800000000000000000000000000000000000000000000000000000000000000000000000000000000080020000",
                "c94675051186fcd6fb7f92f20c7248da15efcc56924c77712220240573183c17",
                NONCE, TARGET);

        unsuccessfulWork = workFactory.buildWork(
                "00000001569be4f2b5b23e745240aaa149084029850973b78b0c5ce40002f41600000000f1c3d9c8a8a701275715da32e577521340180146e3517c8ebb1d0044feaa9f3f4d1d355d1b04864c00000000000000800000000000000000000000000000000000000000000000000000000000000000000000000000000080020000",
                "babe3a22106e8bd2274bcb1571042c5dde2ee927a3fb62606938ab8ae7b241ba",
                NONCE, TARGET);
    }

    public Work getSuccessfulWork() {
        return succcessfulWork;
    }

    public Work getUnsuccessfulWork() {
        return unsuccessfulWork;
    }

    public Work getSuccessfulWork2() {
        return successsfulWork2;
    }
}

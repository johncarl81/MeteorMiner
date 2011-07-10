package org.meteorminer.network;

import com.google.inject.assistedinject.Assisted;
import org.meteorminer.domain.Work;
import org.meteorminer.output.CLInterface;
import org.meteorminer.output.Statistics;

import javax.inject.Inject;
import java.io.IOException;

/**
 * @author John Ericksen
 */
public class WorkSubmit implements Runnable {

    private Work work;
    private JsonClient jsonClient;
    private Statistics stats;
    private int nonce;
    private CLInterface output;
    private SubmitWorkMessageStrategyFactory submitWorkMessageStrategyFactory;

    @Inject
    public WorkSubmit(@Assisted Work work, @Assisted int nonce,
                      JsonClient jsonClient, Statistics stats,
                      CLInterface output,
                      SubmitWorkMessageStrategyFactory submitWorkMessageStrategyFactory) {
        this.work = work;
        this.jsonClient = jsonClient;
        this.stats = stats;
        this.nonce = nonce;
        this.output = output;
        this.submitWorkMessageStrategyFactory = submitWorkMessageStrategyFactory;
    }

    public void run() {
        try {
            output.verbose("Work passed local verification.  Proceeding to submit.");

            SubmitWorkMessageStrategy submitWorkMessageStrategy = submitWorkMessageStrategyFactory.buildSubmitWorkStrategy(work);
            //last chance to check for stale work
            if (!work.isStale()) {
                SubmitWorkResponse workResponse = jsonClient.execute(submitWorkMessageStrategy);

                if (workResponse.isResult()) {
                    output.notification("Hash Submitted: %08x", nonce);
                    stats.incrementWorkPass(1);

                } else {
                    output.notification("Hash Rejected: %08x", nonce);
                    stats.incrementWorkFail(1);
                }
            }

        } catch (IOException e) {
            output.notification("Exception while submitting the following work:");
            output.notification(work.toString());
            output.error(e);
        }
    }
}

package org.meteorminer.hash.scanHash;

import org.meteorminer.domain.Work;
import org.meteorminer.hash.VerifyHash;
import org.meteorminer.output.CLInterface;
import org.meteorminer.service.WorkFoundCallback;

import javax.inject.Inject;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

/**
 * @author John Ericksen
 */
public class DigestProcessHashImpl implements VerifyHash {

    @Inject
    private CLInterface output;
    @Inject
    private WorkFoundCallback workFoundCallback;
    @Inject
    private MessageDigest digest;

    @Override
    public void verify(Work work, int nonce) {
        final ByteBuffer digestInput = ByteBuffer.allocate(80);

        for (int j = 0; j < 19; j++)
            digestInput.putInt(j * 4, work.getData()[j]);

        digestInput.putInt(19 * 4, nonce);

        byte[] digestOutput = digest.digest(digest.digest(digestInput.array()));

        long G = ((long) ((0xFF & ((int) digestOutput[27])) << 24 |
                (0xFF & ((int) digestOutput[26])) << 16 |
                (0xFF & ((int) digestOutput[25])) << 8 |
                (0xFF & ((int) digestOutput[24]))));

        long H = ((long) ((0xFF & ((int) digestOutput[31])) << 24 |
                (0xFF & ((int) digestOutput[30])) << 16 |
                (0xFF & ((int) digestOutput[29])) << 8 |
                (0xFF & ((int) digestOutput[28]))));

        if (G <= work.getTarget()[6]) {
            if (H == 0) {
                workFoundCallback.found(work, nonce);
            } else {
                output.notification("Invalid block found, possible driver or hardware issue");
            }
        } else {
            output.verbose("Hash failed internal validation.");
        }
    }
}

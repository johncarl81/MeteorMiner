package org.meteorminer.hash.scanHash;

import org.meteorminer.domain.Work;
import org.meteorminer.hash.VerifyHash;
import org.meteorminer.output.CLInterface;
import org.meteorminer.service.WorkFoundCallback;

import javax.inject.Inject;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author John Ericksen
 */
public class DigestProcessHashImpl implements VerifyHash {

    @Inject
    private CLInterface output;
    @Inject
    private WorkFoundCallback workFoundCallback;

    @Override
    public void verify(Work work, int nonce) {
        try {
            final ByteBuffer digestInput = ByteBuffer.allocate(80);

            for (int j = 0; j < 19; j++)
                digestInput.putInt(j * 4, work.getData()[j]);

            digestInput.putInt(19 * 4, nonce);

            final MessageDigest digestInside = MessageDigest.getInstance("SHA-256");
            final MessageDigest digestOutside = MessageDigest.getInstance("SHA-256");

            byte[] digestOutput = digestOutside.digest(digestInside.digest(digestInput.array()));

            long G = ((long) ((0x000000FF & ((int) digestOutput[27])) << 24 |
                    (0x000000FF & ((int) digestOutput[26])) << 16 |
                    (0x000000FF & ((int) digestOutput[25])) << 8 |
                    (0x000000FF & ((int) digestOutput[24])))) & 0xFFFFFFFFL;

            long H = ((long) ((0x000000FF & ((int) digestOutput[31])) << 24 |
                    (0x000000FF & ((int) digestOutput[30])) << 16 |
                    (0x000000FF & ((int) digestOutput[29])) << 8 |
                    (0x000000FF & ((int) digestOutput[28])))) & 0xFFFFFFFFL;

            if (G <= work.getTarget()[6]) {
                if (H == 0) {
                    workFoundCallback.found(work, nonce);
                } else {
                    output.notification("Invalid block found, possible driver or hardware issue");
                }
            } else {
                output.verbose("Hash failed internal validation.");
            }
        } catch (NoSuchAlgorithmException e) {
            output.error(e);
        }
    }
}

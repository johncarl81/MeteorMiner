package org.meteorminer.network;

import org.meteorminer.domain.Work;

/**
 * @author John Ericksen
 */
public class SubmitWorkMessage extends GetWorkMessage {

    public SubmitWorkMessage(Work work) {
        getParams().add(encodeBlock(work));
    }

    private String encodeBlock(Work work) {
        StringBuilder builder = new StringBuilder();

        for (int d : work.getData())
            builder.append(String.format("%08x", Integer.reverseBytes(d)));

        return builder.toString();
    }


}

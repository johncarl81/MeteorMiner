package org.meteorminer.domain;

import com.google.gson.Gson;
import org.meteorminer.hash.PreProcessWorkFactory;
import org.meteorminer.network.ResponseFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

/**
 * Factory building the Work object
 *
 * @author John Ericksen
 */
@Singleton
public class WorkFactory implements ResponseFactory<Work> {

    @Inject
    private Set<PreProcessWorkFactory> preProcessWorkFactories;
    @Inject
    private Gson gson;

    public Work buildWork(String midstate, String data, String hash1, String target) {
        Work work = new Work(midstate, data, hash1, target);

        preprocessWork(work);

        return work;
    }

    public void setPreProcessWorkFactories(Set<PreProcessWorkFactory> preProcessWorkFactories) {
        this.preProcessWorkFactories = preProcessWorkFactories;
    }

    /**
     * Build a work object from the given JSON response node from the GetWork request.
     *
     * @param inputStream
     * @return Work
     */
    @Override
    public Work buildResponse(InputStream inputStream) {
        Work work = gson.fromJson(new InputStreamReader(inputStream), GetWorkResponse.class).getResult();
        work.processStrings();

        preprocessWork(work);

        work.updateTime();

        return work;
    }

    private void preprocessWork(Work work) {
        //preprocessing
        for (PreProcessWorkFactory preProcessWorkFactory : preProcessWorkFactories) {
            work.getPreProcessedWork().put(preProcessWorkFactory.getPreProcessWorkName(),
                    preProcessWorkFactory.buildPreProcessWork(work));
        }
    }
}

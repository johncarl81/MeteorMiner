package org.meteorminer.domain;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import org.meteorminer.config.MeteorAdvice;
import org.meteorminer.config.module.MeteorApplicationModule;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static junit.framework.Assert.assertEquals;

/**
 * @author John Ericksen
 */
public class WorkFactoryTest {

    private static final String MIDSTATE = "28d3719ed784688c07ca3b61f56206aad88bcbf008e238d5ef4ae2d6ca9b005a";
    private static final String DATA = "00000001f448fe14f18e525cf2145a230e69c607d76f874e53a137610000031d00000000ef2d01297576cec0c8" +
            "177eee8039bcebfa45cc39ece5b38a85f41335bcde63d04e108b071a0c2a12000000000000008000000000000000000000000" +
            "00000000000000000000000000000000000000000000000000000000080020000";
    private static final String HASH1 = "00000000000000000000000000000000000000000000000000000000000000000000008000000000000000000" +
            "000000000000000000000000000000000010000";
    private static final String TARGET = "ffffffffffffffffffffffffffffffffffffffffffffffffffffffff00000000";

    private WorkFactory workFactory;

    @Before
    public void setup() {
        Injector injector = Guice.createInjector(new MeteorApplicationModule(new MeteorAdvice()));
        workFactory = injector.getInstance(WorkFactory.class);
    }

    @Test
    public void createFromInputStream() {
        String test = "{\"result\":{\"midstate\":\"" + MIDSTATE + "\"," +
                "\"data\":\"" + DATA + "\"," +
                "\"hash1\":\"" + HASH1 + "\"," +
                "\"target\":\"" + TARGET + "\"}," +
                "\"error\":null,\"id\":1}";

        InputStream inputStream = new ByteArrayInputStream(test.getBytes());

        Work work = workFactory.buildResponse(inputStream);

        assertEquals(MIDSTATE, work.getMidstateString());
        assertEquals(DATA, work.getDataString());
        assertEquals(HASH1, work.getHash1());
        assertEquals(TARGET, work.getTargetString());
    }
}

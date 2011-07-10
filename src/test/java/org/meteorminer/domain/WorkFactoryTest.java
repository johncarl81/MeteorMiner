package org.meteorminer.domain;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import org.meteorminer.config.MeteorAdvice;
import org.meteorminer.config.module.MeteorApplicationModule;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author John Ericksen
 */
public class WorkFactoryTest {

    private WorkFactory workFactory;

    @Before
    public void setup() {
        Injector injector = Guice.createInjector(new MeteorApplicationModule(new MeteorAdvice()));
        workFactory = injector.getInstance(WorkFactory.class);
    }

    @Test
    public void createFromInputStream() {
        String test = "{\"result\":{\"midstate\":\"28d3719ed784688c07ca3b61f56206aad88bcbf008e238d5ef4ae2d6ca9b005a\"," +
                "\"data\":\"00000001f448fe14f18e525cf2145a230e69c607d76f874e53a137610000031d00000000ef2d01297576cec0c8" +
                "177eee8039bcebfa45cc39ece5b38a85f41335bcde63d04e108b071a0c2a12000000000000008000000000000000000000000" +
                "00000000000000000000000000000000000000000000000000000000080020000\"," +
                "\"hash1\":\"00000000000000000000000000000000000000000000000000000000000000000000008000000000000000000" +
                "000000000000000000000000000000000010000\"," +
                "\"target\":\"ffffffffffffffffffffffffffffffffffffffffffffffffffffffff00000000\"}," +
                "\"error\":null,\"id\":\"1\"}";

        System.out.println(test);

        InputStream inputStream = new ByteArrayInputStream(test.getBytes());

        Work work = workFactory.buildResponse(inputStream);

        System.out.println(work);
    }
}

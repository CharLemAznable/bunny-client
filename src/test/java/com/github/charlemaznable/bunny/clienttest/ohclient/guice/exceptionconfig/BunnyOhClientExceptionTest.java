package com.github.charlemaznable.bunny.clienttest.ohclient.guice.exceptionconfig;

import com.github.charlemaznable.bunny.client.guice.BunnyOhClientInjector;
import com.github.charlemaznable.bunny.clienttest.mock.BunnyClientExceptionConfig;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.n3r.diamond.client.impl.MockDiamondServer;

import static com.github.charlemaznable.bunny.clienttest.mock.MockOhClientServer.testExceptionServer;

public class BunnyOhClientExceptionTest {

    @BeforeAll
    public static void beforeAll() {
        MockDiamondServer.setUpMockServer();
        MockDiamondServer.setConfigInfo("BunnyClient", "exception",
                "httpServerBaseUrl=http://127.0.0.1:22116/exception\n" +
                        "eventBusAddressPrefix=/exception\n");
    }

    @AfterAll
    public static void afterAll() {
        MockDiamondServer.tearDownMockServer();
    }

    @Test
    public void testBunnyOhClientException() {
        val ohClientInjector = new BunnyOhClientInjector(BunnyClientExceptionConfig.class);
        val bunnyOhClient = ohClientInjector.getClient();
        testExceptionServer(bunnyOhClient);
    }
}

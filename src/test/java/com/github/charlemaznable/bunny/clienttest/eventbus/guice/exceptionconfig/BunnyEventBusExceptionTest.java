package com.github.charlemaznable.bunny.clienttest.eventbus.guice.exceptionconfig;

import com.github.charlemaznable.bunny.client.eventbus.BunnyEventBus;
import com.github.charlemaznable.bunny.client.guice.BunnyEventBusModular;
import com.github.charlemaznable.bunny.clienttest.mock.BunnyClientExceptionConfig;
import com.google.inject.Guice;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.n3r.diamond.client.impl.MockDiamondServer;

import static com.github.charlemaznable.bunny.clienttest.mock.MockEventBusConsumer.testExceptionConsumer;

@ExtendWith(VertxExtension.class)
public class BunnyEventBusExceptionTest {

    @BeforeAll
    public static void beforeAll() {
        MockDiamondServer.setUpMockServer();
        MockDiamondServer.setConfigInfo("BunnyClient", "exception", """
                httpServerBaseUrl=http://127.0.0.1:22116/exception
                eventBusAddressPrefix=/exception
                """);
    }

    @AfterAll
    public static void afterAll() {
        MockDiamondServer.tearDownMockServer();
    }

    @Test
    public void testBunnyEventBusException(Vertx vertx, VertxTestContext test) {
        val eventBusModular = new BunnyEventBusModular(BunnyClientExceptionConfig.class).vertx(vertx);
        val injector = Guice.createInjector(eventBusModular.createModule());
        val bunnyEventBus = injector.getInstance(BunnyEventBus.class);
        testExceptionConsumer(vertx, bunnyEventBus, test);
    }
}

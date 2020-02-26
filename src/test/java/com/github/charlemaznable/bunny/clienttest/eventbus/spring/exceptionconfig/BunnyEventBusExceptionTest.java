package com.github.charlemaznable.bunny.clienttest.eventbus.spring.exceptionconfig;

import com.github.charlemaznable.bunny.client.eventbus.BunnyEventBus;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.github.charlemaznable.bunny.clienttest.mock.MockEventBusConsumer.testExceptionConsumer;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ExtendWith({SpringExtension.class, VertxExtension.class})
@ContextConfiguration(classes = BunnyEventBusExceptionConfiguration.class)
public class BunnyEventBusExceptionTest {

    @Autowired
    private BunnyEventBus bunnyEventBus;

    @BeforeAll
    public static void beforeAll(Vertx vertx) {
        BunnyEventBusExceptionConfiguration.vertx = vertx;
    }

    @Test
    public void testBunnyEventBusException(Vertx vertx, VertxTestContext test) {
        testExceptionConsumer(vertx, bunnyEventBus, test);
    }
}

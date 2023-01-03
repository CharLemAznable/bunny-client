package com.github.charlemaznable.bunny.clienttest.eventbus.spring.defaultconfig;

import com.github.charlemaznable.bunny.client.eventbus.BunnyEventBus;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static com.github.charlemaznable.bunny.clienttest.mock.MockEventBusConsumer.testDefaultConsumer;

@ExtendWith(VertxExtension.class)
@SpringJUnitConfig(BunnyEventBusDefaultConfiguration.class)
public class BunnyEventBusDefaultTest {

    @Autowired
    private Vertx vertx;
    @Autowired
    private BunnyEventBus bunnyEventBus;

    @Test
    public void testBunnyEventBus(VertxTestContext test) {
        testDefaultConsumer(vertx, bunnyEventBus, test);
    }
}

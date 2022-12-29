package com.github.charlemaznable.bunny.clienttest.eventbus.spring.errorconfig;

import com.github.charlemaznable.bunny.client.eventbus.BunnyEventBus;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.github.charlemaznable.bunny.clienttest.mock.MockEventBusConsumer.testErrorConsumer;

@ExtendWith({SpringExtension.class, VertxExtension.class})
@ContextConfiguration(classes = BunnyEventBusErrorConfiguration.class)
public class BunnyEventBusErrorTest {

    @Autowired
    private Vertx vertx;
    @Autowired
    private BunnyEventBus bunnyEventBus;

    @Test
    public void testBunnyEventBusError(VertxTestContext test) {
        testErrorConsumer(vertx, bunnyEventBus, test);
    }
}

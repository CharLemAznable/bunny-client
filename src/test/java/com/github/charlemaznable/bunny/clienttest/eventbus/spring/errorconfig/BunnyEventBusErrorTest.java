package com.github.charlemaznable.bunny.clienttest.eventbus.spring.errorconfig;

import com.github.charlemaznable.bunny.client.eventbus.BunnyEventBus;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.github.charlemaznable.bunny.clienttest.mock.MockEventBusConsumer.testErrorConsumer;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ExtendWith({SpringExtension.class, VertxExtension.class})
@ContextConfiguration(classes = BunnyEventBusErrorConfiguration.class)
public class BunnyEventBusErrorTest {

    static Vertx vertx;

    @Bean
    public Vertx vertx() {
        return vertx;
    }

    @Autowired
    private BunnyEventBus bunnyEventBus;

    @BeforeAll
    public static void beforeAll(Vertx vertx) {
        BunnyEventBusErrorConfiguration.vertx = vertx;
    }

    @Test
    public void testBunnyEventBusError(Vertx vertx, VertxTestContext test) {
        testErrorConsumer(vertx, bunnyEventBus, test);
    }
}

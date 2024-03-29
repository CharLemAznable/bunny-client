package com.github.charlemaznable.bunny.clienttest.eventbus.guice.errorconfig;

import com.github.charlemaznable.bunny.client.eventbus.BunnyEventBus;
import com.github.charlemaznable.bunny.client.guice.BunnyEventBusModular;
import com.github.charlemaznable.bunny.clienttest.mock.BunnyClientErrorConfig;
import com.google.inject.Guice;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.github.charlemaznable.bunny.clienttest.mock.MockEventBusConsumer.testErrorConsumer;

@ExtendWith(VertxExtension.class)
public class BunnyEventBusErrorTest {

    @Test
    public void testBunnyEventBusError(Vertx vertx, VertxTestContext test) {
        val eventBusModular = new BunnyEventBusModular(new BunnyClientErrorConfig()).vertx(vertx);
        val injector = Guice.createInjector(eventBusModular.createModule());
        val bunnyEventBus = injector.getInstance(BunnyEventBus.class);
        testErrorConsumer(vertx, bunnyEventBus, test);
    }
}

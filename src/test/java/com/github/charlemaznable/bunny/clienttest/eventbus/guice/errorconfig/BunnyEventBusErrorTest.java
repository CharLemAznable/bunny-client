package com.github.charlemaznable.bunny.clienttest.eventbus.guice.errorconfig;

import com.github.charlemaznable.bunny.client.eventbus.BunnyEventBus;
import com.github.charlemaznable.bunny.client.guice.BunnyEventBusModular;
import com.github.charlemaznable.bunny.clienttest.mock.BunnyClientErrorConfig;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.util.Providers;
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
        val vertxModule = new AbstractModule() {
            @Override
            protected void configure() {
                bind(Vertx.class).toProvider(Providers.of(vertx));
            }
        };
        val eventBusModular = new BunnyEventBusModular(new BunnyClientErrorConfig());
        val injector = Guice.createInjector(eventBusModular.createModule(), vertxModule);
        val bunnyEventBus = injector.getInstance(BunnyEventBus.class);
        testErrorConsumer(vertx, bunnyEventBus, test);
    }
}

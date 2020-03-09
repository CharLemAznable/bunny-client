package com.github.charlemaznable.bunny.clienttest.eventbus.guice.defaultconfig;

import com.github.charlemaznable.bunny.client.eventbus.BunnyEventBus;
import com.github.charlemaznable.bunny.client.guice.BunnyEventBusModular;
import com.github.charlemaznable.core.context.FactoryContext;
import com.github.charlemaznable.core.guice.GuiceFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.util.Modules;
import com.google.inject.util.Providers;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.github.charlemaznable.bunny.clienttest.mock.MockEventBusConsumer.testDefaultConsumer;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(VertxExtension.class)
public class BunnyEventBusDefaultTest {

    @Test
    public void testBunnyEventBus(Vertx vertx, VertxTestContext test) {
        val vertxModule = new AbstractModule() {
            @Override
            protected void configure() {
                bind(Vertx.class).toProvider(Providers.of(vertx));
            }
        };
        val eventBusModular = new BunnyEventBusModular();
        val injector = Guice.createInjector(eventBusModular.createModule(), vertxModule);
        val bunnyEventBus = injector.getInstance(BunnyEventBus.class);
        testDefaultConsumer(vertx, bunnyEventBus, test);

        val emptyModular = new BunnyEventBusModular(Modules.EMPTY_MODULE);
        val guiceFactory = new GuiceFactory(Guice.createInjector(emptyModular.createModule(), vertxModule));
        FactoryContext.accept(guiceFactory, Vertx.class, internalVertx -> {
            assertSame(vertx, internalVertx);

            val emptyEventBus = new BunnyEventBus(internalVertx);
            testDefaultConsumer(vertx, emptyEventBus, test);
        });
    }
}

package com.github.charlemaznable.bunny.clienttest.eventbus.guice.defaultconfig;

import com.github.charlemaznable.bunny.client.eventbus.BunnyEventBus;
import com.github.charlemaznable.bunny.client.guice.BunnyEventBusInjector;
import com.github.charlemaznable.core.context.FactoryContext;
import com.github.charlemaznable.core.guice.InjectorFactory;
import com.google.inject.util.Modules;
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
        val eventBusInjector = new BunnyEventBusInjector(vertx);
        val injector = eventBusInjector.createInjector();
        val bunnyEventBus = injector.getInstance(BunnyEventBus.class);
        testDefaultConsumer(vertx, bunnyEventBus, test);

        val emptyInjector = new BunnyEventBusInjector(vertx, Modules.EMPTY_MODULE);
        val injectorFactory = new InjectorFactory(emptyInjector.createInjector());
        FactoryContext.accept(injectorFactory, Vertx.class, internalVertx -> {
            assertSame(vertx, internalVertx);

            val emptyEventBus = new BunnyEventBus(internalVertx);
            testDefaultConsumer(vertx, emptyEventBus, test);
        });
    }
}

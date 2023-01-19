package com.github.charlemaznable.bunny.clienttest.eventbus.guice.defaultconfig;

import com.github.charlemaznable.bunny.client.config.BunnyClientConfig;
import com.github.charlemaznable.bunny.client.eventbus.BunnyEventBus;
import com.github.charlemaznable.bunny.client.guice.BunnyEventBusModular;
import com.github.charlemaznable.core.codec.nonsense.NonsenseOptions;
import com.github.charlemaznable.core.codec.signature.SignatureOptions;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.util.Providers;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.github.charlemaznable.bunny.clienttest.mock.MockEventBusConsumer.testDefaultConsumer;

@ExtendWith(VertxExtension.class)
public class BunnyEventBusDefaultTest {

    @Test
    public void testBunnyEventBus(Vertx vertx, VertxTestContext test) {
        var eventBusModular = new BunnyEventBusModular().vertx(vertx);
        var injector = Guice.createInjector(eventBusModular.createModule());
        var bunnyEventBus = injector.getInstance(BunnyEventBus.class);
        testDefaultConsumer(vertx, bunnyEventBus, test);

        eventBusModular = new BunnyEventBusModular(new AbstractModule() {
            @Override
            protected void configure() {
                bind(BunnyClientConfig.class).toProvider(Providers.of(null));
            }
        }).nonsenseOptions(new NonsenseOptions()).signatureOptions(new SignatureOptions()).vertx(vertx);
        injector = Guice.createInjector(eventBusModular.createModule());
        bunnyEventBus = injector.getInstance(BunnyEventBus.class);
        testDefaultConsumer(vertx, bunnyEventBus, test);
    }
}

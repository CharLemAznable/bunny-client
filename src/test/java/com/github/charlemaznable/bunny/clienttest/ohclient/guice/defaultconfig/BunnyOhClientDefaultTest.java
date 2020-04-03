package com.github.charlemaznable.bunny.clienttest.ohclient.guice.defaultconfig;

import com.github.charlemaznable.bunny.client.config.BunnyClientConfig;
import com.github.charlemaznable.bunny.client.guice.BunnyOhClientModular;
import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClient;
import com.github.charlemaznable.core.codec.nonsense.NonsenseOptions;
import com.github.charlemaznable.core.codec.signature.SignatureOptions;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.util.Providers;
import lombok.var;
import org.junit.jupiter.api.Test;

import static com.github.charlemaznable.bunny.clienttest.mock.MockOhClientServer.testDefaultServer;

public class BunnyOhClientDefaultTest {

    @Test
    public void testBunnyOhClient() {
        var ohClientModular = new BunnyOhClientModular();
        var injector = Guice.createInjector(ohClientModular.createModule());
        var bunnyOhClient = injector.getInstance(BunnyOhClient.class);
        testDefaultServer(bunnyOhClient);

        ohClientModular = new BunnyOhClientModular(new AbstractModule() {
            @Override
            protected void configure() {
                bind(BunnyClientConfig.class).toProvider(Providers.of(null));
            }
        }).nonsenseOptions(new NonsenseOptions()).signatureOptions(new SignatureOptions());
        injector = Guice.createInjector(ohClientModular.createModule());
        bunnyOhClient = injector.getInstance(BunnyOhClient.class);
        testDefaultServer(bunnyOhClient);
    }
}

package com.github.charlemaznable.bunny.clienttest.ohclient.guice.defaultconfig;

import com.github.charlemaznable.bunny.client.guice.BunnyOhClientInjector;
import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClient;
import com.github.charlemaznable.core.guice.InjectorFactory;
import com.google.inject.util.Modules;
import lombok.val;
import org.junit.jupiter.api.Test;

import static com.github.charlemaznable.bunny.clienttest.mock.MockOhClientServer.testDefaultServer;

public class BunnyOhClientDefaultTest {

    @Test
    public void testBunnyOhClient() {
        val ohClientInjector = new BunnyOhClientInjector();
        val injector = ohClientInjector.createInjector();
        val bunnyOhClient = injector.getInstance(BunnyOhClient.class);
        testDefaultServer(bunnyOhClient);

        val emptyInjector = new BunnyOhClientInjector(Modules.EMPTY_MODULE);
        val injectorFactory = new InjectorFactory(emptyInjector.createInjector());
        val emptyOhClient = injectorFactory.build(BunnyOhClient.class);
        testDefaultServer(emptyOhClient);
    }
}

package com.github.charlemaznable.bunny.clienttest.ohclient.guice.defaultconfig;

import com.github.charlemaznable.bunny.client.guice.BunnyOhClientModular;
import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClient;
import com.github.charlemaznable.core.guice.GuiceFactory;
import com.google.inject.Guice;
import com.google.inject.util.Modules;
import lombok.val;
import org.junit.jupiter.api.Test;

import static com.github.charlemaznable.bunny.clienttest.mock.MockOhClientServer.testDefaultServer;

public class BunnyOhClientDefaultTest {

    @Test
    public void testBunnyOhClient() {
        val ohClientModular = new BunnyOhClientModular();
        val injector = Guice.createInjector(ohClientModular.createModule());
        val bunnyOhClient = injector.getInstance(BunnyOhClient.class);
        testDefaultServer(bunnyOhClient);

        val emptyModular = new BunnyOhClientModular(Modules.EMPTY_MODULE);
        val guiceFactory = new GuiceFactory(Guice.createInjector(emptyModular.createModule()));
        val emptyOhClient = guiceFactory.build(BunnyOhClient.class);
        testDefaultServer(emptyOhClient);
    }
}

package com.github.charlemaznable.bunny.clienttest.ohclient.guice.errorconfig;

import com.github.charlemaznable.bunny.client.guice.BunnyOhClientModular;
import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClient;
import com.github.charlemaznable.bunny.clienttest.mock.BunnyClientErrorConfig;
import com.google.inject.Guice;
import lombok.val;
import org.junit.jupiter.api.Test;

import static com.github.charlemaznable.bunny.clienttest.mock.MockOhClientServer.testErrorServer;

public class BunnyOhClientErrorTest {

    @Test
    public void testBunnyOhClientError() {
        val ohClientModular = new BunnyOhClientModular(new BunnyClientErrorConfig());
        val injector = Guice.createInjector(ohClientModular.createModule());
        val bunnyOhClient = injector.getInstance(BunnyOhClient.class);
        testErrorServer(bunnyOhClient);
    }
}

package com.github.charlemaznable.bunny.clienttest.ohclient.spring.errorconfig;

import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static com.github.charlemaznable.bunny.clienttest.mock.MockOhClientServer.testErrorServer;

@SpringJUnitConfig(BunnyOhClientErrorConfiguration.class)
public class BunnyOhClientErrorTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private BunnyOhClient bunnyOhClient;

    @Test
    public void testBunnyOhClientError() {
        testErrorServer(bunnyOhClient);
    }
}

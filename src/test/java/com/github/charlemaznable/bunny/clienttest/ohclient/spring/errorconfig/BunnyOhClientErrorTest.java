package com.github.charlemaznable.bunny.clienttest.ohclient.spring.errorconfig;

import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.github.charlemaznable.bunny.clienttest.mock.MockOhClientServer.testErrorServer;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BunnyOhClientErrorConfiguration.class)
public class BunnyOhClientErrorTest {

    @Autowired
    private BunnyOhClient bunnyOhClient;

    @Test
    public void testBunnyOhClientError() {
        testErrorServer(bunnyOhClient);
    }
}

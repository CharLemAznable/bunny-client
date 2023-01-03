package com.github.charlemaznable.bunny.clienttest.ohclient.spring.wrapperconfig;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static com.github.charlemaznable.bunny.clienttest.mock.MockOhClientServer.testDefaultServer;

@SpringJUnitConfig(BunnyOhClientWrapperConfiguration.class)
public class BunnyOhClientWrapperTest {

    @Autowired
    private BunnyOhClientWrapper bunnyOhClientWrapper;

    @Test
    public void testBunnyOhClient() {
        testDefaultServer(bunnyOhClientWrapper.bunnyOhClient());
    }
}

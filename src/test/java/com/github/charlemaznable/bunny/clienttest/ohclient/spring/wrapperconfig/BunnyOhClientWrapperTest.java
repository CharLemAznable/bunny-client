package com.github.charlemaznable.bunny.clienttest.ohclient.spring.wrapperconfig;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.github.charlemaznable.bunny.clienttest.mock.MockOhClientServer.testDefaultServer;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BunnyOhClientWrapperConfiguration.class)
public class BunnyOhClientWrapperTest {

    @Autowired
    private BunnyOhClientWrapper bunnyOhClientWrapper;

    @Test
    public void testBunnyOhClient() {
        testDefaultServer(bunnyOhClientWrapper.bunnyOhClient());
    }
}

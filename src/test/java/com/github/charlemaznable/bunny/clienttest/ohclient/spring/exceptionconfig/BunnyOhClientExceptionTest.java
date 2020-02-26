package com.github.charlemaznable.bunny.clienttest.ohclient.spring.exceptionconfig;

import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.github.charlemaznable.bunny.clienttest.mock.MockOhClientServer.testExceptionServer;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BunnyOhClientExceptionConfiguration.class)
public class BunnyOhClientExceptionTest {

    @Autowired
    private BunnyOhClient bunnyOhClient;

    @Test
    public void testBunnyOhClientException() {
        testExceptionServer(bunnyOhClient);
    }
}

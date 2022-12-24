package com.github.charlemaznable.bunny.clienttest.eventbus.spring.exceptionconfig;

import com.github.charlemaznable.bunny.client.spring.BunnyEventBusImport;
import com.github.charlemaznable.bunny.clienttest.mock.BunnyClientExceptionConfig;
import com.github.charlemaznable.configservice.diamond.DiamondScan;
import com.github.charlemaznable.core.vertx.spring.VertxImport;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.n3r.diamond.client.impl.MockDiamondServer;

import static com.github.charlemaznable.configservice.diamond.DiamondFactory.diamondLoader;
import static com.github.charlemaznable.core.spring.SpringFactory.springFactory;
import static com.github.charlemaznable.httpclient.ohclient.OhFactory.springOhLoader;
import static org.joor.Reflect.on;

@DiamondScan(basePackageClasses = BunnyClientExceptionConfig.class)
@VertxImport
@BunnyEventBusImport
public class BunnyEventBusExceptionConfiguration {

    @PostConstruct
    public void postConstruct() {
        on(diamondLoader(springFactory())).field("configCache").call("invalidateAll");
        on(springOhLoader()).field("ohCache").call("invalidateAll");
        MockDiamondServer.setUpMockServer();
        MockDiamondServer.setConfigInfo("BunnyClient", "exception", """
                httpServerBaseUrl=http://127.0.0.1:22116/exception
                eventBusAddressPrefix=/exception
                """);
    }

    @PreDestroy
    public void preDestroy() {
        MockDiamondServer.tearDownMockServer();
    }
}


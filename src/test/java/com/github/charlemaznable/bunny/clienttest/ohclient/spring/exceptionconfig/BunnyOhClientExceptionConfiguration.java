package com.github.charlemaznable.bunny.clienttest.ohclient.spring.exceptionconfig;

import com.github.charlemaznable.bunny.client.spring.BunnyOhClientImport;
import com.github.charlemaznable.bunny.clienttest.mock.BunnyClientExceptionConfig;
import com.github.charlemaznable.configservice.diamond.DiamondScan;
import org.n3r.diamond.client.impl.MockDiamondServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static com.github.charlemaznable.configservice.diamond.DiamondFactory.diamondLoader;
import static com.github.charlemaznable.core.spring.SpringFactory.springFactory;
import static com.github.charlemaznable.httpclient.ohclient.OhFactory.springOhLoader;
import static org.joor.Reflect.on;

@DiamondScan(basePackageClasses = BunnyClientExceptionConfig.class)
@BunnyOhClientImport
public class BunnyOhClientExceptionConfiguration {

    @PostConstruct
    public void postConstruct() {
        on(diamondLoader(springFactory())).field("configCache").call("invalidateAll");
        on(springOhLoader()).field("ohCache").call("invalidateAll");
        MockDiamondServer.setUpMockServer();
        MockDiamondServer.setConfigInfo("BunnyClient", "exception",
                "httpServerBaseUrl=http://127.0.0.1:22116/exception\n" +
                        "eventBusAddressPrefix=/exception\n");
    }

    @PreDestroy
    public void preDestroy() {
        MockDiamondServer.tearDownMockServer();
    }
}

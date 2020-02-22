package com.github.charlemaznable.bunny.clienttest.vertx.defaultconfig;

import com.github.charlemaznable.bunny.client.vertx.VertxBunnyImport;
import com.github.charlemaznable.core.miner.MinerFactory;
import com.github.charlemaznable.core.net.ohclient.OhFactory;
import org.n3r.diamond.client.impl.MockDiamondServer;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.joor.Reflect.onClass;

@VertxBunnyImport
@Configuration
public class BunnyEventBusDefaultConfiguration {

    @PostConstruct
    public void postConstruct() {
        onClass(OhFactory.class).field("ohCache").call("invalidateAll");
        onClass(MinerFactory.class).field("minerCache").call("invalidateAll");
        MockDiamondServer.setUpMockServer();
    }

    @PreDestroy
    public void preDestroy() {
        MockDiamondServer.tearDownMockServer();
    }
}

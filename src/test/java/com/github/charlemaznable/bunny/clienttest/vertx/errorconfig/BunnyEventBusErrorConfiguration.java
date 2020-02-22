package com.github.charlemaznable.bunny.clienttest.vertx.errorconfig;

import com.github.charlemaznable.bunny.client.config.BunnyClientConfig;
import com.github.charlemaznable.bunny.client.vertx.VertxBunnyImport;
import com.github.charlemaznable.core.miner.MinerFactory;
import com.github.charlemaznable.core.net.ohclient.OhFactory;
import org.n3r.diamond.client.impl.MockDiamondServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.joor.Reflect.onClass;

@VertxBunnyImport
@Configuration
public class BunnyEventBusErrorConfiguration {

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

    @Bean
    public BunnyClientConfig bunnyClientConfig() {
        return new BunnyClientConfig() {
            @Override
            public String httpServerBaseUrl() {
                return "http://127.0.0.1:22115/bunny";
            }

            @Override
            public String eventBusAddressPrefix() {
                return "/rabbit";
            }
        };
    }
}


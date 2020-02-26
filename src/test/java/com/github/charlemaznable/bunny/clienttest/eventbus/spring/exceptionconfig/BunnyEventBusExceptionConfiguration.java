package com.github.charlemaznable.bunny.clienttest.eventbus.spring.exceptionconfig;

import com.github.charlemaznable.bunny.client.spring.BunnyEventBusImport;
import com.github.charlemaznable.bunny.clienttest.mock.BunnyClientExceptionConfig;
import com.github.charlemaznable.core.miner.MinerScan;
import io.vertx.core.Vertx;
import org.n3r.diamond.client.impl.MockDiamondServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static com.github.charlemaznable.core.miner.MinerFactory.springMinerLoader;
import static com.github.charlemaznable.core.net.ohclient.OhFactory.springOhLoader;
import static org.joor.Reflect.on;

@Configuration
@MinerScan(basePackageClasses = BunnyClientExceptionConfig.class)
@BunnyEventBusImport
public class BunnyEventBusExceptionConfiguration {

    static Vertx vertx;

    @Bean
    public Vertx vertx() {
        return vertx;
    }

    @PostConstruct
    public void postConstruct() {
        on(springMinerLoader()).field("minerCache").call("invalidateAll");
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


package com.github.charlemaznable.bunny.clienttest.eventbus.spring.defaultconfig;

import com.github.charlemaznable.bunny.client.spring.BunnyEventBusImport;
import io.vertx.core.Vertx;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

import static com.github.charlemaznable.core.miner.MinerFactory.springMinerLoader;
import static com.github.charlemaznable.core.net.ohclient.OhFactory.springOhLoader;
import static org.joor.Reflect.on;

@Configuration
@BunnyEventBusImport
public class BunnyEventBusDefaultConfiguration {

    static Vertx vertx;

    @Bean
    public Vertx vertx() {
        return vertx;
    }

    @PostConstruct
    public void postConstruct() {
        on(springMinerLoader()).field("minerCache").call("invalidateAll");
        on(springOhLoader()).field("ohCache").call("invalidateAll");
    }
}

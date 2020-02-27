package com.github.charlemaznable.bunny.clienttest.eventbus.spring.errorconfig;

import com.github.charlemaznable.bunny.client.config.BunnyClientConfig;
import com.github.charlemaznable.bunny.client.spring.BunnyEventBusImport;
import com.github.charlemaznable.bunny.clienttest.mock.BunnyClientErrorConfig;
import com.github.charlemaznable.core.vertx.spring.SpringVertxImport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

import static com.github.charlemaznable.core.miner.MinerFactory.springMinerLoader;
import static com.github.charlemaznable.core.net.ohclient.OhFactory.springOhLoader;
import static org.joor.Reflect.on;

@Configuration
@SpringVertxImport
@BunnyEventBusImport
public class BunnyEventBusErrorConfiguration {

    @PostConstruct
    public void postConstruct() {
        on(springMinerLoader()).field("minerCache").call("invalidateAll");
        on(springOhLoader()).field("ohCache").call("invalidateAll");
    }

    @Bean
    public BunnyClientConfig bunnyClientConfig() {
        return new BunnyClientErrorConfig();
    }
}


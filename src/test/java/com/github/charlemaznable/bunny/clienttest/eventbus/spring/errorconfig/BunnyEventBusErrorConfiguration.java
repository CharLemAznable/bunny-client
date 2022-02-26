package com.github.charlemaznable.bunny.clienttest.eventbus.spring.errorconfig;

import com.github.charlemaznable.bunny.client.config.BunnyClientConfig;
import com.github.charlemaznable.bunny.client.spring.BunnyEventBusImport;
import com.github.charlemaznable.bunny.clienttest.mock.BunnyClientErrorConfig;
import com.github.charlemaznable.core.vertx.spring.VertxImport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

import static com.github.charlemaznable.httpclient.ohclient.OhFactory.springOhLoader;
import static com.github.charlemaznable.miner.MinerFactory.springMinerLoader;
import static org.n3r.eql.joor.Reflect.on;

@Configuration
@VertxImport
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


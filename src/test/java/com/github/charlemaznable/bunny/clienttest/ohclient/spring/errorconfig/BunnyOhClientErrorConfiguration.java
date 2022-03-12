package com.github.charlemaznable.bunny.clienttest.ohclient.spring.errorconfig;

import com.github.charlemaznable.bunny.client.config.BunnyClientConfig;
import com.github.charlemaznable.bunny.client.spring.BunnyOhClientImport;
import com.github.charlemaznable.bunny.clienttest.mock.BunnyClientErrorConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

import static com.github.charlemaznable.configservice.diamond.DiamondFactory.diamondLoader;
import static com.github.charlemaznable.core.spring.SpringFactory.springFactory;
import static com.github.charlemaznable.httpclient.ohclient.OhFactory.springOhLoader;
import static org.joor.Reflect.on;

@Configuration
@BunnyOhClientImport
public class BunnyOhClientErrorConfiguration {

    @PostConstruct
    public void postConstruct() {
        on(diamondLoader(springFactory())).field("configCache").call("invalidateAll");
        on(springOhLoader()).field("ohCache").call("invalidateAll");
    }

    @Bean
    public BunnyClientConfig bunnyClientConfig() {
        return new BunnyClientErrorConfig();
    }
}

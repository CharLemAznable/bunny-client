package com.github.charlemaznable.bunny.client.vertx;

import io.vertx.core.Vertx;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings("SpringFacetCodeInspection")
@Configuration
public class VertxBunnyBeans {

    @Bean
    public Vertx vertx() {
        return Vertx.vertx();
    }
}

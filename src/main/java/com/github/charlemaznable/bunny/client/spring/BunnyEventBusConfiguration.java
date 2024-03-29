package com.github.charlemaznable.bunny.client.spring;

import com.github.charlemaznable.bunny.client.config.BunnyClientConfig;
import com.github.charlemaznable.bunny.client.eventbus.BunnyEventBus;
import com.github.charlemaznable.core.codec.nonsense.NonsenseOptions;
import com.github.charlemaznable.core.codec.signature.SignatureOptions;
import com.github.charlemaznable.core.spring.ElvesImport;
import io.vertx.core.Vertx;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

@Configuration
@ElvesImport
public class BunnyEventBusConfiguration {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean("com.github.charlemaznable.bunny.client.eventbus.BunnyEventBus")
    public BunnyEventBus bunnyEventBus(Vertx vertx,
                                       @Nullable BunnyClientConfig bunnyClientConfig,
                                       @Nullable NonsenseOptions nonsenseOptions,
                                       @Nullable SignatureOptions signatureOptions) {
        return new BunnyEventBus(vertx, bunnyClientConfig, nonsenseOptions, signatureOptions);
    }
}

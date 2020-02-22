package com.github.charlemaznable.bunny.client.spring;

import com.github.charlemaznable.bunny.client.config.BunnyClientConfig;
import com.github.charlemaznable.core.net.common.Mapping.UrlProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.github.charlemaznable.core.lang.Condition.checkNotNull;
import static com.github.charlemaznable.core.miner.MinerFactory.getMiner;

@Component
public final class BunnyHttpClientUrlProvider implements UrlProvider {

    private final BunnyClientConfig bunnyClientConfig;

    @Autowired(required = false)
    public BunnyHttpClientUrlProvider() {
        this(getMiner(BunnyClientConfig.class));
    }

    @Autowired(required = false)
    public BunnyHttpClientUrlProvider(BunnyClientConfig bunnyClientConfig) {
        this.bunnyClientConfig = checkNotNull(bunnyClientConfig);
    }

    @Override
    public String url(Class<?> clazz) {
        return checkNotNull(bunnyClientConfig.httpServerBaseUrl());
    }
}

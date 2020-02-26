package com.github.charlemaznable.bunny.client.ohclient;

import com.github.charlemaznable.bunny.client.config.BunnyClientConfig;
import com.github.charlemaznable.core.net.common.Mapping.UrlProvider;
import com.google.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

import static com.github.charlemaznable.core.lang.Condition.checkNotNull;
import static com.github.charlemaznable.core.lang.Condition.nullThen;
import static com.github.charlemaznable.core.miner.MinerFactory.getMiner;

@Component
public final class BunnyOhClientUrlProvider implements UrlProvider {

    private final BunnyClientConfig bunnyClientConfig;

    public BunnyOhClientUrlProvider() {
        this(null);
    }

    @Inject
    @Autowired
    public BunnyOhClientUrlProvider(@Nullable BunnyClientConfig bunnyClientConfig) {
        this.bunnyClientConfig = nullThen(bunnyClientConfig,
                () -> getMiner(BunnyClientConfig.class));
    }

    @Override
    public String url(Class<?> clazz) {
        return checkNotNull(bunnyClientConfig.httpServerBaseUrl());
    }
}

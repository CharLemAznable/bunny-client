package com.github.charlemaznable.bunny.client.ohclient;

import com.github.charlemaznable.bunny.client.config.BunnyClientConfig;
import com.github.charlemaznable.httpclient.common.Mapping.UrlProvider;
import com.google.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

import static com.github.charlemaznable.configservice.ConfigFactory.getConfig;
import static com.github.charlemaznable.core.lang.Condition.checkNotNull;
import static com.github.charlemaznable.core.lang.Condition.nullThen;

@Component
public final class BunnyOhClientUrlProvider implements UrlProvider {

    private final BunnyClientConfig bunnyClientConfig;

    @Inject
    @Autowired
    public BunnyOhClientUrlProvider(@Nullable BunnyClientConfig bunnyClientConfig) {
        this.bunnyClientConfig = nullThen(bunnyClientConfig,
                () -> getConfig(BunnyClientConfig.class));
    }

    @Override
    public String url(Class<?> clazz) {
        return checkNotNull(bunnyClientConfig.httpServerBaseUrl());
    }
}

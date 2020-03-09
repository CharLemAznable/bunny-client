package com.github.charlemaznable.bunny.client.guice;

import com.github.charlemaznable.bunny.client.config.BunnyClientConfig;
import com.github.charlemaznable.core.miner.MinerModular;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.util.Providers;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BunnyEventBusModular {

    private Module configModule;

    public BunnyEventBusModular() {
        this((BunnyClientConfig) null);
    }

    public BunnyEventBusModular(Class<? extends BunnyClientConfig> configClass) {
        this(new MinerModular().createModule(configClass));
    }

    public BunnyEventBusModular(BunnyClientConfig configImpl) {
        this(new AbstractModule() {
            @Override
            protected void configure() {
                bind(BunnyClientConfig.class).toProvider(Providers.of(configImpl));
            }
        });
    }

    public Module createModule() {
        return configModule;
    }
}

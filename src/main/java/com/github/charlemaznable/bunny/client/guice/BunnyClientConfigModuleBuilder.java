package com.github.charlemaznable.bunny.client.guice;

import com.github.charlemaznable.bunny.client.config.BunnyClientConfig;
import com.github.charlemaznable.core.miner.MinerInjector;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.util.Providers;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class BunnyClientConfigModuleBuilder {

    private Module configModule;

    public BunnyClientConfigModuleBuilder() {
        this((BunnyClientConfig) null);
    }

    public BunnyClientConfigModuleBuilder(Class<? extends BunnyClientConfig> configClass) {
        this(new MinerInjector().createModule(configClass));
    }

    public BunnyClientConfigModuleBuilder(BunnyClientConfig configImpl) {
        this(new AbstractModule() {
            @Override
            protected void configure() {
                bind(BunnyClientConfig.class).toProvider(Providers.of(configImpl));
            }
        });
    }

    public Module build() {
        return this.configModule;
    }
}

package com.github.charlemaznable.bunny.client.guice;

import com.github.charlemaznable.bunny.client.config.BunnyClientConfig;
import com.github.charlemaznable.core.miner.MinerInjector;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.util.Providers;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class BunnyConfigModuleBuilder {

    private Module configModule;

    public BunnyConfigModuleBuilder() {
        this((BunnyClientConfig) null);
    }

    public BunnyConfigModuleBuilder(Class<? extends BunnyClientConfig> configClass) {
        this(new MinerInjector().createModule(configClass));
    }

    public BunnyConfigModuleBuilder(BunnyClientConfig configImpl) {
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

package com.github.charlemaznable.bunny.client.guice;

import com.github.charlemaznable.bunny.client.config.BunnyClientConfig;
import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClient;
import com.github.charlemaznable.core.miner.MinerInjector;
import com.github.charlemaznable.core.net.ohclient.OhInjector;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Providers;

public class BunnyOhClientInjector {

    private OhInjector ohInjector;

    public BunnyOhClientInjector() {
        this((BunnyClientConfig) null);
    }

    public BunnyOhClientInjector(Class<? extends BunnyClientConfig> configClass) {
        this(new MinerInjector().createModule(configClass));
    }

    public BunnyOhClientInjector(BunnyClientConfig configImpl) {
        this(new AbstractModule() {
            @Override
            protected void configure() {
                bind(BunnyClientConfig.class).toProvider(Providers.of(configImpl));
            }
        });
    }

    public BunnyOhClientInjector(Module configModule) {
        this.ohInjector = new OhInjector(configModule);
    }

    public Module createModule() {
        return this.ohInjector.createModule(BunnyOhClient.class);
    }

    public Injector createInjector() {
        return Guice.createInjector(createModule());
    }

    public BunnyOhClient getClient() {
        return this.ohInjector.getClient(BunnyOhClient.class);
    }
}

package com.github.charlemaznable.bunny.client.guice;

import com.github.charlemaznable.bunny.client.config.BunnyClientConfig;
import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClient;
import com.github.charlemaznable.core.miner.MinerModular;
import com.github.charlemaznable.core.net.ohclient.OhModular;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.util.Providers;

public final class BunnyOhClientModular {

    private OhModular ohModular;

    public BunnyOhClientModular() {
        this((BunnyClientConfig) null);
    }

    public BunnyOhClientModular(Class<? extends BunnyClientConfig> configClass) {
        this(new MinerModular().bindClasses(configClass).createModule());
    }

    public BunnyOhClientModular(BunnyClientConfig configImpl) {
        this(new AbstractModule() {
            @Override
            protected void configure() {
                bind(BunnyClientConfig.class).toProvider(Providers.of(configImpl));
            }
        });
    }

    public BunnyOhClientModular(Module configModule) {
        this.ohModular = new OhModular(configModule).bindClasses(BunnyOhClient.class);
    }

    public Module createModule() {
        return this.ohModular.createModule();
    }

    public BunnyOhClient getClient() {
        return this.ohModular.getClient(BunnyOhClient.class);
    }
}

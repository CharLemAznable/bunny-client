package com.github.charlemaznable.bunny.client.guice;

import com.github.charlemaznable.bunny.client.config.BunnyClientConfig;
import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClient;
import com.github.charlemaznable.core.net.ohclient.OhModular;
import com.google.inject.Module;

public final class BunnyOhClientModular extends AbstractBunnyModular<BunnyOhClientModular> {

    public BunnyOhClientModular() {
        super();
    }

    public BunnyOhClientModular(Class<? extends BunnyClientConfig> configClass) {
        super(configClass);
    }

    public BunnyOhClientModular(BunnyClientConfig configImpl) {
        super(configImpl);
    }

    public BunnyOhClientModular(Module configModule) {
        super(configModule);
    }

    @Override
    public Module createModule() {
        return new OhModular(super.createModule())
                .bindClasses(BunnyOhClient.class).createModule();
    }

    public BunnyOhClient getClient() {
        return new OhModular(super.createModule()).getClient(BunnyOhClient.class);
    }
}

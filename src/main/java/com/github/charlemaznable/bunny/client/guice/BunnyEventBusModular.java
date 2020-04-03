package com.github.charlemaznable.bunny.client.guice;

import com.github.charlemaznable.bunny.client.config.BunnyClientConfig;
import com.google.inject.Module;

public final class BunnyEventBusModular extends AbstractBunnyModular<BunnyEventBusModular> {

    public BunnyEventBusModular() {
        super();
    }

    public BunnyEventBusModular(Class<? extends BunnyClientConfig> configClass) {
        super(configClass);
    }

    public BunnyEventBusModular(BunnyClientConfig configImpl) {
        super(configImpl);
    }

    public BunnyEventBusModular(Module configModule) {
        super(configModule);
    }
}

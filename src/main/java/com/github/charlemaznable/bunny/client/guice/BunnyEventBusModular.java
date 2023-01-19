package com.github.charlemaznable.bunny.client.guice;

import com.github.charlemaznable.bunny.client.config.BunnyClientConfig;
import com.github.charlemaznable.bunny.client.eventbus.BunnyEventBus;
import com.github.charlemaznable.core.codec.nonsense.NonsenseOptions;
import com.github.charlemaznable.core.codec.signature.SignatureOptions;
import com.github.charlemaznable.core.guice.Modulee;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.util.Providers;
import io.vertx.core.Vertx;

import javax.annotation.Nullable;

import static com.github.charlemaznable.core.lang.Condition.checkNotNull;

public final class BunnyEventBusModular extends AbstractBunnyModular<BunnyEventBusModular> {

    private Module vertxModule;

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

    public BunnyEventBusModular vertx(Vertx vertx) {
        return vertxModule(new AbstractModule() {
            @Override
            protected void configure() {
                bind(Vertx.class).toProvider(Providers.of(vertx));
            }
        });
    }

    public BunnyEventBusModular vertxModule(Module vertxModule) {
        this.vertxModule = vertxModule;
        return this;
    }

    public Module createModule() {
        return Modulee.combine(this.configModule, checkNotNull(vertxModule),
                new AbstractModule() {
                    @Provides
                    public BunnyEventBus bunnyEventBus(Vertx vertx,
                                                       @Nullable BunnyClientConfig bunnyClientConfig,
                                                       @Nullable NonsenseOptions nonsenseOptions,
                                                       @Nullable SignatureOptions signatureOptions) {
                        return new BunnyEventBus(vertx, bunnyClientConfig, nonsenseOptions, signatureOptions);
                    }
                });
    }
}

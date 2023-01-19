package com.github.charlemaznable.bunny.client.guice;

import com.github.charlemaznable.bunny.client.config.BunnyClientConfig;
import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClient;
import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClientContentFormatter;
import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClientResponseParser;
import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClientUrlProvider;
import com.github.charlemaznable.core.codec.nonsense.NonsenseOptions;
import com.github.charlemaznable.core.codec.signature.SignatureOptions;
import com.github.charlemaznable.httpclient.ohclient.OhModular;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import org.springframework.lang.Nullable;

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

    public Module createModule() {
        return new OhModular(this.configModule, new AbstractModule() {

            @Provides
            public BunnyOhClientUrlProvider bunnyOhClientUrlProvider(@Nullable BunnyClientConfig bunnyClientConfig) {
                return new BunnyOhClientUrlProvider(bunnyClientConfig);
            }

            @Provides
            public BunnyOhClientContentFormatter bunnyOhClientContentFormatter(@Nullable NonsenseOptions nonsenseOptions,
                                                                               @Nullable SignatureOptions signatureOptions) {
                return new BunnyOhClientContentFormatter(nonsenseOptions, signatureOptions);
            }

            @Provides
            public BunnyOhClientResponseParser bunnyOhClientResponseParser(@Nullable NonsenseOptions nonsenseOptions,
                                                                           @Nullable SignatureOptions signatureOptions) {
                return new BunnyOhClientResponseParser(nonsenseOptions, signatureOptions);
            }
        }).bindClasses(BunnyOhClient.class).createModule();
    }
}

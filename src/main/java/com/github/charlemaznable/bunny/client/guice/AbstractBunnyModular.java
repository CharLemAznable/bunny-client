package com.github.charlemaznable.bunny.client.guice;

import com.github.charlemaznable.bunny.client.config.BunnyClientConfig;
import com.github.charlemaznable.configservice.ConfigModular;
import com.github.charlemaznable.core.codec.nonsense.NonsenseOptions;
import com.github.charlemaznable.core.codec.signature.SignatureOptions;
import com.github.charlemaznable.core.guice.Modulee;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.util.Providers;

@SuppressWarnings("unchecked")
public abstract class AbstractBunnyModular<T extends AbstractBunnyModular<T>> {

    protected final Module configModule;
    private NonsenseOptions nonsenseOptions;
    private SignatureOptions signatureOptions;

    public AbstractBunnyModular() {
        this((BunnyClientConfig) null);
    }

    public AbstractBunnyModular(Class<? extends BunnyClientConfig> configClass) {
        this(new ConfigModular().bindClasses(configClass).createModule());
    }

    public AbstractBunnyModular(BunnyClientConfig configImpl) {
        this(new AbstractModule() {
            @Override
            protected void configure() {
                bind(BunnyClientConfig.class).toProvider(Providers.of(configImpl));
            }
        });
    }

    public AbstractBunnyModular(Module configModule) {
        this.configModule = Modulee.combine(configModule, new AbstractModule() {
            @Override
            protected void configure() {
                bind(NonsenseOptions.class).toProvider(Providers.of(nonsenseOptions));
                bind(SignatureOptions.class).toProvider(Providers.of(signatureOptions));
            }
        });
    }

    public T nonsenseOptions(NonsenseOptions nonsenseOptions) {
        this.nonsenseOptions = nonsenseOptions;
        return (T) this;
    }

    public T signatureOptions(SignatureOptions signatureOptions) {
        this.signatureOptions = signatureOptions;
        return (T) this;
    }
}

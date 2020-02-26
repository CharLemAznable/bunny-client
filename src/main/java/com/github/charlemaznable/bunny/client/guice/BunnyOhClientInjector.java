package com.github.charlemaznable.bunny.client.guice;

import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClient;
import com.github.charlemaznable.core.net.ohclient.OhInjector;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class BunnyOhClientInjector {

    private OhInjector ohInjector;

    public BunnyOhClientInjector() {
        this(new BunnyConfigModuleBuilder());
    }

    public BunnyOhClientInjector(BunnyConfigModuleBuilder configModuleBuilder) {
        this(configModuleBuilder.build());
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

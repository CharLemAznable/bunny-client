package com.github.charlemaznable.bunny.client.guice;

import com.github.charlemaznable.bunny.client.eventbus.BunnyEventBus;
import com.github.charlemaznable.core.guice.Modulee;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Providers;
import io.vertx.core.Vertx;

public class BunnyEventBusInjector {

    private Module combinedModule;

    public BunnyEventBusInjector(Vertx vertx) {
        this(vertx, new BunnyConfigModuleBuilder());
    }

    public BunnyEventBusInjector(Vertx vertx, BunnyConfigModuleBuilder configModuleBuilder) {
        this(vertx, configModuleBuilder.build());
    }

    public BunnyEventBusInjector(Vertx vertx, Module configModule) {
        this.combinedModule = Modulee.combine(new AbstractModule() {
            @Override
            protected void configure() {
                bind(Vertx.class).toProvider(Providers.of(vertx));
            }
        }, configModule);
    }

    public Module createModule() {
        return this.combinedModule;
    }

    public Injector createInjector() {
        return Guice.createInjector(createModule());
    }

    public BunnyEventBus getEventBus() {
        return createInjector().getInstance(BunnyEventBus.class);
    }
}

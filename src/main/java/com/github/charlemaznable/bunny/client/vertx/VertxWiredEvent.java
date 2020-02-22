package com.github.charlemaznable.bunny.client.vertx;

import io.vertx.core.Vertx;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

public final class VertxWiredEvent extends ApplicationContextEvent {

    private static final long serialVersionUID = -9184240752364053971L;

    @Getter
    @Accessors(fluent = true)
    private final transient Vertx vertx;

    public VertxWiredEvent(ApplicationContext source, Vertx vertx) {
        super(source);
        this.vertx = vertx;
    }
}

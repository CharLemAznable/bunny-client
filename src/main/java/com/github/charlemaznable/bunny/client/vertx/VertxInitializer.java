package com.github.charlemaznable.bunny.client.vertx;

import com.github.charlemaznable.core.spring.SpringContext;
import io.vertx.core.Vertx;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public final class VertxInitializer {

    @Getter
    @Accessors(fluent = true)
    private final Vertx vertx;

    public VertxInitializer() {
        this.vertx = Vertx.vertx(); // clusteredVertx handler
    }

    @EventListener
    public void autowireVertx(ContextRefreshedEvent event) {
        SpringContext.autowireBean(vertx);

        val applicationContext = event.getApplicationContext();
        applicationContext.publishEvent(
                new VertxWiredEvent(applicationContext, vertx));
    }
}


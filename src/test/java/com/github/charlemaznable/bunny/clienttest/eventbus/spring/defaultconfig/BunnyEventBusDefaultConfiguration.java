package com.github.charlemaznable.bunny.clienttest.eventbus.spring.defaultconfig;

import com.github.charlemaznable.bunny.client.spring.BunnyEventBusImport;
import com.github.charlemaznable.core.vertx.spring.VertxImport;

import javax.annotation.PostConstruct;

import static com.github.charlemaznable.httpclient.ohclient.OhFactory.springOhLoader;
import static com.github.charlemaznable.miner.MinerFactory.springMinerLoader;
import static org.n3r.eql.joor.Reflect.on;

@VertxImport
@BunnyEventBusImport
public class BunnyEventBusDefaultConfiguration {

    @PostConstruct
    public void postConstruct() {
        on(springMinerLoader()).field("minerCache").call("invalidateAll");
        on(springOhLoader()).field("ohCache").call("invalidateAll");
    }
}

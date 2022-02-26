package com.github.charlemaznable.bunny.clienttest.ohclient.spring.defaultconfig;

import com.github.charlemaznable.bunny.client.spring.BunnyOhClientImport;

import javax.annotation.PostConstruct;

import static com.github.charlemaznable.httpclient.ohclient.OhFactory.springOhLoader;
import static com.github.charlemaznable.miner.MinerFactory.springMinerLoader;
import static org.joor.Reflect.on;

@BunnyOhClientImport
public class BunnyOhClientDefaultConfiguration {

    @PostConstruct
    public void postConstruct() {
        on(springMinerLoader()).field("minerCache").call("invalidateAll");
        on(springOhLoader()).field("ohCache").call("invalidateAll");
    }
}

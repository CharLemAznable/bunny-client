package com.github.charlemaznable.bunny.clienttest.ohclient.spring.defaultconfig;

import com.github.charlemaznable.bunny.client.spring.BunnyOhClientImport;
import jakarta.annotation.PostConstruct;

import static com.github.charlemaznable.configservice.diamond.DiamondFactory.diamondLoader;
import static com.github.charlemaznable.core.spring.SpringFactory.springFactory;
import static com.github.charlemaznable.httpclient.ohclient.OhFactory.springOhLoader;
import static org.joor.Reflect.on;

@BunnyOhClientImport
public class BunnyOhClientDefaultConfiguration {

    @PostConstruct
    public void postConstruct() {
        on(diamondLoader(springFactory())).field("configCache").call("invalidateAll");
        on(springOhLoader()).field("ohCache").call("invalidateAll");
    }
}

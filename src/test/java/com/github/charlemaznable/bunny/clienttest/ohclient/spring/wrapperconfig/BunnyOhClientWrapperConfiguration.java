package com.github.charlemaznable.bunny.clienttest.ohclient.spring.wrapperconfig;

import com.github.charlemaznable.core.spring.ElvesImport;
import com.github.charlemaznable.core.spring.NeoComponentScan;
import jakarta.annotation.PostConstruct;

import static com.github.charlemaznable.configservice.diamond.DiamondFactory.diamondLoader;
import static com.github.charlemaznable.core.spring.SpringFactory.springFactory;
import static com.github.charlemaznable.httpclient.ohclient.OhFactory.springOhLoader;
import static org.joor.Reflect.on;

@ElvesImport
@NeoComponentScan
public class BunnyOhClientWrapperConfiguration {

    @PostConstruct
    public void postConstruct() {
        on(diamondLoader(springFactory())).field("configCache").call("invalidateAll");
        on(springOhLoader()).field("ohCache").call("invalidateAll");
    }
}

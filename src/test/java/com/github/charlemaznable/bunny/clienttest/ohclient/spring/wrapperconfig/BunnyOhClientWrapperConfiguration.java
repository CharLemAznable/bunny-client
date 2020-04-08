package com.github.charlemaznable.bunny.clienttest.ohclient.spring.wrapperconfig;

import com.github.charlemaznable.core.spring.ComplexComponentScan;
import com.github.charlemaznable.core.spring.ComplexImport;
import com.github.charlemaznable.core.spring.SpringContext;

import javax.annotation.PostConstruct;

import static com.github.charlemaznable.core.miner.MinerFactory.springMinerLoader;
import static com.github.charlemaznable.core.net.ohclient.OhFactory.springOhLoader;
import static org.joor.Reflect.on;
import static org.joor.Reflect.onClass;

@ComplexImport
@ComplexComponentScan
public class BunnyOhClientWrapperConfiguration {

    @PostConstruct
    public void postConstruct() {
        onClass(SpringContext.class).set("applicationContext", null);
        on(springMinerLoader()).field("minerCache").call("invalidateAll");
        on(springOhLoader()).field("ohCache").call("invalidateAll");
    }
}

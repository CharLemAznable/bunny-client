package com.github.charlemaznable.bunny.clienttest.ohclient.spring.wrapperconfig;

import com.github.charlemaznable.core.spring.ElvesImport;
import com.github.charlemaznable.core.spring.NeoComponentScan;

import javax.annotation.PostConstruct;

import static com.github.charlemaznable.httpclient.ohclient.OhFactory.springOhLoader;
import static com.github.charlemaznable.miner.MinerFactory.springMinerLoader;
import static org.n3r.eql.joor.Reflect.on;

@ElvesImport
@NeoComponentScan
public class BunnyOhClientWrapperConfiguration {

    @PostConstruct
    public void postConstruct() {
        on(springMinerLoader()).field("minerCache").call("invalidateAll");
        on(springOhLoader()).field("ohCache").call("invalidateAll");
    }
}

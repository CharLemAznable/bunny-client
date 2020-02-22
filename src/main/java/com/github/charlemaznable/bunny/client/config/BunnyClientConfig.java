package com.github.charlemaznable.bunny.client.config;

import com.github.charlemaznable.core.miner.MinerConfig;

@MinerConfig(group = "BunnyClient", dataId = "default")
public interface BunnyClientConfig {

    @MinerConfig(defaultValue = "http://127.0.0.1:22114/bunny")
    String httpServerBaseUrl();

    @MinerConfig(defaultValue = "/bunny")
    String eventBusAddressPrefix();
}

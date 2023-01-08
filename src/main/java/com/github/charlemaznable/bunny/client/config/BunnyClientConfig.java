package com.github.charlemaznable.bunny.client.config;

import com.github.charlemaznable.configservice.Config;

@Config(keyset = "BunnyClient", key = "${bunny-client-config:-default}")
public interface BunnyClientConfig {

    @Config(defaultValue = "http://127.0.0.1:22114/bunny")
    String httpServerBaseUrl();

    @Config(defaultValue = "/bunny")
    String eventBusAddressPrefix();
}

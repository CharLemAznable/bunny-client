package com.github.charlemaznable.bunny.clienttest.mock;

import com.github.charlemaznable.bunny.client.config.BunnyClientConfig;

public class BunnyClientErrorConfig implements BunnyClientConfig {

    @Override
    public String httpServerBaseUrl() {
        return "http://127.0.0.1:22115/error";
    }

    @Override
    public String eventBusAddressPrefix() {
        return "/error";
    }
}

package com.github.charlemaznable.bunny.client.config;

import com.github.charlemaznable.configservice.apollo.ApolloConfig;
import com.github.charlemaznable.configservice.diamond.DiamondConfig;

@ApolloConfig(namespace = "BunnyClient", propertyName = "${bunny-client-config:-default}")
@DiamondConfig(group = "BunnyClient", dataId = "${bunny-client-config:-default}")
public interface BunnyClientConfig {

    @DiamondConfig(defaultValue = "http://127.0.0.1:22114/bunny")
    String httpServerBaseUrl();

    @DiamondConfig(defaultValue = "/bunny")
    String eventBusAddressPrefix();
}

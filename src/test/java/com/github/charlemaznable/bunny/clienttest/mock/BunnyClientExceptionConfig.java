package com.github.charlemaznable.bunny.clienttest.mock;

import com.github.charlemaznable.bunny.client.config.BunnyClientConfig;
import com.github.charlemaznable.configservice.diamond.DiamondConfig;

@DiamondConfig(group = "BunnyClient", dataId = "exception")
public interface BunnyClientExceptionConfig extends BunnyClientConfig {
}

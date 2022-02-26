package com.github.charlemaznable.bunny.client.spring;

import com.github.charlemaznable.bunny.client.eventbus.BunnyEventBus;
import com.github.charlemaznable.core.spring.ElvesImport;
import com.github.charlemaznable.core.spring.NeoComponentScan;

@ElvesImport
@NeoComponentScan(basePackageClasses = BunnyEventBus.class)
public final class BunnyEventBusComponents {
}

package com.github.charlemaznable.bunny.client.spring;

import com.github.charlemaznable.bunny.client.eventbus.BunnyEventBus;
import com.github.charlemaznable.core.spring.ComplexComponentScan;
import com.github.charlemaznable.core.spring.ComplexImport;

@ComplexImport
@ComplexComponentScan(basePackageClasses = BunnyEventBus.class)
public final class BunnyEventBusComponents {
}

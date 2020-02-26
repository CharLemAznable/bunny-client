package com.github.charlemaznable.bunny.client.spring;

import com.github.charlemaznable.bunny.client.eventbus.BunnyEventBusScanAnchor;
import com.github.charlemaznable.core.spring.ComplexComponentScan;
import com.github.charlemaznable.core.spring.ComplexImport;

@ComplexImport
@ComplexComponentScan(basePackageClasses = BunnyEventBusScanAnchor.class)
public final class BunnyEventBusComponents {
}

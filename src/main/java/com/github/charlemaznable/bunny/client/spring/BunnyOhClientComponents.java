package com.github.charlemaznable.bunny.client.spring;

import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClientScanAnchor;
import com.github.charlemaznable.core.net.ohclient.OhScan;
import com.github.charlemaznable.core.spring.ComplexComponentScan;
import com.github.charlemaznable.core.spring.ComplexImport;

@ComplexImport
@ComplexComponentScan(basePackageClasses = BunnyOhClientScanAnchor.class)
@OhScan(basePackageClasses = BunnyOhClientScanAnchor.class)
public final class BunnyOhClientComponents {
}

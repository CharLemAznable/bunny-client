package com.github.charlemaznable.bunny.client.spring;

import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClient;
import com.github.charlemaznable.core.net.ohclient.OhScan;
import com.github.charlemaznable.core.spring.ComplexComponentScan;
import com.github.charlemaznable.core.spring.ComplexImport;

@ComplexImport
@ComplexComponentScan(basePackageClasses = BunnyOhClient.class)
@OhScan(basePackageClasses = BunnyOhClient.class)
public final class BunnyOhClientComponents {
}

package com.github.charlemaznable.bunny.client.spring;

import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClient;
import com.github.charlemaznable.core.spring.ElvesImport;
import com.github.charlemaznable.core.spring.NeoComponentScan;
import com.github.charlemaznable.httpclient.ohclient.OhScan;

@ElvesImport
@NeoComponentScan(basePackageClasses = BunnyOhClient.class)
@OhScan(basePackageClasses = BunnyOhClient.class)
public final class BunnyOhClientComponents {
}

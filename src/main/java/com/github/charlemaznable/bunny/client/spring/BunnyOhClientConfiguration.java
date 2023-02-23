package com.github.charlemaznable.bunny.client.spring;

import com.github.charlemaznable.bunny.client.config.BunnyClientConfig;
import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClient;
import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClientConfigurer;
import com.github.charlemaznable.core.codec.nonsense.NonsenseOptions;
import com.github.charlemaznable.core.codec.signature.SignatureOptions;
import com.github.charlemaznable.core.spring.ElvesImport;
import com.github.charlemaznable.httpclient.ohclient.OhScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.lang.Nullable;

@Configuration
@ElvesImport
@OhScan(basePackageClasses = BunnyOhClient.class,
        includeFilters = {@ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {BunnyOhClient.class}
        )})
public class BunnyOhClientConfiguration {

    @Bean("com.github.charlemaznable.bunny.client.ohclient.BunnyOhClientConfigurer")
    public BunnyOhClientConfigurer bunnyOhClientConfigurer(@Nullable BunnyClientConfig bunnyClientConfig,
                                                           @Nullable NonsenseOptions nonsenseOptions,
                                                           @Nullable SignatureOptions signatureOptions) {
        return new BunnyOhClientConfigurer(bunnyClientConfig, nonsenseOptions, signatureOptions);
    }
}

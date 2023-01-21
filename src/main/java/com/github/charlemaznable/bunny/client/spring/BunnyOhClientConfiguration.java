package com.github.charlemaznable.bunny.client.spring;

import com.github.charlemaznable.bunny.client.config.BunnyClientConfig;
import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClient;
import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClientContentFormatter;
import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClientResponseParser;
import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClientUrlProvider;
import com.github.charlemaznable.core.codec.nonsense.NonsenseOptions;
import com.github.charlemaznable.core.codec.signature.SignatureOptions;
import com.github.charlemaznable.core.spring.ElvesImport;
import com.github.charlemaznable.httpclient.ohclient.OhScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

@Configuration
@ElvesImport
@OhScan(basePackageClasses = BunnyOhClient.class)
public class BunnyOhClientConfiguration {

    @Bean("com.github.charlemaznable.bunny.client.ohclient.BunnyOhClientUrlProvider")
    public BunnyOhClientUrlProvider bunnyOhClientUrlProvider(@Nullable BunnyClientConfig bunnyClientConfig) {
        return new BunnyOhClientUrlProvider(bunnyClientConfig);
    }

    @Bean("com.github.charlemaznable.bunny.client.ohclient.BunnyOhClientContentFormatter")
    public BunnyOhClientContentFormatter bunnyOhClientContentFormatter(@Nullable NonsenseOptions nonsenseOptions,
                                                                       @Nullable SignatureOptions signatureOptions) {
        return new BunnyOhClientContentFormatter(nonsenseOptions, signatureOptions);
    }

    @Bean("com.github.charlemaznable.bunny.client.ohclient.BunnyOhClientResponseParser")
    public BunnyOhClientResponseParser bunnyOhClientResponseParser(@Nullable NonsenseOptions nonsenseOptions,
                                                                   @Nullable SignatureOptions signatureOptions) {
        return new BunnyOhClientResponseParser(nonsenseOptions, signatureOptions);
    }
}

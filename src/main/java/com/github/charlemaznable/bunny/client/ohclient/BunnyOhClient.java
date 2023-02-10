package com.github.charlemaznable.bunny.client.ohclient;

import com.github.charlemaznable.bunny.client.domain.BunnyBaseRequest;
import com.github.charlemaznable.bunny.client.domain.BunnyBaseResponse;
import com.github.charlemaznable.httpclient.common.Bundle;
import com.github.charlemaznable.httpclient.common.ConfigureWith;
import com.github.charlemaznable.httpclient.common.Mapping;
import com.github.charlemaznable.httpclient.ohclient.OhClient;

@OhClient
@ConfigureWith(BunnyOhClientConfigurer.class)
public interface BunnyOhClient {

    @Mapping("{bunny-address}")
    <T extends BunnyBaseResponse> T request(@Bundle BunnyBaseRequest<T> request);
}

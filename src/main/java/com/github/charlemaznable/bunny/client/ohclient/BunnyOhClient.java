package com.github.charlemaznable.bunny.client.ohclient;

import com.github.charlemaznable.bunny.client.domain.BunnyBaseRequest;
import com.github.charlemaznable.bunny.client.domain.BunnyBaseResponse;
import com.github.charlemaznable.core.net.common.Bundle;
import com.github.charlemaznable.core.net.common.ContentFormat;
import com.github.charlemaznable.core.net.common.HttpMethod;
import com.github.charlemaznable.core.net.common.Mapping;
import com.github.charlemaznable.core.net.common.RequestMethod;
import com.github.charlemaznable.core.net.common.ResponseParse;
import com.github.charlemaznable.core.net.ohclient.OhClient;

@OhClient
@Mapping(urlProvider = BunnyOhClientUrlProvider.class)
@RequestMethod(HttpMethod.POST)
@ContentFormat(BunnyOhClientContentFormatter.class)
@ResponseParse(BunnyOhClientResponseParser.class)
public interface BunnyOhClient {

    @Mapping("{bunny-address}")
    <T extends BunnyBaseResponse> T request(@Bundle BunnyBaseRequest<T> request);
}

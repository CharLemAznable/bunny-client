package com.github.charlemaznable.bunny.client.ohclient;

import com.github.charlemaznable.bunny.client.domain.BunnyBaseRequest;
import com.github.charlemaznable.bunny.client.domain.BunnyBaseResponse;
import com.github.charlemaznable.httpclient.common.Bundle;
import com.github.charlemaznable.httpclient.common.ContentFormat;
import com.github.charlemaznable.httpclient.common.HttpMethod;
import com.github.charlemaznable.httpclient.common.Mapping;
import com.github.charlemaznable.httpclient.common.RequestMethod;
import com.github.charlemaznable.httpclient.common.ResponseParse;
import com.github.charlemaznable.httpclient.ohclient.OhClient;

@OhClient
@Mapping(urlProvider = BunnyOhClientUrlProvider.class)
@RequestMethod(HttpMethod.POST)
@ContentFormat(BunnyOhClientContentFormatter.class)
@ResponseParse(BunnyOhClientResponseParser.class)
public interface BunnyOhClient {

    @Mapping("{bunny-address}")
    <T extends BunnyBaseResponse> T request(@Bundle BunnyBaseRequest<T> request);
}

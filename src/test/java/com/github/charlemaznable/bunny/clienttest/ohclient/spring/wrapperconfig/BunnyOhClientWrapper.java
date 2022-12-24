package com.github.charlemaznable.bunny.clienttest.ohclient.spring.wrapperconfig;

import com.github.charlemaznable.bunny.client.domain.BunnyBaseRequest;
import com.github.charlemaznable.bunny.client.domain.BunnyBaseResponse;
import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClient;
import com.github.charlemaznable.httpclient.common.Bundle;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

import static com.github.charlemaznable.core.lang.Condition.nullThen;
import static com.github.charlemaznable.httpclient.ohclient.OhFactory.getClient;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Component
public class BunnyOhClientWrapper {

    @Getter
    @Accessors(fluent = true)
    private final BunnyOhClient bunnyOhClient;

    @Autowired
    public BunnyOhClientWrapper(@Nullable BunnyOhClient bunnyOhClient) {
        this.bunnyOhClient = nullThen(bunnyOhClient, () -> getClient(BunnyOhClient.class));
    }

    public <T extends BunnyBaseResponse> T request(@Bundle BunnyBaseRequest<T> request) {
        return this.bunnyOhClient.request(request);
    }
}

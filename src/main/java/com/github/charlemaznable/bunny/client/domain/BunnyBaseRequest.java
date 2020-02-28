package com.github.charlemaznable.bunny.client.domain;

import com.github.charlemaznable.core.net.common.CncRequest;
import com.github.charlemaznable.core.net.common.PathVar;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BunnyBaseRequest<T extends BunnyBaseResponse> implements CncRequest<T> {

    @PathVar("bunny-address")
    protected String bunnyAddress;
    /**
     * 计费类型, 如: 短信/流量
     */
    private String chargingType;
}

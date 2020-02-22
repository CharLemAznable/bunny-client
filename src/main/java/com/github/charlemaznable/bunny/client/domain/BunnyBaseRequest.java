package com.github.charlemaznable.bunny.client.domain;

import com.github.charlemaznable.core.net.common.CncRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BunnyBaseRequest<T extends BunnyBaseResponse> implements CncRequest<T> {

    /**
     * 计费类型, 如: 短信/流量
     */
    private String chargingType;
}

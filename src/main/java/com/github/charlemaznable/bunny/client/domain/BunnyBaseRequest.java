package com.github.charlemaznable.bunny.client.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.charlemaznable.core.net.common.CncRequest;
import com.github.charlemaznable.core.net.common.PathVar;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

import static com.github.charlemaznable.core.lang.Mapp.newHashMap;

@Getter
public abstract class BunnyBaseRequest<T extends BunnyBaseResponse> implements CncRequest<T> {

    @JSONField(serialize = false, deserialize = false)
    @PathVar("bunny-address")
    protected String bunnyAddress;
    /**
     * 计费类型, 如: 短信/流量
     */
    @Setter
    private String chargingType;
    /**
     * 可选扩展参数
     */
    private Map<String, String> extend = newHashMap();
}

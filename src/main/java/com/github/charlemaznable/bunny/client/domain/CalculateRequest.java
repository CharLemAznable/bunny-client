package com.github.charlemaznable.bunny.client.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 计费接口 请求
 */
@Getter
@Setter
public class CalculateRequest extends BunnyBaseRequest<CalculateResponse> {

    /**
     * 计费参数, 依据计费类型选择计费插件, 由插件解析
     */
    private Map<String, Object> chargingParameters;

    public CalculateRequest() {
        this.bunnyAddress = BunnyAddress.CALCULATE;
    }

    @Override
    public Class<? extends CalculateResponse> responseClass() {
        return CalculateResponse.class;
    }
}

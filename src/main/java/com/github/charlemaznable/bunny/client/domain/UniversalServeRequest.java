package com.github.charlemaznable.bunny.client.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 服务接口 通用请求
 */
@Getter
@Setter
public class UniversalServeRequest extends ServeRequest<Map<String, Object>, Map<String, Object>> {

    @Override
    public Class<? extends ServeResponse<Map<String, Object>>> responseClass() {
        return UniversalServeResponse.class;
    }
}

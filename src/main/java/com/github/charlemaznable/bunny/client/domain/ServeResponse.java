package com.github.charlemaznable.bunny.client.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 服务接口 响应
 */
@Getter
@Setter
public class ServeResponse extends BunnyBaseResponse {

    /**
     * 服务类型
     */
    private String serveType;
    /**
     * 服务响应
     */
    private Map<String, Object> internalResponse;
    /**
     * 非期望的内部异常信息
     */
    private String unexpectedFailure;
}

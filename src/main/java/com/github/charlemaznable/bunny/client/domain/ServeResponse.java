package com.github.charlemaznable.bunny.client.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 服务接口 响应
 */
@Getter
@Setter
public class ServeResponse<U> extends BunnyBaseResponse {

    /**
     * 服务类型
     */
    private String serveType;
    /**
     * 服务响应
     */
    private U internalResponse;
    /**
     * 非期望的内部异常信息
     */
    private String unexpectedFailure;
}

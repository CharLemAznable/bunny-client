package com.github.charlemaznable.bunny.client.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 服务结果回调接口 响应
 */
@Getter
@Setter
public class ServeCallbackResponse extends BunnyBaseResponse {

    /**
     * 服务类型
     */
    private String serveType;
}

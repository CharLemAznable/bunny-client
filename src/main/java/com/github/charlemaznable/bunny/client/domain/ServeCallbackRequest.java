package com.github.charlemaznable.bunny.client.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 服务结果回调接口 请求
 */
@Getter
@Setter
public class ServeCallbackRequest extends BunnyBaseRequest<ServeCallbackResponse> {

    /**
     * 服务请求
     */
    private Map<String, Object> internalRequest;
    /**
     * 服务接口调用流水号
     */
    private String seqId;

    public ServeCallbackRequest() {
        this.bunnyAddress = BunnyAddress.SERVE_CALLBACK;
    }

    @Override
    public Class<? extends ServeCallbackResponse> responseClass() {
        return ServeCallbackResponse.class;
    }
}

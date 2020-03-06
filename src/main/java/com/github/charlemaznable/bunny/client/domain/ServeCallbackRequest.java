package com.github.charlemaznable.bunny.client.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.val;

/**
 * 服务结果回调接口 请求
 */
@Getter
@Setter
public abstract class ServeCallbackRequest<T>
        extends BunnyBaseRequest<ServeCallbackResponse> {

    /**
     * 服务类型
     */
    private String serveType;
    /**
     * 服务请求
     */
    private T internalRequest;
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

    @Override
    public ServeCallbackResponse createResponse() {
        val serveResponse = super.createResponse();
        serveResponse.setServeType(this.serveType);
        return serveResponse;
    }
}

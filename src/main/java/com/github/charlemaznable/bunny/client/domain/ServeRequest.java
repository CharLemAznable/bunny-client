package com.github.charlemaznable.bunny.client.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.util.Map;

/**
 * 服务接口 请求
 */
@Getter
@Setter
public class ServeRequest extends BunnyBaseRequest<ServeResponse> {

    /**
     * 扣费计量, 表示本次需扣减的服务计量, 如短信条数/流量数值
     * 如为空, 则使用计费插件进行计算
     */
    private Integer paymentValue;
    /**
     * 计费参数, 依据计费类型选择计费插件, 由插件解析
     * 如扣费计量不为空, 则忽略
     */
    private Map<String, String> chargingParameters;
    /**
     * 服务类型
     */
    private String serveType;
    /**
     * 服务请求
     */
    private Map<String, Object> internalRequest;
    /**
     * 服务结果回调地址
     */
    private String callbackUrl;

    public ServeRequest() {
        this.bunnyAddress = BunnyAddress.SERVE;
    }

    @Override
    public Class<ServeResponse> responseClass() {
        return ServeResponse.class;
    }

    @Override
    public ServeResponse createResponse() {
        val serveResponse = super.createResponse();
        serveResponse.setServeType(this.serveType);
        return serveResponse;
    }
}

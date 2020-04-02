package com.github.charlemaznable.bunny.client.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 服务接口 请求
 */
@Getter
@Setter
public class ServeRequest extends BunnyBaseRequest<ServeResponse> {

    /**
     * 扣费计量, 表示本次需扣减的服务计量, 如短信条数/流量数值
     * 如为空, 则使用计费插件进行计算, 计费参数使用服务请求字段内容
     */
    private Integer paymentValue;
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
    public Class<? extends ServeResponse> responseClass() {
        return ServeResponse.class;
    }
}

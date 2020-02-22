package com.github.charlemaznable.bunny.client.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 预扣费接口 响应
 */
@Getter
@Setter
public class PaymentAdvanceResponse extends BunnyBaseResponse {

    /**
     * 预扣费流水号, 用于扣费确认/扣费回退接口参数
     */
    private String paymentId;
    /**
     * 扣费计量, 表示预扣减的服务计量, 如短信条数/流量数值
     */
    private String paymentValue;
    /**
     * 余额计量, 表示当前服务余额的计量, 如短信条数/流量数值
     */
    private String balance;
    /**
     * 计量单位, 表示服务计量的单位, 如条/兆字节
     */
    private String unit;
}

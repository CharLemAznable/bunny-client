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
}

package com.github.charlemaznable.bunny.client.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 预扣费接口 请求
 */
@Getter
@Setter
public class PaymentAdvanceRequest extends BunnyBaseRequest<PaymentAdvanceResponse> {

    /**
     * 扣费计量, 表示预扣减的服务计量, 如短信条数/流量数值
     */
    private String paymentValue;

    public PaymentAdvanceRequest() {
        this.bunnyAddress = BunnyAddress.PAYMENT_ADVANCE;
    }

    @Override
    public Class<PaymentAdvanceResponse> getResponseClass() {
        return PaymentAdvanceResponse.class;
    }
}

package com.github.charlemaznable.bunny.client.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 扣费回退接口 请求
 */
@Getter
@Setter
public class PaymentRollbackRequest extends BunnyBaseRequest<PaymentRollbackResponse> {

    /**
     * 预扣费流水号, 由预扣费接口返回
     */
    private String paymentId;

    public PaymentRollbackRequest() {
        this.bunnyAddress = BunnyAddress.PAYMENT_ROLLBACK;
    }

    @Override
    public Class<PaymentRollbackResponse> responseClass() {
        return PaymentRollbackResponse.class;
    }
}

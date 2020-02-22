package com.github.charlemaznable.bunny.client.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 扣费确认接口 请求
 */
@Getter
@Setter
public class PaymentCommitRequest extends BunnyBaseRequest<PaymentCommitResponse> {

    /**
     * 预扣费流水号, 由预扣费接口返回
     */
    private String paymentId;

    @Override
    public Class<PaymentCommitResponse> getResponseClass() {
        return PaymentCommitResponse.class;
    }
}

package com.github.charlemaznable.bunny.client.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 预扣费接口 请求
 */
@Getter
@Setter
public class PaymentAdvanceRequest extends BunnyBaseRequest<PaymentAdvanceResponse> {

    /**
     * 计费参数, 依据计费类型选择计费插件, 由插件解析
     * 按插件计算的费用计量, 进行预扣减
     */
    private Map<String, String> chargingParameters;

    public PaymentAdvanceRequest() {
        this.bunnyAddress = BunnyAddress.PAYMENT_ADVANCE;
    }

    @Override
    public Class<PaymentAdvanceResponse> getResponseClass() {
        return PaymentAdvanceResponse.class;
    }
}

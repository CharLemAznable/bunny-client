package com.github.charlemaznable.bunny.client.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 充值接口 请求
 */
@Getter
@Setter
public class ChargeRequest extends BunnyBaseRequest<ChargeResponse> {

    /**
     * 充值计量, 表示需充值服务的计量, 如短信条数/流量数值
     */
    private String chargeValue;

    public ChargeRequest() {
        this.bunnyAddress = BunnyAddress.CHARGE;
    }

    @Override
    public Class<ChargeResponse> getResponseClass() {
        return ChargeResponse.class;
    }
}

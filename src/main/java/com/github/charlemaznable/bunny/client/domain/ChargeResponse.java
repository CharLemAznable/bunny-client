package com.github.charlemaznable.bunny.client.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 充值接口 响应
 */
@Getter
@Setter
public class ChargeResponse extends BunnyBaseResponse {

    /**
     * 余额计量, 表示当前服务余额的计量, 如短信条数/流量数值
     */
    private String balance;
    /**
     * 计量单位, 表示服务计量的单位, 如条/兆字节
     */
    private String balanceUnit;
}

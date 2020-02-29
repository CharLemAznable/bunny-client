package com.github.charlemaznable.bunny.client.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 扣费确认接口 响应
 */
@Getter
@Setter
public class PaymentCommitResponse extends BunnyBaseResponse {

    /**
     * 扣费计量, 表示确认扣减的服务计量, 如短信条数/流量数值
     */
    private String commit;
    /**
     * 计量单位, 表示服务计量的单位, 如条/兆字节
     */
    private String unit;
}

package com.github.charlemaznable.bunny.client.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 计费接口 响应
 */
@Getter
@Setter
public class CalculateResponse extends BunnyBaseResponse {

    /**
     * 费用计量, 表示需使用服务的计量, 如短信条数/流量数值
     */
    private int calculate;
}

package com.github.charlemaznable.bunny.client.domain;

import com.github.charlemaznable.core.net.common.CncResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BunnyBaseResponse implements CncResponse {

    public static final String RESP_CODE_KEY = "respCode";
    public static final String RESP_CODE_OK = "OK";
    public static final String RESP_DESC_KEY = "respDesc";
    public static final String RESP_DESC_SUCCESS = "SUCCESS";

    /**
     * 计费类型, 如: 短信/流量
     */
    private String chargingType;
    /**
     * 响应编码
     */
    private String respCode;
    /**
     * 响应描述
     */
    private String respDesc;

    public boolean isSuccess() {
        return RESP_CODE_OK.equals(respCode);
    }
}

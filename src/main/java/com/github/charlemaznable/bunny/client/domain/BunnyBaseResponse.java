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
     * 响应编码
     */
    private String respCode;
    /**
     * 响应描述
     */
    private String respDesc;
    /**
     * 服务名称
     */
    private String serveName;

    public boolean isSuccess() {
        return RESP_CODE_OK.equals(respCode);
    }

    public void succeed() {
        fillResp(RESP_CODE_OK, RESP_DESC_SUCCESS);
    }

    public void failed(String respCode, String respDesc) {
        fillResp(respCode, respDesc);
    }

    public void failed(BunnyException exception) {
        fillResp(exception.respCode(), exception.respDesc());
    }

    private void fillResp(String respCode, String respDesc) {
        this.setRespCode(respCode);
        this.setRespDesc(respDesc);
    }
}

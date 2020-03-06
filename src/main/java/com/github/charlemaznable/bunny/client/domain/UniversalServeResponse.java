package com.github.charlemaznable.bunny.client.domain;

import com.github.charlemaznable.bunny.client.domain.UniversalServeResponse.InternalResponse;
import com.github.charlemaznable.core.net.common.CncResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
 * 服务接口 通用响应
 */
@Getter
@Setter
public class UniversalServeResponse extends ServeResponse<InternalResponse> {

    public static class InternalResponse
            extends HashMap<String, Object> implements CncResponse {

        private static final long serialVersionUID = -4058312074652769236L;
    }
}

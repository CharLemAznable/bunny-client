package com.github.charlemaznable.bunny.client.domain;

import com.github.charlemaznable.bunny.client.domain.UniversalServeRequest.InternalRequest;
import com.github.charlemaznable.bunny.client.domain.UniversalServeResponse.InternalResponse;
import com.github.charlemaznable.core.net.common.CncRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
 * 服务接口 通用请求
 */
@Getter
@Setter
public class UniversalServeRequest extends ServeRequest<InternalRequest, InternalResponse> {

    @Override
    public Class<? extends ServeResponse<InternalResponse>> responseClass() {
        return UniversalServeResponse.class;
    }

    public static class InternalRequest
            extends HashMap<String, Object> implements CncRequest<InternalResponse> {

        private static final long serialVersionUID = -8510932200175563809L;

        @Override
        public Class<? extends InternalResponse> responseClass() {
            return InternalResponse.class;
        }
    }
}

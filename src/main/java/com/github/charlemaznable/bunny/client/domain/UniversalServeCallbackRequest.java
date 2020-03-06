package com.github.charlemaznable.bunny.client.domain;

import com.github.charlemaznable.bunny.client.domain.UniversalServeCallbackRequest.InternalRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
 * 服务结果回调接口 通用请求
 */
@Getter
@Setter
public class UniversalServeCallbackRequest extends ServeCallbackRequest<InternalRequest> {

    public static class InternalRequest extends HashMap<String, Object> {

        private static final long serialVersionUID = -2945488488814709689L;
    }
}

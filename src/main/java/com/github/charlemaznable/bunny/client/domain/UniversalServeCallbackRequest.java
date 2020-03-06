package com.github.charlemaznable.bunny.client.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 服务结果回调接口 通用请求
 */
@Getter
@Setter
public class UniversalServeCallbackRequest extends ServeCallbackRequest<Map<String, Object>> {
}

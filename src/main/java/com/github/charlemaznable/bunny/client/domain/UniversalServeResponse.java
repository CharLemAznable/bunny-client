package com.github.charlemaznable.bunny.client.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 服务接口 通用响应
 */
@Getter
@Setter
public class UniversalServeResponse extends ServeResponse<Map<String, Object>> {
}

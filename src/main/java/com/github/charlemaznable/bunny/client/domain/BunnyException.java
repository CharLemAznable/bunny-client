package com.github.charlemaznable.bunny.client.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.Serial;

import static com.github.charlemaznable.core.codec.Json.jsonOf;

@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public class BunnyException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2780989237523433110L;
    private final String respCode; // 错误编码
    private final String respDesc; // 错误描述

    @Override
    public String getMessage() {
        return jsonOf("respCode", respCode, "respDesc", respDesc);
    }
}

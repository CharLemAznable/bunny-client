package com.github.charlemaznable.bunny.client.domain;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class BunnyAddress {

    public static final String CALCULATE = "/calculate";
    public static final String CHARGE = "/charge";
    public static final String QUERY = "/query";
    public static final String SERVE = "/serve";
    public static final String SERVE_CALLBACK = "/serve-callback";
}

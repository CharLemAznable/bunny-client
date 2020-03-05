package com.github.charlemaznable.bunny.client.domain;

public final class BunnyAddress {

    public static final String CALCULATE = "/calculate";
    public static final String CHARGE = "/charge";
    public static final String QUERY = "/query";
    public static final String SERVE = "/serve";
    public static final String SERVE_CALLBACK = "/serve-callback";

    private BunnyAddress() {
        throw new UnsupportedOperationException();
    }
}

package com.github.charlemaznable.bunny.client.domain;

public final class BunnyAddress {

    public static final String CALCULATE = "/calculate";
    public static final String CHARGE = "/charge";
    public static final String PAYMENT_ADVANCE = "/payment/advance";
    public static final String PAYMENT_COMMIT = "/payment/commit";
    public static final String PAYMENT_ROLLBACK = "/payment/rollback";
    public static final String QUERY = "/query";

    private BunnyAddress() {
        throw new UnsupportedOperationException();
    }
}

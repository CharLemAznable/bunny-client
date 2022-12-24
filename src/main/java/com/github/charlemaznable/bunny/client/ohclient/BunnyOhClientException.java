package com.github.charlemaznable.bunny.client.ohclient;

import java.io.Serial;

public final class BunnyOhClientException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -9041493912358216019L;

    public BunnyOhClientException(String msg) {
        super(msg);
    }
}

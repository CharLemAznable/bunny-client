package com.github.charlemaznable.bunny.client.eventbus;

import java.io.Serial;

public final class BunnyEventBusException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2255319981621410586L;

    public BunnyEventBusException(String msg) {
        super(msg);
    }
}

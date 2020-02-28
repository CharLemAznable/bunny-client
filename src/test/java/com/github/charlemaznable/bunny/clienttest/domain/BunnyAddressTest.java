package com.github.charlemaznable.bunny.clienttest.domain;

import com.github.charlemaznable.bunny.client.domain.BunnyAddress;
import org.joor.ReflectException;
import org.junit.jupiter.api.Test;

import static org.joor.Reflect.onClass;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BunnyAddressTest {

    @Test
    public void testBunnyAddress() {
        assertThrows(ReflectException.class, () ->
                onClass(BunnyAddress.class).create().get());
    }
}

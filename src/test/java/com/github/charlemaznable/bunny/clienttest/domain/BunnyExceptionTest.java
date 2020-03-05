package com.github.charlemaznable.bunny.clienttest.domain;

import com.github.charlemaznable.bunny.client.domain.BunnyException;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.github.charlemaznable.core.codec.Json.unJson;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BunnyExceptionTest {

    @Test
    public void testBunnyException() {
        val bunnyException = new BunnyException("ERROR", "FAILURE");
        val message = bunnyException.getMessage();
        Map<String, Object> unJson = unJson(message);
        assertEquals(bunnyException.respCode(), unJson.get("respCode"));
        assertEquals(bunnyException.respDesc(), unJson.get("respDesc"));
    }
}

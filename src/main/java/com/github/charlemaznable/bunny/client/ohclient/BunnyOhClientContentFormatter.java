package com.github.charlemaznable.bunny.client.ohclient;

import com.github.charlemaznable.core.codec.NonsenseSignature;
import com.github.charlemaznable.core.net.common.ContentFormat.JsonContentFormatter;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Map;

@Component
public final class BunnyOhClientContentFormatter extends JsonContentFormatter {

    private NonsenseSignature nonsenseSignature = new NonsenseSignature();

    @Override
    public String format(@Nonnull Map<String, Object> parameterMap,
                         @Nonnull Map<String, Object> contextMap) {
        return super.format(nonsenseSignature.sign(parameterMap), contextMap);
    }
}

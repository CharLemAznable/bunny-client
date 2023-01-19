package com.github.charlemaznable.bunny.client.ohclient;

import com.github.charlemaznable.core.codec.NonsenseSignature;
import com.github.charlemaznable.core.codec.nonsense.NonsenseOptions;
import com.github.charlemaznable.core.codec.signature.SignatureOptions;
import com.github.charlemaznable.httpclient.common.ContentFormat.JsonContentFormatter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

import static com.github.charlemaznable.core.lang.Condition.notNullThen;

public final class BunnyOhClientContentFormatter extends JsonContentFormatter {

    private final NonsenseSignature nonsenseSignature;

    public BunnyOhClientContentFormatter(@Nullable NonsenseOptions nonsenseOptions,
                                         @Nullable SignatureOptions signatureOptions) {
        this.nonsenseSignature = new NonsenseSignature();
        notNullThen(nonsenseOptions, this.nonsenseSignature::nonsenseOptions);
        notNullThen(signatureOptions, this.nonsenseSignature::signatureOptions);
    }

    @Override
    public String format(@Nonnull Map<String, Object> parameterMap,
                         @Nonnull Map<String, Object> contextMap) {
        return super.format(nonsenseSignature.sign(parameterMap), contextMap);
    }
}

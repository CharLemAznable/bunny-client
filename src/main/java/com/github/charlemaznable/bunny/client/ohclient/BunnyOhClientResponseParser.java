package com.github.charlemaznable.bunny.client.ohclient;

import com.github.charlemaznable.core.codec.NonsenseSignature;
import com.github.charlemaznable.core.codec.nonsense.NonsenseOptions;
import com.github.charlemaznable.core.codec.signature.SignatureOptions;
import com.github.charlemaznable.core.net.common.ResponseParse.ResponseParser;
import com.google.inject.Inject;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

import static com.github.charlemaznable.bunny.client.domain.BunnyBaseResponse.RESP_CODE_KEY;
import static com.github.charlemaznable.bunny.client.domain.BunnyBaseResponse.RESP_CODE_OK;
import static com.github.charlemaznable.core.codec.Json.spec;
import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.lang.Condition.notNullThen;

@Component
public final class BunnyOhClientResponseParser implements ResponseParser {

    private final NonsenseSignature nonsenseSignature;

    public BunnyOhClientResponseParser() {
        this(null, null);
    }

    @Inject
    @Autowired
    public BunnyOhClientResponseParser(@Nullable NonsenseOptions nonsenseOptions,
                                       @Nullable SignatureOptions signatureOptions) {
        this.nonsenseSignature = new NonsenseSignature();
        notNullThen(nonsenseOptions, this.nonsenseSignature::nonsenseOptions);
        notNullThen(signatureOptions, this.nonsenseSignature::signatureOptions);
    }

    @Override
    public Object parse(@Nonnull String responseContent,
                        @Nonnull Class<?> returnType,
                        @Nonnull Map<String, Object> contextMap) {
        val responseMap = unJson(responseContent);
        if (!RESP_CODE_OK.equals(responseMap.get(RESP_CODE_KEY))) {
            // 失败响应, 不验证签名
            return spec(responseMap, returnType);
        }

        if (!nonsenseSignature.verify(responseMap)) {
            throw new BunnyOhClientException("Response verify failed");
        }
        return spec(responseMap, returnType);
    }
}

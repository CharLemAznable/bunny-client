package com.github.charlemaznable.bunny.client.spring;

import com.github.charlemaznable.core.codec.NonsenseSignature;
import com.github.charlemaznable.core.net.common.ResponseParse.ResponseParser;
import lombok.val;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Map;

import static com.github.charlemaznable.bunny.client.domain.BunnyBaseResponse.RESP_CODE_KEY;
import static com.github.charlemaznable.bunny.client.domain.BunnyBaseResponse.RESP_CODE_OK;
import static com.github.charlemaznable.core.codec.Json.spec;
import static com.github.charlemaznable.core.codec.Json.unJson;

@Component
public final class BunnyHttpClientResponseParser implements ResponseParser {

    private NonsenseSignature nonsenseSignature = new NonsenseSignature();

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
            throw new BunnyHttpClientException("Response verify failed");
        }
        return spec(responseMap, returnType);
    }
}

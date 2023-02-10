package com.github.charlemaznable.bunny.client.ohclient;

import com.github.charlemaznable.bunny.client.config.BunnyClientConfig;
import com.github.charlemaznable.core.codec.NonsenseSignature;
import com.github.charlemaznable.core.codec.nonsense.NonsenseOptions;
import com.github.charlemaznable.core.codec.signature.SignatureOptions;
import com.github.charlemaznable.httpclient.common.ContentFormat;
import com.github.charlemaznable.httpclient.common.HttpMethod;
import com.github.charlemaznable.httpclient.common.ResponseParse;
import com.github.charlemaznable.httpclient.configurer.ContentFormatConfigurer;
import com.github.charlemaznable.httpclient.configurer.RequestMethodConfigurer;
import com.github.charlemaznable.httpclient.configurer.ResponseParseConfigurer;
import com.github.charlemaznable.httpclient.configurer.configservice.MappingConfig;
import lombok.AllArgsConstructor;
import lombok.val;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Map;

import static com.github.charlemaznable.bunny.client.domain.BunnyBaseResponse.RESP_CODE_KEY;
import static com.github.charlemaznable.bunny.client.domain.BunnyBaseResponse.RESP_CODE_OK;
import static com.github.charlemaznable.configservice.ConfigFactory.getConfig;
import static com.github.charlemaznable.core.codec.Json.spec;
import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.lang.Condition.checkNotNull;
import static com.github.charlemaznable.core.lang.Condition.notNullThen;
import static com.github.charlemaznable.core.lang.Condition.nullThen;

public class BunnyOhClientConfigurer implements MappingConfig,
        ContentFormatConfigurer, RequestMethodConfigurer, ResponseParseConfigurer {

    private final BunnyClientConfig bunnyClientConfig;
    private final NonsenseSignature nonsenseSignature;

    public BunnyOhClientConfigurer(@Nullable BunnyClientConfig bunnyClientConfig,
                                   @Nullable NonsenseOptions nonsenseOptions,
                                   @Nullable SignatureOptions signatureOptions) {
        this.bunnyClientConfig = nullThen(bunnyClientConfig, () -> getConfig(BunnyClientConfig.class));
        this.nonsenseSignature = new NonsenseSignature();
        notNullThen(nonsenseOptions, this.nonsenseSignature::nonsenseOptions);
        notNullThen(signatureOptions, this.nonsenseSignature::signatureOptions);
    }

    @Override
    public String urlsString() {
        return checkNotNull(this.bunnyClientConfig.httpServerBaseUrl());
    }

    @Override
    public ContentFormat.ContentFormatter contentFormatter() {
        return new ContentFormatter(this.nonsenseSignature);
    }

    @Override
    public HttpMethod requestMethod() {
        return HttpMethod.POST;
    }

    @Override
    public ResponseParse.ResponseParser responseParser() {
        return new ResponseParser(this.nonsenseSignature);
    }

    @AllArgsConstructor
    static final class ContentFormatter extends ContentFormat.JsonContentFormatter {

        private final NonsenseSignature nonsenseSignature;

        @Override
        public String format(@Nonnull Map<String, Object> parameterMap,
                             @Nonnull Map<String, Object> contextMap) {
            return super.format(nonsenseSignature.sign(parameterMap), contextMap);
        }
    }

    @AllArgsConstructor
    static final class ResponseParser implements ResponseParse.ResponseParser {

        private final NonsenseSignature nonsenseSignature;

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
}

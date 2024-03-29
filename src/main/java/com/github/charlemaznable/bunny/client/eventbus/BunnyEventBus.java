package com.github.charlemaznable.bunny.client.eventbus;

import com.github.charlemaznable.bunny.client.config.BunnyClientConfig;
import com.github.charlemaznable.bunny.client.domain.BunnyBaseRequest;
import com.github.charlemaznable.bunny.client.domain.BunnyBaseResponse;
import com.github.charlemaznable.core.codec.NonsenseSignature;
import com.github.charlemaznable.core.codec.nonsense.NonsenseOptions;
import com.github.charlemaznable.core.codec.signature.SignatureOptions;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import lombok.val;

import javax.annotation.Nullable;

import static com.github.charlemaznable.bunny.client.domain.BunnyBaseResponse.RESP_CODE_KEY;
import static com.github.charlemaznable.bunny.client.domain.BunnyBaseResponse.RESP_CODE_OK;
import static com.github.charlemaznable.configservice.ConfigFactory.getConfig;
import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.codec.Json.spec;
import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.lang.Condition.checkNotNull;
import static com.github.charlemaznable.core.lang.Condition.notNullThen;
import static com.github.charlemaznable.core.lang.Condition.nullThen;
import static io.vertx.core.Future.failedFuture;
import static io.vertx.core.Future.succeededFuture;

public final class BunnyEventBus {

    private final EventBus eventBus;
    private final BunnyClientConfig bunnyClientConfig;
    private final NonsenseSignature nonsenseSignature;

    public BunnyEventBus(Vertx vertx,
                         @Nullable BunnyClientConfig bunnyClientConfig,
                         @Nullable NonsenseOptions nonsenseOptions,
                         @Nullable SignatureOptions signatureOptions) {
        this.eventBus = checkNotNull(vertx).eventBus();
        this.bunnyClientConfig = nullThen(bunnyClientConfig, () -> getConfig(BunnyClientConfig.class));
        this.nonsenseSignature = new NonsenseSignature();
        notNullThen(nonsenseOptions, this.nonsenseSignature::nonsenseOptions);
        notNullThen(signatureOptions, this.nonsenseSignature::signatureOptions);
    }

    public <T extends BunnyBaseResponse> void request
            (BunnyBaseRequest<T> request, Handler<AsyncResult<T>> handler) {
        val requestMap = nonsenseSignature.sign(request);
        eventBus.<String>request(bunnyClientConfig.eventBusAddressPrefix() + request.getBunnyAddress(),
                json(requestMap), asyncResult -> {
                    if (asyncResult.failed()) {
                        handler.handle(failedFuture(asyncResult.cause()));
                        return;
                    }

                    val responseMap = unJson(asyncResult.result().body());
                    if (!RESP_CODE_OK.equals(responseMap.get(RESP_CODE_KEY))) {
                        // 失败响应, 不验证签名
                        handler.handle(succeededFuture(
                                spec(responseMap, request.responseClass())));
                        return;
                    }

                    if (!nonsenseSignature.verify(responseMap)) {
                        handler.handle(failedFuture(
                                new BunnyEventBusException("Response verify failed")));
                        return;
                    }
                    handler.handle(succeededFuture(
                            spec(responseMap, request.responseClass())));
                });
    }
}

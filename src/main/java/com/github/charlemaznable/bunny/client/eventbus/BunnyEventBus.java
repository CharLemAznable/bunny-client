package com.github.charlemaznable.bunny.client.eventbus;

import com.github.charlemaznable.bunny.client.config.BunnyClientConfig;
import com.github.charlemaznable.bunny.client.domain.BunnyBaseRequest;
import com.github.charlemaznable.bunny.client.domain.BunnyBaseResponse;
import com.github.charlemaznable.bunny.client.domain.CalculateRequest;
import com.github.charlemaznable.bunny.client.domain.CalculateResponse;
import com.github.charlemaznable.bunny.client.domain.ChargeRequest;
import com.github.charlemaznable.bunny.client.domain.ChargeResponse;
import com.github.charlemaznable.bunny.client.domain.PaymentAdvanceRequest;
import com.github.charlemaznable.bunny.client.domain.PaymentAdvanceResponse;
import com.github.charlemaznable.bunny.client.domain.PaymentCommitRequest;
import com.github.charlemaznable.bunny.client.domain.PaymentCommitResponse;
import com.github.charlemaznable.bunny.client.domain.PaymentRollbackRequest;
import com.github.charlemaznable.bunny.client.domain.PaymentRollbackResponse;
import com.github.charlemaznable.core.codec.NonsenseSignature;
import com.google.inject.Inject;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

import static com.github.charlemaznable.bunny.client.domain.BunnyBaseResponse.RESP_CODE_KEY;
import static com.github.charlemaznable.bunny.client.domain.BunnyBaseResponse.RESP_CODE_OK;
import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.codec.Json.spec;
import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.lang.Condition.checkNotNull;
import static com.github.charlemaznable.core.lang.Condition.nullThen;
import static com.github.charlemaznable.core.miner.MinerFactory.getMiner;
import static io.vertx.core.Future.failedFuture;
import static io.vertx.core.Future.succeededFuture;

@Component
public final class BunnyEventBus {

    private final EventBus eventBus;
    private final BunnyClientConfig bunnyClientConfig;
    private NonsenseSignature nonsenseSignature = new NonsenseSignature();

    public BunnyEventBus(Vertx vertx) {
        this(vertx, null);
    }

    @Inject
    @Autowired
    public BunnyEventBus(Vertx vertx, @Nullable BunnyClientConfig bunnyClientConfig) {
        this.eventBus = checkNotNull(vertx).eventBus();
        this.bunnyClientConfig = nullThen(bunnyClientConfig,
                () -> getMiner(BunnyClientConfig.class));
    }

    public void calculate(CalculateRequest request,
                          Handler<AsyncResult<CalculateResponse>> handler) {
        request("/calculate", request, handler);
    }

    public void charge(ChargeRequest request,
                       Handler<AsyncResult<ChargeResponse>> handler) {
        request("/charge", request, handler);
    }

    public void paymentAdvance(PaymentAdvanceRequest request,
                               Handler<AsyncResult<PaymentAdvanceResponse>> handler) {
        request("/payment/advance", request, handler);
    }

    public void paymentCommit(PaymentCommitRequest request,
                              Handler<AsyncResult<PaymentCommitResponse>> handler) {
        request("/payment/commit", request, handler);
    }

    public void paymentRollback(PaymentRollbackRequest request,
                                Handler<AsyncResult<PaymentRollbackResponse>> handler) {
        request("/payment/rollback", request, handler);
    }

    private <T extends BunnyBaseResponse> void request(String address, BunnyBaseRequest<T> request,
                                                       Handler<AsyncResult<T>> handler) {
        val requestMap = nonsenseSignature.sign(request);
        eventBus.<String>request(bunnyClientConfig.eventBusAddressPrefix() + address,
                json(requestMap), asyncResult -> {
                    if (asyncResult.failed()) {
                        handler.handle(failedFuture(asyncResult.cause()));
                        return;
                    }

                    val responseMap = unJson(asyncResult.result().body());
                    if (!RESP_CODE_OK.equals(responseMap.get(RESP_CODE_KEY))) {
                        // 失败响应, 不验证签名
                        handler.handle(succeededFuture(
                                spec(responseMap, request.getResponseClass())));
                        return;
                    }

                    if (!nonsenseSignature.verify(responseMap)) {
                        handler.handle(failedFuture(
                                new BunnyEventBusException("Response verify failed")));
                        return;
                    }
                    handler.handle(succeededFuture(
                            spec(responseMap, request.getResponseClass())));
                });
    }
}

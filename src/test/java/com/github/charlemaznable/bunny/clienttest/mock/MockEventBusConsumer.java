package com.github.charlemaznable.bunny.clienttest.mock;

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
import com.github.charlemaznable.bunny.client.eventbus.BunnyEventBus;
import com.github.charlemaznable.bunny.client.eventbus.BunnyEventBusException;
import com.github.charlemaznable.core.codec.NonsenseSignature;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.junit5.VertxTestContext;
import lombok.val;

import java.util.HashMap;
import java.util.Map;

import static com.github.charlemaznable.bunny.client.domain.BunnyBaseResponse.RESP_CODE_OK;
import static com.github.charlemaznable.bunny.client.domain.BunnyBaseResponse.RESP_DESC_SUCCESS;
import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.codec.Json.spec;
import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.lang.Listt.newArrayList;
import static com.github.charlemaznable.core.lang.Mapp.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MockEventBusConsumer {

    private static final String PAYMENT_ID = "paymentId";
    private static final String BALANCE = "balance";
    private static final String UNIT = "unit";
    private static final NonsenseSignature nonsenseSignature = new NonsenseSignature();

    public static void testDefaultConsumer(Vertx vertx, BunnyEventBus bunnyEventBus, VertxTestContext test) {
        val eventBus = vertx.eventBus();
        eventBus.<String>consumer("/bunny/calculate", message -> {
            val requestMap = verifyRequestMap(message);
            val req1 = spec(requestMap, CalculateRequest.class);
            assertEquals("value1", req1.getChargingParameters().get("key1"));
            val resp1 = new CalculateResponse();
            resp1.setChargingType(req1.getChargingType());
            resp1.setRespCode(RESP_CODE_OK);
            resp1.setRespDesc(RESP_DESC_SUCCESS);
            resp1.setCalcResult("calcResult");
            resp1.setCalcResultUnit("calcResultUnit");
            message.reply(json(nonsenseSignature.sign(resp1)));
        });
        eventBus.<String>consumer("/bunny/charge", message -> {
            val requestMap = verifyRequestMap(message);
            val req2 = spec(requestMap, ChargeRequest.class);
            assertEquals("chargeValue", req2.getChargeValue());
            val resp2 = new ChargeResponse();
            resp2.setChargingType(req2.getChargingType());
            resp2.setRespCode(RESP_CODE_OK);
            resp2.setRespDesc(RESP_DESC_SUCCESS);
            resp2.setBalance(BALANCE);
            resp2.setBalanceUnit("balanceUnit");
            message.reply(json(nonsenseSignature.sign(resp2)));
        });
        eventBus.<String>consumer("/bunny/payment/advance", message -> {
            val requestMap = verifyRequestMap(message);
            val req3 = spec(requestMap, PaymentAdvanceRequest.class);
            assertEquals("value2", req3.getChargingParameters().get("key2"));
            val resp3 = new PaymentAdvanceResponse();
            resp3.setChargingType(req3.getChargingType());
            resp3.setRespCode(RESP_CODE_OK);
            resp3.setRespDesc(RESP_DESC_SUCCESS);
            resp3.setPaymentId(PAYMENT_ID);
            resp3.setPaymentValue("paymentValue");
            resp3.setBalance(BALANCE);
            resp3.setUnit(UNIT);
            message.reply(json(nonsenseSignature.sign(resp3)));
        });
        eventBus.<String>consumer("/bunny/payment/commit", message -> {
            val requestMap = verifyRequestMap(message);
            val req4 = spec(requestMap, PaymentCommitRequest.class);
            assertEquals(PAYMENT_ID, req4.getPaymentId());
            val resp4 = new PaymentCommitResponse();
            resp4.setChargingType(req4.getChargingType());
            resp4.setRespCode(RESP_CODE_OK);
            resp4.setRespDesc(RESP_DESC_SUCCESS);
            resp4.setCommitValue("commitValue");
            resp4.setBalance(BALANCE);
            resp4.setUnit(UNIT);
            message.reply(json(nonsenseSignature.sign(resp4)));
        });
        eventBus.<String>consumer("/bunny/payment/rollback", message -> {
            val requestMap = verifyRequestMap(message);
            val req5 = spec(requestMap, PaymentRollbackRequest.class);
            assertEquals(PAYMENT_ID, req5.getPaymentId());
            val resp5 = new PaymentRollbackResponse();
            resp5.setChargingType(req5.getChargingType());
            resp5.setRespCode(RESP_CODE_OK);
            resp5.setRespDesc(RESP_DESC_SUCCESS);
            resp5.setRollbackValue("rollbackValue");
            resp5.setBalance(BALANCE);
            resp5.setUnit(UNIT);
            message.reply(json(nonsenseSignature.sign(resp5)));
        });

        CompositeFuture.all(newArrayList(
                Future.<Void>future(f -> {
                    val calculateRequest = new CalculateRequest();
                    calculateRequest.setChargingType("calculate");
                    calculateRequest.setChargingParameters(of("key1", "value1"));
                    bunnyEventBus.request(calculateRequest, async -> test.verify(() -> {
                        val calculateResponse = async.result();
                        assertEquals(calculateRequest.getChargingType(), calculateResponse.getChargingType());
                        assertTrue(calculateResponse.isSuccess());
                        assertEquals(RESP_CODE_OK, calculateResponse.getRespCode());
                        assertEquals(RESP_DESC_SUCCESS, calculateResponse.getRespDesc());
                        assertEquals("calcResult", calculateResponse.getCalcResult());
                        assertEquals("calcResultUnit", calculateResponse.getCalcResultUnit());
                        f.complete();
                    }));
                }),
                Future.<Void>future(f -> {
                    val chargeRequest = new ChargeRequest();
                    chargeRequest.setChargeValue("charge");
                    chargeRequest.setChargeValue("chargeValue");
                    bunnyEventBus.request(chargeRequest, async -> test.verify(() -> {
                        val chargeResponse = async.result();
                        assertEquals(chargeRequest.getChargingType(), chargeResponse.getChargingType());
                        assertTrue(chargeResponse.isSuccess());
                        assertEquals(RESP_CODE_OK, chargeResponse.getRespCode());
                        assertEquals(RESP_DESC_SUCCESS, chargeResponse.getRespDesc());
                        assertEquals(BALANCE, chargeResponse.getBalance());
                        assertEquals("balanceUnit", chargeResponse.getBalanceUnit());
                        f.complete();
                    }));
                }),
                Future.<Void>future(f -> {
                    val advanceRequest = new PaymentAdvanceRequest();
                    advanceRequest.setChargingType("advance");
                    advanceRequest.setChargingParameters(of("key2", "value2"));
                    bunnyEventBus.request(advanceRequest, async -> test.verify(() -> {
                        val advanceResponse = async.result();
                        assertEquals(advanceRequest.getChargingType(), advanceResponse.getChargingType());
                        assertTrue(advanceResponse.isSuccess());
                        assertEquals(RESP_CODE_OK, advanceResponse.getRespCode());
                        assertEquals(RESP_DESC_SUCCESS, advanceResponse.getRespDesc());
                        assertEquals(PAYMENT_ID, advanceResponse.getPaymentId());
                        assertEquals("paymentValue", advanceResponse.getPaymentValue());
                        assertEquals(BALANCE, advanceResponse.getBalance());
                        assertEquals(UNIT, advanceResponse.getUnit());
                        f.complete();
                    }));
                }),
                Future.<Void>future(f -> {
                    val commitRequest = new PaymentCommitRequest();
                    commitRequest.setChargingType("commit");
                    commitRequest.setPaymentId(PAYMENT_ID);
                    bunnyEventBus.request(commitRequest, async -> test.verify(() -> {
                        val commitResponse = async.result();
                        assertEquals(commitRequest.getChargingType(), commitResponse.getChargingType());
                        assertTrue(commitResponse.isSuccess());
                        assertEquals(RESP_CODE_OK, commitResponse.getRespCode());
                        assertEquals(RESP_DESC_SUCCESS, commitResponse.getRespDesc());
                        assertEquals("commitValue", commitResponse.getCommitValue());
                        assertEquals(BALANCE, commitResponse.getBalance());
                        assertEquals(UNIT, commitResponse.getUnit());
                        f.complete();
                    }));
                }),
                Future.<Void>future(f -> {
                    val rollbackRequest = new PaymentRollbackRequest();
                    rollbackRequest.setChargingType("rollback");
                    rollbackRequest.setPaymentId(PAYMENT_ID);
                    bunnyEventBus.request(rollbackRequest, async -> test.verify(() -> {
                        val rollbackResponse = async.result();
                        assertEquals(rollbackRequest.getChargingType(), rollbackResponse.getChargingType());
                        assertTrue(rollbackResponse.isSuccess());
                        assertEquals(RESP_CODE_OK, rollbackResponse.getRespCode());
                        assertEquals(RESP_DESC_SUCCESS, rollbackResponse.getRespDesc());
                        assertEquals("rollbackValue", rollbackResponse.getRollbackValue());
                        assertEquals(BALANCE, rollbackResponse.getBalance());
                        assertEquals(UNIT, rollbackResponse.getUnit());
                        f.complete();
                    }));
                })
        )).setHandler(result -> test.<CompositeFuture>completing().handle(result));
    }

    public static void testErrorConsumer(Vertx vertx, BunnyEventBus bunnyEventBus, VertxTestContext test) {
        val eventBus = vertx.eventBus();
        eventBus.<String>consumer("/error/calculate", message -> {
            val resp = new CalculateResponse();
            resp.setRespCode("ERROR");
            resp.setRespDesc("FAILURE");
            message.reply(json(resp));
        });

        CompositeFuture.all(newArrayList(
                Future.<Void>future(f -> {
                    val calculateRequest = new CalculateRequest();
                    calculateRequest.setChargingType("error");
                    calculateRequest.setChargingParameters(new HashMap<>());
                    bunnyEventBus.request(calculateRequest, async -> test.verify(() -> {
                        val calculateResponse = async.result();
                        assertFalse(calculateResponse.isSuccess());
                        assertEquals("ERROR", calculateResponse.getRespCode());
                        assertEquals("FAILURE", calculateResponse.getRespDesc());
                        f.complete();
                    }));
                })
        )).setHandler(result -> test.<CompositeFuture>completing().handle(result));
    }

    public static void testExceptionConsumer(Vertx vertx, BunnyEventBus bunnyEventBus, VertxTestContext test) {
        val eventBus = vertx.eventBus();
        eventBus.<String>consumer("/exception/calculate", message -> {
            val resp = new CalculateResponse();
            resp.setChargingType("error");
            resp.setRespCode("OK");
            resp.setRespDesc("SUCCESS");
            resp.setCalcResult("calcResult");
            resp.setCalcResultUnit("calcResultUnit");
            message.reply(json(resp));
        });

        CompositeFuture.all(newArrayList(
                Future.<Void>future(f -> {
                    val calculateRequest = new CalculateRequest();
                    calculateRequest.setChargingType("calculate");
                    calculateRequest.setChargingParameters(new HashMap<>());
                    bunnyEventBus.request(calculateRequest, async -> test.verify(() -> {
                        assertTrue(async.failed());
                        val cause = async.cause();
                        assertTrue(cause instanceof BunnyEventBusException);
                        assertEquals("Response verify failed", cause.getMessage());
                        f.complete();
                    }));
                }),
                Future.<Void>future(f -> {
                    val chargeRequest = new ChargeRequest();
                    chargeRequest.setChargeValue("charge");
                    chargeRequest.setChargeValue("chargeValue");
                    bunnyEventBus.request(chargeRequest, async -> test.verify(() -> {
                        assertTrue(async.failed());
                        val cause = async.cause();
                        assertTrue(cause instanceof ReplyException);
                        assertEquals("No handlers for address /exception/charge", cause.getMessage());
                        f.complete();
                    }));
                })
        )).setHandler(result -> test.<CompositeFuture>completing().handle(result));
    }

    private static Map<String, Object> verifyRequestMap(Message<String> message) {
        val requestBody = message.body();
        val requestMap = unJson(requestBody);
        assertTrue(nonsenseSignature.verify(requestMap));
        return requestMap;
    }
}

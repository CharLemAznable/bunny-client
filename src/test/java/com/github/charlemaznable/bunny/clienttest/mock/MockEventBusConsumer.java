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
import com.github.charlemaznable.bunny.client.domain.QueryRequest;
import com.github.charlemaznable.bunny.client.domain.QueryResponse;
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

    private static final int CALCULATE_VALUE = 12;
    private static final int CHARGE_VALUE = 34;
    private static final String PAYMENT_ID_VALUE = "paymentId";
    private static final int COMMIT_VALUE = 56;
    private static final int ROLLBACK_VALUE = 78;
    private static final int BALANCE_VALUE = 90;
    private static final String UNIT_VALUE = "unit";
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
            resp1.setCalculate(CALCULATE_VALUE);
            resp1.setUnit(UNIT_VALUE);
            message.reply(json(nonsenseSignature.sign(resp1)));
        });
        eventBus.<String>consumer("/bunny/charge", message -> {
            val requestMap = verifyRequestMap(message);
            val req2 = spec(requestMap, ChargeRequest.class);
            assertEquals(CHARGE_VALUE, req2.getChargeValue());
            val resp2 = new ChargeResponse();
            resp2.setChargingType(req2.getChargingType());
            resp2.setRespCode(RESP_CODE_OK);
            resp2.setRespDesc(RESP_DESC_SUCCESS);
            message.reply(json(nonsenseSignature.sign(resp2)));
        });
        eventBus.<String>consumer("/bunny/payment/advance", message -> {
            val requestMap = verifyRequestMap(message);
            val req3 = spec(requestMap, PaymentAdvanceRequest.class);
            assertEquals(CALCULATE_VALUE, req3.getPaymentValue());
            val resp3 = new PaymentAdvanceResponse();
            resp3.setChargingType(req3.getChargingType());
            resp3.setRespCode(RESP_CODE_OK);
            resp3.setRespDesc(RESP_DESC_SUCCESS);
            resp3.setPaymentId(PAYMENT_ID_VALUE);
            message.reply(json(nonsenseSignature.sign(resp3)));
        });
        eventBus.<String>consumer("/bunny/payment/commit", message -> {
            val requestMap = verifyRequestMap(message);
            val req4 = spec(requestMap, PaymentCommitRequest.class);
            assertEquals(PAYMENT_ID_VALUE, req4.getPaymentId());
            val resp4 = new PaymentCommitResponse();
            resp4.setChargingType(req4.getChargingType());
            resp4.setRespCode(RESP_CODE_OK);
            resp4.setRespDesc(RESP_DESC_SUCCESS);
            resp4.setCommit(COMMIT_VALUE);
            resp4.setUnit(UNIT_VALUE);
            message.reply(json(nonsenseSignature.sign(resp4)));
        });
        eventBus.<String>consumer("/bunny/payment/rollback", message -> {
            val requestMap = verifyRequestMap(message);
            val req5 = spec(requestMap, PaymentRollbackRequest.class);
            assertEquals(PAYMENT_ID_VALUE, req5.getPaymentId());
            val resp5 = new PaymentRollbackResponse();
            resp5.setChargingType(req5.getChargingType());
            resp5.setRespCode(RESP_CODE_OK);
            resp5.setRespDesc(RESP_DESC_SUCCESS);
            resp5.setRollback(ROLLBACK_VALUE);
            resp5.setUnit(UNIT_VALUE);
            message.reply(json(nonsenseSignature.sign(resp5)));
        });
        eventBus.<String>consumer("/bunny/query", message -> {
            val requestMap = verifyRequestMap(message);
            val req6 = spec(requestMap, QueryRequest.class);
            val resp6 = new QueryResponse();
            resp6.setChargingType(req6.getChargingType());
            resp6.setRespCode(RESP_CODE_OK);
            resp6.setRespDesc(RESP_DESC_SUCCESS);
            resp6.setBalance(BALANCE_VALUE);
            resp6.setUnit(UNIT_VALUE);
            message.reply(json(nonsenseSignature.sign(resp6)));
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
                        assertEquals(CALCULATE_VALUE, calculateResponse.getCalculate());
                        assertEquals(UNIT_VALUE, calculateResponse.getUnit());
                        f.complete();
                    }));
                }),
                Future.<Void>future(f -> {
                    val chargeRequest = new ChargeRequest();
                    chargeRequest.setChargingType("charge");
                    chargeRequest.setChargeValue(CHARGE_VALUE);
                    bunnyEventBus.request(chargeRequest, async -> test.verify(() -> {
                        val chargeResponse = async.result();
                        assertEquals(chargeRequest.getChargingType(), chargeResponse.getChargingType());
                        assertTrue(chargeResponse.isSuccess());
                        assertEquals(RESP_CODE_OK, chargeResponse.getRespCode());
                        assertEquals(RESP_DESC_SUCCESS, chargeResponse.getRespDesc());
                        f.complete();
                    }));
                }),
                Future.<Void>future(f -> {
                    val advanceRequest = new PaymentAdvanceRequest();
                    advanceRequest.setChargingType("advance");
                    advanceRequest.setPaymentValue(CALCULATE_VALUE);
                    bunnyEventBus.request(advanceRequest, async -> test.verify(() -> {
                        val advanceResponse = async.result();
                        assertEquals(advanceRequest.getChargingType(), advanceResponse.getChargingType());
                        assertTrue(advanceResponse.isSuccess());
                        assertEquals(RESP_CODE_OK, advanceResponse.getRespCode());
                        assertEquals(RESP_DESC_SUCCESS, advanceResponse.getRespDesc());
                        assertEquals(PAYMENT_ID_VALUE, advanceResponse.getPaymentId());
                        f.complete();
                    }));
                }),
                Future.<Void>future(f -> {
                    val commitRequest = new PaymentCommitRequest();
                    commitRequest.setChargingType("commit");
                    commitRequest.setPaymentId(PAYMENT_ID_VALUE);
                    bunnyEventBus.request(commitRequest, async -> test.verify(() -> {
                        val commitResponse = async.result();
                        assertEquals(commitRequest.getChargingType(), commitResponse.getChargingType());
                        assertTrue(commitResponse.isSuccess());
                        assertEquals(RESP_CODE_OK, commitResponse.getRespCode());
                        assertEquals(RESP_DESC_SUCCESS, commitResponse.getRespDesc());
                        assertEquals(COMMIT_VALUE, commitResponse.getCommit());
                        assertEquals(UNIT_VALUE, commitResponse.getUnit());
                        f.complete();
                    }));
                }),
                Future.<Void>future(f -> {
                    val rollbackRequest = new PaymentRollbackRequest();
                    rollbackRequest.setChargingType("rollback");
                    rollbackRequest.setPaymentId(PAYMENT_ID_VALUE);
                    bunnyEventBus.request(rollbackRequest, async -> test.verify(() -> {
                        val rollbackResponse = async.result();
                        assertEquals(rollbackRequest.getChargingType(), rollbackResponse.getChargingType());
                        assertTrue(rollbackResponse.isSuccess());
                        assertEquals(RESP_CODE_OK, rollbackResponse.getRespCode());
                        assertEquals(RESP_DESC_SUCCESS, rollbackResponse.getRespDesc());
                        assertEquals(ROLLBACK_VALUE, rollbackResponse.getRollback());
                        assertEquals(UNIT_VALUE, rollbackResponse.getUnit());
                        f.complete();
                    }));
                }),
                Future.<Void>future(f -> {
                    val queryRequest = new QueryRequest();
                    queryRequest.setChargingType("query");
                    bunnyEventBus.request(queryRequest, async -> test.verify(() -> {
                        val queryResponse = async.result();
                        assertEquals(queryRequest.getChargingType(), queryResponse.getChargingType());
                        assertTrue(queryResponse.isSuccess());
                        assertEquals(RESP_CODE_OK, queryResponse.getRespCode());
                        assertEquals(RESP_DESC_SUCCESS, queryResponse.getRespDesc());
                        assertEquals(BALANCE_VALUE, queryResponse.getBalance());
                        assertEquals(UNIT_VALUE, queryResponse.getUnit());
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
            resp.setRespCode(RESP_CODE_OK);
            resp.setRespDesc(RESP_DESC_SUCCESS);
            resp.setCalculate(CALCULATE_VALUE);
            resp.setUnit(UNIT_VALUE);
            message.reply(json(resp));
        });

        CompositeFuture.all(newArrayList(
                Future.<Void>future(f -> {
                    val calculateRequest = new CalculateRequest();
                    calculateRequest.setChargingType("error");
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
                    chargeRequest.setChargingType("error");
                    chargeRequest.setChargeValue(CHARGE_VALUE);
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

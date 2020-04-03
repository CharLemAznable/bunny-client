package com.github.charlemaznable.bunny.clienttest.mock;

import com.github.charlemaznable.bunny.client.domain.BunnyException;
import com.github.charlemaznable.bunny.client.domain.CalculateRequest;
import com.github.charlemaznable.bunny.client.domain.CalculateResponse;
import com.github.charlemaznable.bunny.client.domain.ChargeRequest;
import com.github.charlemaznable.bunny.client.domain.QueryRequest;
import com.github.charlemaznable.bunny.client.domain.ServeCallbackRequest;
import com.github.charlemaznable.bunny.client.domain.ServeRequest;
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
    private static final int BALANCE_VALUE = 56;
    private static final String UNIT_VALUE = "unit";
    private static final Integer PAYMENT_VALUE = 78;
    private static final String SERVE_TYPE = "proxy";
    private static final String INTERNAL_KEY = "key";
    private static final String INTERNAL_VALUE = "value";
    private static final String CALLBACK_URL = "callback-url";
    private static final String UNEXPECTED_FAILURE = "unexpected-failure";
    private static final String SEQ_ID = "seq-id";
    private static final NonsenseSignature nonsenseSignature = new NonsenseSignature();

    public static void testDefaultConsumer(Vertx vertx, BunnyEventBus bunnyEventBus, VertxTestContext test) {
        val eventBus = vertx.eventBus();
        eventBus.<String>consumer("/bunny/calculate", message -> {
            val requestMap = verifyRequestMap(message);
            val calculateRequest = spec(requestMap, CalculateRequest.class);
            assertEquals("value1", calculateRequest.getChargingParameters().get("key1"));
            val calculateResponse = calculateRequest.createResponse();
            calculateResponse.succeed();
            calculateResponse.setCalculate(CALCULATE_VALUE);
            message.reply(json(nonsenseSignature.sign(calculateResponse)));
        });
        eventBus.<String>consumer("/bunny/charge", message -> {
            val requestMap = verifyRequestMap(message);
            val chargeRequest = spec(requestMap, ChargeRequest.class);
            assertEquals(CHARGE_VALUE, chargeRequest.getChargeValue());
            val chargeResponse = chargeRequest.createResponse();
            chargeResponse.succeed();
            message.reply(json(nonsenseSignature.sign(chargeResponse)));
        });
        eventBus.<String>consumer("/bunny/query", message -> {
            val requestMap = verifyRequestMap(message);
            val queryRequest = spec(requestMap, QueryRequest.class);
            val queryResponse = queryRequest.createResponse();
            queryResponse.succeed();
            queryResponse.setBalance(BALANCE_VALUE);
            queryResponse.setUnit(UNIT_VALUE);
            message.reply(json(nonsenseSignature.sign(queryResponse)));
        });
        eventBus.<String>consumer("/bunny/serve", message -> {
            val requestMap = verifyRequestMap(message);
            val serveRequest = spec(requestMap, ServeRequest.class);
            assertEquals(PAYMENT_VALUE, serveRequest.getPaymentValue());
            assertEquals(INTERNAL_VALUE, serveRequest.getInternalRequest().get(INTERNAL_KEY));
            assertEquals(CALLBACK_URL, serveRequest.getCallbackUrl());
            val serveResponse = serveRequest.createResponse();
            serveResponse.succeed();
            serveResponse.setInternalResponse(serveRequest.getInternalRequest());
            serveResponse.setUnexpectedFailure(UNEXPECTED_FAILURE);
            message.reply(json(nonsenseSignature.sign(serveResponse)));
        });
        eventBus.<String>consumer("/bunny/serve-callback", message -> {
            val requestMap = verifyRequestMap(message);
            val serveCallbackRequest = spec(requestMap, ServeCallbackRequest.class);
            assertEquals(INTERNAL_VALUE, serveCallbackRequest.getInternalRequest().get(INTERNAL_KEY));
            assertEquals(SEQ_ID, serveCallbackRequest.getSeqId());
            val serveCallbackResponse = serveCallbackRequest.createResponse();
            serveCallbackResponse.succeed();
            serveCallbackResponse.setUnexpectedFailure(UNEXPECTED_FAILURE);
            message.reply(json(nonsenseSignature.sign(serveCallbackResponse)));
        });

        CompositeFuture.all(newArrayList(
                Future.<Void>future(f -> {
                    val calculateRequest = new CalculateRequest();
                    calculateRequest.setServeName("calculate");
                    calculateRequest.setChargingParameters(of("key1", "value1"));
                    bunnyEventBus.request(calculateRequest, async -> test.verify(() -> {
                        val calculateResponse = async.result();
                        assertEquals(calculateRequest.getServeName(), calculateResponse.getServeName());
                        assertTrue(calculateResponse.isSuccess());
                        assertEquals(RESP_CODE_OK, calculateResponse.getRespCode());
                        assertEquals(RESP_DESC_SUCCESS, calculateResponse.getRespDesc());
                        assertEquals(CALCULATE_VALUE, calculateResponse.getCalculate());
                        f.complete();
                    }));
                }),
                Future.<Void>future(f -> {
                    val chargeRequest = new ChargeRequest();
                    chargeRequest.setServeName("charge");
                    chargeRequest.setChargeValue(CHARGE_VALUE);
                    bunnyEventBus.request(chargeRequest, async -> test.verify(() -> {
                        val chargeResponse = async.result();
                        assertEquals(chargeRequest.getServeName(), chargeResponse.getServeName());
                        assertTrue(chargeResponse.isSuccess());
                        assertEquals(RESP_CODE_OK, chargeResponse.getRespCode());
                        assertEquals(RESP_DESC_SUCCESS, chargeResponse.getRespDesc());
                        f.complete();
                    }));
                }),
                Future.<Void>future(f -> {
                    val queryRequest = new QueryRequest();
                    queryRequest.setServeName("query");
                    bunnyEventBus.request(queryRequest, async -> test.verify(() -> {
                        val queryResponse = async.result();
                        assertEquals(queryRequest.getServeName(), queryResponse.getServeName());
                        assertTrue(queryResponse.isSuccess());
                        assertEquals(RESP_CODE_OK, queryResponse.getRespCode());
                        assertEquals(RESP_DESC_SUCCESS, queryResponse.getRespDesc());
                        assertEquals(BALANCE_VALUE, queryResponse.getBalance());
                        assertEquals(UNIT_VALUE, queryResponse.getUnit());
                        f.complete();
                    }));
                }),
                Future.<Void>future(f -> {
                    val serveRequest = new ServeRequest();
                    serveRequest.setServeName("serve");
                    serveRequest.setPaymentValue(PAYMENT_VALUE);
                    serveRequest.setInternalRequest(of(INTERNAL_KEY, INTERNAL_VALUE));
                    serveRequest.setCallbackUrl(CALLBACK_URL);
                    bunnyEventBus.request(serveRequest, async -> test.verify(() -> {
                        val serveResponse = async.result();
                        assertEquals(serveRequest.getServeName(), serveResponse.getServeName());
                        assertTrue(serveResponse.isSuccess());
                        assertEquals(RESP_CODE_OK, serveResponse.getRespCode());
                        assertEquals(RESP_DESC_SUCCESS, serveResponse.getRespDesc());
                        assertEquals(INTERNAL_VALUE, serveResponse.getInternalResponse().get(INTERNAL_KEY));
                        assertEquals(UNEXPECTED_FAILURE, serveResponse.getUnexpectedFailure());
                        f.complete();
                    }));
                }),
                Future.<Void>future(f -> {
                    val serveCallbackRequest = new ServeCallbackRequest();
                    serveCallbackRequest.setServeName("serveCallback");
                    serveCallbackRequest.setInternalRequest(of(INTERNAL_KEY, INTERNAL_VALUE));
                    serveCallbackRequest.setSeqId(SEQ_ID);
                    bunnyEventBus.request(serveCallbackRequest, async -> test.verify(() -> {
                        val serveCallbackResponse = async.result();
                        assertEquals(serveCallbackRequest.getServeName(), serveCallbackResponse.getServeName());
                        assertTrue(serveCallbackResponse.isSuccess());
                        assertEquals(RESP_CODE_OK, serveCallbackResponse.getRespCode());
                        assertEquals(RESP_DESC_SUCCESS, serveCallbackResponse.getRespDesc());
                        assertEquals(UNEXPECTED_FAILURE, serveCallbackResponse.getUnexpectedFailure());
                        f.complete();
                    }));
                })
        )).setHandler(result -> test.<CompositeFuture>completing().handle(result));
    }

    public static void testErrorConsumer(Vertx vertx, BunnyEventBus bunnyEventBus, VertxTestContext test) {
        val eventBus = vertx.eventBus();
        eventBus.<String>consumer("/error/calculate", message -> {
            val resp = new CalculateResponse();
            resp.failed(new BunnyException("ERROR", "FAILURE"));
            message.reply(json(resp));
        });

        CompositeFuture.all(newArrayList(
                Future.<Void>future(f -> {
                    val calculateRequest = new CalculateRequest();
                    calculateRequest.setServeName("error");
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
            resp.setServeName("error");
            resp.succeed();
            resp.setCalculate(CALCULATE_VALUE);
            message.reply(json(resp));
        });

        CompositeFuture.all(newArrayList(
                Future.<Void>future(f -> {
                    val calculateRequest = new CalculateRequest();
                    calculateRequest.setServeName("error");
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
                    chargeRequest.setServeName("error");
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

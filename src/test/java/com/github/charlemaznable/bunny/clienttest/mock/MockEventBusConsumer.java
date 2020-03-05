package com.github.charlemaznable.bunny.clienttest.mock;

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
import static org.junit.jupiter.api.Assertions.assertNull;
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
            val calculateRequest = spec(requestMap, CalculateRequest.class);
            assertEquals("value1", calculateRequest.getChargingParameters().get("key1"));
            val calculateResponse = calculateRequest.createResponse();
            calculateResponse.succeed();
            calculateResponse.setCalculate(CALCULATE_VALUE);
            calculateResponse.setUnit(UNIT_VALUE);
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
            assertNull(serveRequest.getPaymentValue());
            assertNull(serveRequest.getChargingParameters());
            val serveResponse = serveRequest.createResponse();
            serveResponse.succeed();
            serveResponse.setServeType(serveRequest.getServeType());
            serveResponse.setInternalResponse(serveRequest.getInternalRequest());
            message.reply(json(nonsenseSignature.sign(serveResponse)));
        });
        eventBus.<String>consumer("/bunny/serve-callback", message -> {
            val requestMap = verifyRequestMap(message);
            val serveCallbackRequest = spec(requestMap, ServeCallbackRequest.class);
            val serveCallbackResponse = serveCallbackRequest.createResponse();
            serveCallbackResponse.succeed();
            serveCallbackResponse.setServeType(serveCallbackRequest.getServeType());
            message.reply(json(nonsenseSignature.sign(serveCallbackResponse)));
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
                }),
                Future.<Void>future(f -> {
                    val serveRequest = new ServeRequest();
                    serveRequest.setChargingType("serve");
                    serveRequest.setServeType("proxy");
                    serveRequest.setInternalRequest(of("key2", "value2"));
                    bunnyEventBus.request(serveRequest, async -> test.verify(() -> {
                        val serveResponse = async.result();
                        assertEquals(serveRequest.getChargingType(), serveResponse.getChargingType());
                        assertTrue(serveResponse.isSuccess());
                        assertEquals(RESP_CODE_OK, serveResponse.getRespCode());
                        assertEquals(RESP_DESC_SUCCESS, serveResponse.getRespDesc());
                        assertEquals("proxy", serveResponse.getServeType());
                        assertEquals("value2", serveResponse.getInternalResponse().get("key2"));
                        f.complete();
                    }));
                }),
                Future.<Void>future(f -> {
                    val serveCallbackRequest = new ServeCallbackRequest();
                    serveCallbackRequest.setChargingType("serve");
                    serveCallbackRequest.setServeType("proxy");
                    serveCallbackRequest.setInternalRequest(of("key3", "value3"));
                    bunnyEventBus.request(serveCallbackRequest, async -> test.verify(() -> {
                        val serveCallbackResponse = async.result();
                        assertEquals(serveCallbackRequest.getChargingType(), serveCallbackResponse.getChargingType());
                        assertTrue(serveCallbackResponse.isSuccess());
                        assertEquals(RESP_CODE_OK, serveCallbackResponse.getRespCode());
                        assertEquals(RESP_DESC_SUCCESS, serveCallbackResponse.getRespDesc());
                        assertEquals("proxy", serveCallbackResponse.getServeType());
                        f.complete();
                    }));
                })
        )).setHandler(result -> test.<CompositeFuture>completing().handle(result));
    }

    public static void testErrorConsumer(Vertx vertx, BunnyEventBus bunnyEventBus, VertxTestContext test) {
        val eventBus = vertx.eventBus();
        eventBus.<String>consumer("/error/calculate", message -> {
            val resp = new CalculateResponse();
            resp.failed("ERROR", "FAILURE");
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
            resp.succeed();
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

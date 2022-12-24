package com.github.charlemaznable.bunny.clienttest.mock;

import com.github.charlemaznable.bunny.client.domain.CalculateRequest;
import com.github.charlemaznable.bunny.client.domain.CalculateResponse;
import com.github.charlemaznable.bunny.client.domain.ChargeRequest;
import com.github.charlemaznable.bunny.client.domain.QueryRequest;
import com.github.charlemaznable.bunny.client.domain.ServeCallbackRequest;
import com.github.charlemaznable.bunny.client.domain.ServeRequest;
import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClient;
import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClientException;
import com.github.charlemaznable.core.codec.NonsenseSignature;
import com.github.charlemaznable.httpclient.common.StatusError;
import lombok.SneakyThrows;
import lombok.val;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.springframework.http.HttpStatus;

import javax.annotation.Nonnull;
import java.util.HashMap;

import static com.github.charlemaznable.bunny.client.domain.BunnyBaseResponse.RESP_CODE_OK;
import static com.github.charlemaznable.bunny.client.domain.BunnyBaseResponse.RESP_DESC_SUCCESS;
import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.codec.Json.spec;
import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.lang.Mapp.of;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MockOhClientServer {

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

    @SneakyThrows
    public static void testDefaultServer(BunnyOhClient bunnyOhClient) {
        try (val mockWebServer = new MockWebServer()) {
            mockWebServer.setDispatcher(new Dispatcher() {
                @Nonnull
                @Override
                public MockResponse dispatch(@Nonnull RecordedRequest request) {
                    val requestBody = request.getBody().readUtf8();
                    val requestMap = unJson(requestBody);
                    assertTrue(nonsenseSignature.verify(requestMap));

                    switch (requireNonNull(request.getPath())) {
                        case "/bunny/calculate":
                            val calculateRequest = spec(requestMap, CalculateRequest.class);
                            assertEquals("value1", calculateRequest.getChargingParameters().get("key1"));
                            val calculateResponse = calculateRequest.createResponse();
                            calculateResponse.succeed();
                            calculateResponse.setCalculate(CALCULATE_VALUE);
                            return new MockResponse().setBody(json(
                                    nonsenseSignature.sign(calculateResponse)));

                        case "/bunny/charge":
                            val chargeRequest = spec(requestMap, ChargeRequest.class);
                            assertEquals(CHARGE_VALUE, chargeRequest.getChargeValue());
                            val chargeResponse = chargeRequest.createResponse();
                            chargeResponse.succeed();
                            return new MockResponse().setBody(json(
                                    nonsenseSignature.sign(chargeResponse)));

                        case "/bunny/query":
                            val queryRequest = spec(requestMap, QueryRequest.class);
                            val queryResponse = queryRequest.createResponse();
                            queryResponse.succeed();
                            queryResponse.setBalance(BALANCE_VALUE);
                            queryResponse.setUnit(UNIT_VALUE);
                            return new MockResponse().setBody(json(
                                    nonsenseSignature.sign(queryResponse)));

                        case "/bunny/serve":
                            val serveRequest = spec(requestMap, ServeRequest.class);
                            assertEquals(PAYMENT_VALUE, serveRequest.getPaymentValue());
                            assertEquals(INTERNAL_VALUE, serveRequest.getInternalRequest().get(INTERNAL_KEY));
                            assertEquals(CALLBACK_URL, serveRequest.getCallbackUrl());
                            val serveResponse = serveRequest.createResponse();
                            serveResponse.succeed();
                            serveResponse.setInternalResponse(serveRequest.getInternalRequest());
                            serveResponse.setUnexpectedFailure(UNEXPECTED_FAILURE);
                            return new MockResponse().setBody(json(
                                    nonsenseSignature.sign(serveResponse)));

                        case "/bunny/serve-callback":
                            val serveCallbackRequest = spec(requestMap, ServeCallbackRequest.class);
                            assertEquals(INTERNAL_VALUE, serveCallbackRequest.getInternalRequest().get(INTERNAL_KEY));
                            assertEquals(SEQ_ID, serveCallbackRequest.getSeqId());
                            val serveCallbackResponse = serveCallbackRequest.createResponse();
                            serveCallbackResponse.succeed();
                            serveCallbackResponse.setUnexpectedFailure(UNEXPECTED_FAILURE);
                            return new MockResponse().setBody(json(
                                    nonsenseSignature.sign(serveCallbackResponse)));

                        default:
                            return new MockResponse()
                                    .setResponseCode(HttpStatus.NOT_FOUND.value())
                                    .setBody(HttpStatus.NOT_FOUND.getReasonPhrase());
                    }
                }
            });
            mockWebServer.start(22114);

            val calculateRequest = new CalculateRequest();
            calculateRequest.setServeName("calculate");
            calculateRequest.setChargingParameters(of("key1", "value1"));
            val calculateResponse = bunnyOhClient.request(calculateRequest);
            assertEquals(calculateRequest.getServeName(), calculateResponse.getServeName());
            assertTrue(calculateResponse.isSuccess());
            assertEquals(RESP_CODE_OK, calculateResponse.getRespCode());
            assertEquals(RESP_DESC_SUCCESS, calculateResponse.getRespDesc());
            assertEquals(CALCULATE_VALUE, calculateResponse.getCalculate());

            val chargeRequest = new ChargeRequest();
            chargeRequest.setServeName("charge");
            chargeRequest.setChargeValue(CHARGE_VALUE);
            val chargeResponse = bunnyOhClient.request(chargeRequest);
            assertEquals(chargeRequest.getServeName(), chargeResponse.getServeName());
            assertTrue(chargeResponse.isSuccess());
            assertEquals(RESP_CODE_OK, chargeResponse.getRespCode());
            assertEquals(RESP_DESC_SUCCESS, chargeResponse.getRespDesc());

            val queryRequest = new QueryRequest();
            queryRequest.setServeName("query");
            val queryResponse = bunnyOhClient.request(queryRequest);
            assertEquals(queryRequest.getServeName(), queryResponse.getServeName());
            assertTrue(queryResponse.isSuccess());
            assertEquals(RESP_CODE_OK, queryResponse.getRespCode());
            assertEquals(RESP_DESC_SUCCESS, queryResponse.getRespDesc());
            assertEquals(BALANCE_VALUE, queryResponse.getBalance());
            assertEquals(UNIT_VALUE, queryResponse.getUnit());

            val serveRequest = new ServeRequest();
            serveRequest.setServeName("serve");
            serveRequest.setPaymentValue(PAYMENT_VALUE);
            serveRequest.setInternalRequest(of(INTERNAL_KEY, INTERNAL_VALUE));
            serveRequest.setCallbackUrl(CALLBACK_URL);
            val serveResponse = bunnyOhClient.request(serveRequest);
            assertEquals(serveRequest.getServeName(), serveResponse.getServeName());
            assertTrue(serveResponse.isSuccess());
            assertEquals(RESP_CODE_OK, serveResponse.getRespCode());
            assertEquals(RESP_DESC_SUCCESS, serveResponse.getRespDesc());
            assertEquals(INTERNAL_VALUE, serveResponse.getInternalResponse().get(INTERNAL_KEY));
            assertEquals(UNEXPECTED_FAILURE, serveResponse.getUnexpectedFailure());

            val serveCallbackRequest = new ServeCallbackRequest();
            serveCallbackRequest.setServeName("serve");
            serveCallbackRequest.setInternalRequest(of(INTERNAL_KEY, INTERNAL_VALUE));
            serveCallbackRequest.setSeqId(SEQ_ID);
            val serveCallbackResponse = bunnyOhClient.request(serveCallbackRequest);
            assertEquals(serveCallbackRequest.getServeName(), serveCallbackResponse.getServeName());
            assertTrue(serveCallbackResponse.isSuccess());
            assertEquals(RESP_CODE_OK, serveCallbackResponse.getRespCode());
            assertEquals(RESP_DESC_SUCCESS, serveCallbackResponse.getRespDesc());
            assertEquals(UNEXPECTED_FAILURE, serveCallbackResponse.getUnexpectedFailure());
        }
    }

    @SneakyThrows
    public static void testErrorServer(BunnyOhClient bunnyOhClient) {
        try (val mockWebServer = new MockWebServer()) {
            mockWebServer.setDispatcher(new Dispatcher() {
                @Nonnull
                @Override
                public MockResponse dispatch(@Nonnull RecordedRequest request) {
                    val resp = new CalculateResponse();
                    resp.failed("ERROR", "FAILURE");
                    return new MockResponse().setBody(json(resp));
                }
            });
            mockWebServer.start(22115);

            val calculateRequest = new CalculateRequest();
            calculateRequest.setServeName("error");
            calculateRequest.setChargingParameters(new HashMap<>());
            val calculateResponse = bunnyOhClient.request(calculateRequest);
            assertFalse(calculateResponse.isSuccess());
            assertEquals("ERROR", calculateResponse.getRespCode());
            assertEquals("FAILURE", calculateResponse.getRespDesc());
        }
    }

    @SneakyThrows
    public static void testExceptionServer(BunnyOhClient bunnyOhClient) {
        try (val mockWebServer = new MockWebServer()) {
            mockWebServer.setDispatcher(new Dispatcher() {
                @Nonnull
                @Override
                public MockResponse dispatch(@Nonnull RecordedRequest request) {
                    if ("/exception/calculate".equals(request.getPath())) {
                        val resp = new CalculateResponse();
                        resp.setServeName("error");
                        resp.succeed();
                        resp.setCalculate(CALCULATE_VALUE);
                        return new MockResponse().setBody(json(resp));
                    } else {
                        return new MockResponse()
                                .setResponseCode(HttpStatus.NOT_FOUND.value())
                                .setBody(HttpStatus.NOT_FOUND.getReasonPhrase());
                    }
                }
            });
            mockWebServer.start(22116);

            try {
                val calculateRequest = new CalculateRequest();
                calculateRequest.setServeName("error");
                calculateRequest.setChargingParameters(new HashMap<>());
                bunnyOhClient.request(calculateRequest);
            } catch (BunnyOhClientException e) {
                assertEquals("Response verify failed", e.getMessage());
            }

            try {
                val chargeRequest = new ChargeRequest();
                chargeRequest.setServeName("error");
                chargeRequest.setChargeValue(CHARGE_VALUE);
                bunnyOhClient.request(chargeRequest);
            } catch (StatusError e) {
                assertEquals("Not Found", e.getMessage());
            }
        }
    }
}

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
import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClient;
import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClientException;
import com.github.charlemaznable.core.codec.NonsenseSignature;
import com.github.charlemaznable.core.net.common.StatusError;
import lombok.SneakyThrows;
import lombok.val;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

import static com.github.charlemaznable.bunny.client.domain.BunnyBaseResponse.RESP_CODE_OK;
import static com.github.charlemaznable.bunny.client.domain.BunnyBaseResponse.RESP_DESC_SUCCESS;
import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.codec.Json.spec;
import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.lang.Mapp.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MockOhClientServer {

    private static final int CALCULATE_VALUE = 12;
    private static final int CHARGE_VALUE = 34;
    private static final String PAYMENT_ID_VALUE = "paymentId";
    private static final int COMMIT_VALUE = 56;
    private static final int ROLLBACK_VALUE = 78;
    private static final int BALANCE_VALUE = 90;
    private static final String UNIT_VALUE = "unit";
    private static final NonsenseSignature nonsenseSignature = new NonsenseSignature();

    @SneakyThrows
    public static void testDefaultServer(BunnyOhClient bunnyOhClient) {
        try (val mockWebServer = new MockWebServer()) {
            mockWebServer.setDispatcher(new Dispatcher() {
                @Override
                public MockResponse dispatch(RecordedRequest request) {
                    val requestBody = request.getBody().readUtf8();
                    val requestMap = unJson(requestBody);
                    assertTrue(nonsenseSignature.verify(requestMap));

                    switch (request.getPath()) {
                        case "/bunny/calculate":
                            val req1 = spec(requestMap, CalculateRequest.class);
                            assertEquals("value1", req1.getChargingParameters().get("key1"));
                            val resp1 = new CalculateResponse();
                            resp1.setChargingType(req1.getChargingType());
                            resp1.setRespCode(RESP_CODE_OK);
                            resp1.setRespDesc(RESP_DESC_SUCCESS);
                            resp1.setCalculate(CALCULATE_VALUE);
                            resp1.setUnit(UNIT_VALUE);
                            return new MockResponse().setBody(json(
                                    nonsenseSignature.sign(resp1)));

                        case "/bunny/charge":
                            val req2 = spec(requestMap, ChargeRequest.class);
                            assertEquals(CHARGE_VALUE, req2.getChargeValue());
                            val resp2 = new ChargeResponse();
                            resp2.setChargingType(req2.getChargingType());
                            resp2.setRespCode(RESP_CODE_OK);
                            resp2.setRespDesc(RESP_DESC_SUCCESS);
                            return new MockResponse().setBody(json(
                                    nonsenseSignature.sign(resp2)));

                        case "/bunny/payment/advance":
                            val req3 = spec(requestMap, PaymentAdvanceRequest.class);
                            assertEquals(CALCULATE_VALUE, req3.getPaymentValue());
                            val resp3 = new PaymentAdvanceResponse();
                            resp3.setChargingType(req3.getChargingType());
                            resp3.setRespCode(RESP_CODE_OK);
                            resp3.setRespDesc(RESP_DESC_SUCCESS);
                            resp3.setPaymentId(PAYMENT_ID_VALUE);
                            return new MockResponse().setBody(json(
                                    nonsenseSignature.sign(resp3)));

                        case "/bunny/payment/commit":
                            val req4 = spec(requestMap, PaymentCommitRequest.class);
                            assertEquals(PAYMENT_ID_VALUE, req4.getPaymentId());
                            val resp4 = new PaymentCommitResponse();
                            resp4.setChargingType(req4.getChargingType());
                            resp4.setRespCode(RESP_CODE_OK);
                            resp4.setRespDesc(RESP_DESC_SUCCESS);
                            resp4.setCommit(COMMIT_VALUE);
                            resp4.setUnit(UNIT_VALUE);
                            return new MockResponse().setBody(json(
                                    nonsenseSignature.sign(resp4)));

                        case "/bunny/payment/rollback":
                            val req5 = spec(requestMap, PaymentRollbackRequest.class);
                            assertEquals(PAYMENT_ID_VALUE, req5.getPaymentId());
                            val resp5 = new PaymentRollbackResponse();
                            resp5.setChargingType(req5.getChargingType());
                            resp5.setRespCode(RESP_CODE_OK);
                            resp5.setRespDesc(RESP_DESC_SUCCESS);
                            resp5.setRollback(ROLLBACK_VALUE);
                            resp5.setUnit(UNIT_VALUE);
                            return new MockResponse().setBody(json(
                                    nonsenseSignature.sign(resp5)));

                        case "/bunny/query":
                            val req6 = spec(requestMap, QueryRequest.class);
                            val resp6 = new QueryResponse();
                            resp6.setChargingType(req6.getChargingType());
                            resp6.setRespCode(RESP_CODE_OK);
                            resp6.setRespDesc(RESP_DESC_SUCCESS);
                            resp6.setBalance(BALANCE_VALUE);
                            resp6.setUnit(UNIT_VALUE);
                            return new MockResponse().setBody(json(
                                    nonsenseSignature.sign(resp6)));

                        default:
                            return new MockResponse()
                                    .setResponseCode(HttpStatus.NOT_FOUND.value())
                                    .setBody(HttpStatus.NOT_FOUND.getReasonPhrase());
                    }
                }
            });
            mockWebServer.start(22114);

            val calculateRequest = new CalculateRequest();
            calculateRequest.setChargingType("calculate");
            calculateRequest.setChargingParameters(of("key1", "value1"));
            val calculateResponse = bunnyOhClient.request(calculateRequest);
            assertEquals(calculateRequest.getChargingType(), calculateResponse.getChargingType());
            assertTrue(calculateResponse.isSuccess());
            assertEquals(RESP_CODE_OK, calculateResponse.getRespCode());
            assertEquals(RESP_DESC_SUCCESS, calculateResponse.getRespDesc());
            assertEquals(CALCULATE_VALUE, calculateResponse.getCalculate());
            assertEquals(UNIT_VALUE, calculateResponse.getUnit());

            val chargeRequest = new ChargeRequest();
            chargeRequest.setChargingType("charge");
            chargeRequest.setChargeValue(CHARGE_VALUE);
            val chargeResponse = bunnyOhClient.request(chargeRequest);
            assertEquals(chargeRequest.getChargingType(), chargeResponse.getChargingType());
            assertTrue(chargeResponse.isSuccess());
            assertEquals(RESP_CODE_OK, chargeResponse.getRespCode());
            assertEquals(RESP_DESC_SUCCESS, chargeResponse.getRespDesc());

            val advanceRequest = new PaymentAdvanceRequest();
            advanceRequest.setChargingType("advance");
            advanceRequest.setPaymentValue(CALCULATE_VALUE);
            val advanceResponse = bunnyOhClient.request(advanceRequest);
            assertEquals(advanceRequest.getChargingType(), advanceResponse.getChargingType());
            assertTrue(advanceResponse.isSuccess());
            assertEquals(RESP_CODE_OK, advanceResponse.getRespCode());
            assertEquals(RESP_DESC_SUCCESS, advanceResponse.getRespDesc());
            assertEquals(PAYMENT_ID_VALUE, advanceResponse.getPaymentId());

            val commitRequest = new PaymentCommitRequest();
            commitRequest.setChargingType("commit");
            commitRequest.setPaymentId(PAYMENT_ID_VALUE);
            val commitResponse = bunnyOhClient.request(commitRequest);
            assertEquals(commitRequest.getChargingType(), commitResponse.getChargingType());
            assertTrue(commitResponse.isSuccess());
            assertEquals(RESP_CODE_OK, commitResponse.getRespCode());
            assertEquals(RESP_DESC_SUCCESS, commitResponse.getRespDesc());
            assertEquals(COMMIT_VALUE, commitResponse.getCommit());
            assertEquals(UNIT_VALUE, commitResponse.getUnit());

            val rollbackRequest = new PaymentRollbackRequest();
            rollbackRequest.setChargingType("rollback");
            rollbackRequest.setPaymentId(PAYMENT_ID_VALUE);
            val rollbackResponse = bunnyOhClient.request(rollbackRequest);
            assertEquals(rollbackRequest.getChargingType(), rollbackResponse.getChargingType());
            assertTrue(rollbackResponse.isSuccess());
            assertEquals(RESP_CODE_OK, rollbackResponse.getRespCode());
            assertEquals(RESP_DESC_SUCCESS, rollbackResponse.getRespDesc());
            assertEquals(ROLLBACK_VALUE, rollbackResponse.getRollback());
            assertEquals(UNIT_VALUE, rollbackResponse.getUnit());

            val queryRequest = new QueryRequest();
            queryRequest.setChargingType("query");
            val queryResponse = bunnyOhClient.request(queryRequest);
            assertEquals(queryRequest.getChargingType(), queryResponse.getChargingType());
            assertTrue(queryResponse.isSuccess());
            assertEquals(RESP_CODE_OK, queryResponse.getRespCode());
            assertEquals(RESP_DESC_SUCCESS, queryResponse.getRespDesc());
            assertEquals(BALANCE_VALUE, queryResponse.getBalance());
            assertEquals(UNIT_VALUE, queryResponse.getUnit());
        }
    }

    @SneakyThrows
    public static void testErrorServer(BunnyOhClient bunnyOhClient) {
        try (val mockWebServer = new MockWebServer()) {
            mockWebServer.setDispatcher(new Dispatcher() {
                @Override
                public MockResponse dispatch(RecordedRequest request) {
                    val resp = new CalculateResponse();
                    resp.setRespCode("ERROR");
                    resp.setRespDesc("FAILURE");
                    return new MockResponse().setBody(json(resp));
                }
            });
            mockWebServer.start(22115);

            val calculateRequest = new CalculateRequest();
            calculateRequest.setChargingType("error");
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
                @Override
                public MockResponse dispatch(RecordedRequest request) {
                    if ("/exception/calculate".equals(request.getPath())) {
                        val resp = new CalculateResponse();
                        resp.setChargingType("error");
                        resp.setRespCode(RESP_CODE_OK);
                        resp.setRespDesc(RESP_DESC_SUCCESS);
                        resp.setCalculate(CALCULATE_VALUE);
                        resp.setUnit(UNIT_VALUE);
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
                calculateRequest.setChargingType("error");
                calculateRequest.setChargingParameters(new HashMap<>());
                bunnyOhClient.request(calculateRequest);
            } catch (BunnyOhClientException e) {
                assertEquals("Response verify failed", e.getMessage());
            }

            try {
                val chargeRequest = new ChargeRequest();
                chargeRequest.setChargingType("error");
                chargeRequest.setChargeValue(CHARGE_VALUE);
                bunnyOhClient.request(chargeRequest);
            } catch (StatusError e) {
                assertEquals("Not Found", e.getMessage());
            }
        }
    }
}

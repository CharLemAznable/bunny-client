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
import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClient;
import com.github.charlemaznable.bunny.client.ohclient.BunnyOhClientException;
import com.github.charlemaznable.core.codec.NonsenseSignature;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MockOhClientServer {

    private static final String PAYMENT_ID = "paymentId";
    private static final String BALANCE = "balance";
    private static final String UNIT = "unit";
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
                            resp1.setCalcResult("calcResult");
                            resp1.setCalcResultUnit("calcResultUnit");
                            return new MockResponse().setBody(json(
                                    nonsenseSignature.sign(resp1)));

                        case "/bunny/charge":
                            val req2 = spec(requestMap, ChargeRequest.class);
                            assertEquals("chargeValue", req2.getChargeValue());
                            val resp2 = new ChargeResponse();
                            resp2.setChargingType(req2.getChargingType());
                            resp2.setRespCode(RESP_CODE_OK);
                            resp2.setRespDesc(RESP_DESC_SUCCESS);
                            resp2.setBalance(BALANCE);
                            resp2.setBalanceUnit("balanceUnit");
                            return new MockResponse().setBody(json(
                                    nonsenseSignature.sign(resp2)));

                        case "/bunny/payment/advance":
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
                            return new MockResponse().setBody(json(
                                    nonsenseSignature.sign(resp3)));

                        case "/bunny/payment/commit":
                            val req4 = spec(requestMap, PaymentCommitRequest.class);
                            assertEquals(PAYMENT_ID, req4.getPaymentId());
                            val resp4 = new PaymentCommitResponse();
                            resp4.setChargingType(req4.getChargingType());
                            resp4.setRespCode(RESP_CODE_OK);
                            resp4.setRespDesc(RESP_DESC_SUCCESS);
                            resp4.setCommitValue("commitValue");
                            resp4.setBalance(BALANCE);
                            resp4.setUnit(UNIT);
                            return new MockResponse().setBody(json(
                                    nonsenseSignature.sign(resp4)));

                        case "/bunny/payment/rollback":
                            val req5 = spec(requestMap, PaymentRollbackRequest.class);
                            assertEquals(PAYMENT_ID, req5.getPaymentId());
                            val resp5 = new PaymentRollbackResponse();
                            resp5.setChargingType(req5.getChargingType());
                            resp5.setRespCode(RESP_CODE_OK);
                            resp5.setRespDesc(RESP_DESC_SUCCESS);
                            resp5.setRollbackValue("rollbackValue");
                            resp5.setBalance(BALANCE);
                            resp5.setUnit(UNIT);
                            return new MockResponse().setBody(json(
                                    nonsenseSignature.sign(resp5)));

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
            val calculateResponse = bunnyOhClient.calculate(calculateRequest);
            assertEquals(calculateRequest.getChargingType(), calculateResponse.getChargingType());
            assertEquals(RESP_CODE_OK, calculateResponse.getRespCode());
            assertEquals(RESP_DESC_SUCCESS, calculateResponse.getRespDesc());
            assertEquals("calcResult", calculateResponse.getCalcResult());
            assertEquals("calcResultUnit", calculateResponse.getCalcResultUnit());

            val chargeRequest = new ChargeRequest();
            chargeRequest.setChargeValue("charge");
            chargeRequest.setChargeValue("chargeValue");
            val chargeResponse = bunnyOhClient.charge(chargeRequest);
            assertEquals(chargeRequest.getChargingType(), chargeResponse.getChargingType());
            assertEquals(RESP_CODE_OK, chargeResponse.getRespCode());
            assertEquals(RESP_DESC_SUCCESS, chargeResponse.getRespDesc());
            assertEquals(BALANCE, chargeResponse.getBalance());
            assertEquals("balanceUnit", chargeResponse.getBalanceUnit());

            val advanceRequest = new PaymentAdvanceRequest();
            advanceRequest.setChargingType("advance");
            advanceRequest.setChargingParameters(of("key2", "value2"));
            val advanceResponse = bunnyOhClient.paymentAdvance(advanceRequest);
            assertEquals(advanceRequest.getChargingType(), advanceResponse.getChargingType());
            assertEquals(RESP_CODE_OK, advanceResponse.getRespCode());
            assertEquals(RESP_DESC_SUCCESS, advanceResponse.getRespDesc());
            assertEquals(PAYMENT_ID, advanceResponse.getPaymentId());
            assertEquals("paymentValue", advanceResponse.getPaymentValue());
            assertEquals(BALANCE, advanceResponse.getBalance());
            assertEquals(UNIT, advanceResponse.getUnit());

            val commitRequest = new PaymentCommitRequest();
            commitRequest.setChargingType("commit");
            commitRequest.setPaymentId(PAYMENT_ID);
            val commitResponse = bunnyOhClient.paymentCommit(commitRequest);
            assertEquals(commitRequest.getChargingType(), commitResponse.getChargingType());
            assertEquals(RESP_CODE_OK, commitResponse.getRespCode());
            assertEquals(RESP_DESC_SUCCESS, commitResponse.getRespDesc());
            assertEquals("commitValue", commitResponse.getCommitValue());
            assertEquals(BALANCE, commitResponse.getBalance());
            assertEquals(UNIT, commitResponse.getUnit());

            val rollbackRequest = new PaymentRollbackRequest();
            rollbackRequest.setChargingType("rollback");
            rollbackRequest.setPaymentId(PAYMENT_ID);
            val rollbackResponse = bunnyOhClient.paymentRollback(rollbackRequest);
            assertEquals(rollbackRequest.getChargingType(), rollbackResponse.getChargingType());
            assertEquals(RESP_CODE_OK, rollbackResponse.getRespCode());
            assertEquals(RESP_DESC_SUCCESS, rollbackResponse.getRespDesc());
            assertEquals("rollbackValue", rollbackResponse.getRollbackValue());
            assertEquals(BALANCE, rollbackResponse.getBalance());
            assertEquals(UNIT, rollbackResponse.getUnit());
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
            calculateRequest.setChargingType("calculate");
            calculateRequest.setChargingParameters(new HashMap<>());
            val calculateResponse = bunnyOhClient.calculate(calculateRequest);
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
                    val resp = new CalculateResponse();
                    resp.setChargingType("exception");
                    resp.setRespCode("OK");
                    resp.setRespDesc("SUCCESS");
                    resp.setCalcResult("calcResult");
                    resp.setCalcResultUnit("calcResultUnit");
                    return new MockResponse().setBody(json(resp));
                }
            });
            mockWebServer.start(22116);

            try {
                val calculateRequest = new CalculateRequest();
                calculateRequest.setChargingType("calculate");
                calculateRequest.setChargingParameters(new HashMap<>());
                bunnyOhClient.calculate(calculateRequest);
            } catch (BunnyOhClientException e) {
                assertEquals("Response verify failed", e.getMessage());
            }
        }
    }
}
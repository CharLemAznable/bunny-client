package com.github.charlemaznable.bunny.clienttest.spring.errorconfig;

import com.github.charlemaznable.bunny.client.domain.CalculateRequest;
import com.github.charlemaznable.bunny.client.domain.CalculateResponse;
import com.github.charlemaznable.bunny.client.spring.BunnyHttpClient;
import com.github.charlemaznable.bunny.client.spring.BunnyHttpClientException;
import lombok.SneakyThrows;
import lombok.val;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;

import static com.github.charlemaznable.core.codec.Json.json;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = BunnyHttpClientErrorConfiguration.class)
public class BunnyHttpClientErrorTest {

    @Autowired
    private BunnyHttpClient bunnyHttpClient;

    @SneakyThrows
    @Test
    public void testBunnyHttpClientError() {
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
            val calculateResponse = bunnyHttpClient.calculate(calculateRequest);
            assertEquals("ERROR", calculateResponse.getRespCode());
            assertEquals("FAILURE", calculateResponse.getRespDesc());
        }
    }

    @SneakyThrows
    @Test
    public void testBunnyHttpClientException() {
        try (val mockWebServer = new MockWebServer()) {
            mockWebServer.setDispatcher(new Dispatcher() {
                @Override
                public MockResponse dispatch(RecordedRequest request) {
                    val resp = new CalculateResponse();
                    resp.setChargingType("error");
                    resp.setRespCode("OK");
                    resp.setRespDesc("SUCCESS");
                    resp.setCalcResult("calcResult");
                    resp.setCalcResultUnit("calcResultUnit");
                    return new MockResponse().setBody(json(resp));
                }
            });
            mockWebServer.start(22115);

            try {
                val calculateRequest = new CalculateRequest();
                calculateRequest.setChargingType("calculate");
                calculateRequest.setChargingParameters(new HashMap<>());
                bunnyHttpClient.calculate(calculateRequest);
            } catch (BunnyHttpClientException e) {
                assertEquals("Response verify failed", e.getMessage());
            }
        }
    }
}

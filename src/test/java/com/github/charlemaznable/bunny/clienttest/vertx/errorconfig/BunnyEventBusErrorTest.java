package com.github.charlemaznable.bunny.clienttest.vertx.errorconfig;

import com.github.charlemaznable.bunny.client.domain.CalculateRequest;
import com.github.charlemaznable.bunny.client.domain.CalculateResponse;
import com.github.charlemaznable.bunny.client.domain.ChargeRequest;
import com.github.charlemaznable.bunny.client.domain.PaymentAdvanceRequest;
import com.github.charlemaznable.bunny.client.domain.PaymentAdvanceResponse;
import com.github.charlemaznable.bunny.client.vertx.BunnyEventBus;
import com.github.charlemaznable.bunny.client.vertx.BunnyEventBusException;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;

import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.lang.Listt.newArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ExtendWith({SpringExtension.class, VertxExtension.class})
@ContextConfiguration(classes = BunnyEventBusErrorConfiguration.class)
public class BunnyEventBusErrorTest {

    @Autowired
    private Vertx vertx;
    @Autowired
    private BunnyEventBus bunnyEventBus;

    @Test
    public void testBunnyEventBusError(VertxTestContext test) {
        val eventBus = vertx.eventBus();
        eventBus.<String>consumer("/rabbit/calculate", message -> {
            val resp = new CalculateResponse();
            resp.setChargingType("error");
            resp.setRespCode("OK");
            resp.setRespDesc("SUCCESS");
            resp.setCalcResult("calcResult");
            resp.setCalcResultUnit("calcResultUnit");
            message.reply(json(resp));
        });
        eventBus.<String>consumer("/rabbit/payment/advance", message -> {
            val resp = new PaymentAdvanceResponse();
            resp.setRespCode("ERROR");
            resp.setRespDesc("FAILURE");
            message.reply(json(resp));
        });

        CompositeFuture.all(newArrayList(
                Future.<Void>future(f -> {
                    val calculateRequest = new CalculateRequest();
                    calculateRequest.setChargingType("calculate");
                    calculateRequest.setChargingParameters(new HashMap<>());
                    bunnyEventBus.calculate(calculateRequest, async -> test.verify(() -> {
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
                    bunnyEventBus.charge(chargeRequest, async -> test.verify(() -> {
                        assertTrue(async.failed());
                        val cause = async.cause();
                        assertTrue(cause instanceof ReplyException);
                        assertEquals("No handlers for address /rabbit/charge", cause.getMessage());
                        f.complete();
                    }));
                }),
                Future.<Void>future(f -> {
                    val advanceRequest = new PaymentAdvanceRequest();
                    advanceRequest.setChargingType("advance");
                    advanceRequest.setChargingParameters(new HashMap<>());
                    bunnyEventBus.paymentAdvance(advanceRequest, async -> test.verify(() -> {
                        val advanceResponse = async.result();
                        assertEquals("ERROR", advanceResponse.getRespCode());
                        assertEquals("FAILURE", advanceResponse.getRespDesc());
                        f.complete();
                    }));
                })
        )).setHandler(result -> test.<CompositeFuture>completing().handle(result));
    }
}

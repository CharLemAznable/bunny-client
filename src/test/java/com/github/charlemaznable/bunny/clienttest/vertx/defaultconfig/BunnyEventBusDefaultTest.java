package com.github.charlemaznable.bunny.clienttest.vertx.defaultconfig;

import com.github.charlemaznable.bunny.client.domain.CalculateRequest;
import com.github.charlemaznable.bunny.client.domain.ChargeRequest;
import com.github.charlemaznable.bunny.client.domain.PaymentAdvanceRequest;
import com.github.charlemaznable.bunny.client.domain.PaymentCommitRequest;
import com.github.charlemaznable.bunny.client.domain.PaymentRollbackRequest;
import com.github.charlemaznable.bunny.client.vertx.BunnyEventBus;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.github.charlemaznable.bunny.client.domain.BunnyBaseResponse.RESP_CODE_OK;
import static com.github.charlemaznable.bunny.client.domain.BunnyBaseResponse.RESP_DESC_SUCCESS;
import static com.github.charlemaznable.bunny.clienttest.vertx.defaultconfig.BunnyEventBusDefaultConfiguration.BALANCE;
import static com.github.charlemaznable.bunny.clienttest.vertx.defaultconfig.BunnyEventBusDefaultConfiguration.PAYMENT_ID;
import static com.github.charlemaznable.bunny.clienttest.vertx.defaultconfig.BunnyEventBusDefaultConfiguration.READY;
import static com.github.charlemaznable.bunny.clienttest.vertx.defaultconfig.BunnyEventBusDefaultConfiguration.UNIT;
import static com.github.charlemaznable.core.lang.Listt.newArrayList;
import static com.github.charlemaznable.core.lang.Mapp.of;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ExtendWith({SpringExtension.class, VertxExtension.class})
@ContextConfiguration(classes = BunnyEventBusDefaultConfiguration.class)
public class BunnyEventBusDefaultTest {

    @Autowired
    private BunnyEventBus bunnyEventBus;

    @Test
    public void testBunnyEventBus(VertxTestContext test) {
        await().forever().until(() -> READY);

        CompositeFuture.all(newArrayList(
                Future.<Void>future(f -> {
                    val calculateRequest = new CalculateRequest();
                    calculateRequest.setChargingType("calculate");
                    calculateRequest.setChargingParameters(of("key1", "value1"));
                    bunnyEventBus.calculate(calculateRequest, async -> test.verify(() -> {
                        val calculateResponse = async.result();
                        assertEquals(calculateRequest.getChargingType(), calculateResponse.getChargingType());
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
                    bunnyEventBus.charge(chargeRequest, async -> test.verify(() -> {
                        val chargeResponse = async.result();
                        assertEquals(chargeRequest.getChargingType(), chargeResponse.getChargingType());
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
                    bunnyEventBus.paymentAdvance(advanceRequest, async -> test.verify(() -> {
                        val advanceResponse = async.result();
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
                    bunnyEventBus.paymentCommit(commitRequest, async -> test.verify(() -> {
                        val commitResponse = async.result();
                        assertEquals(commitRequest.getChargingType(), commitResponse.getChargingType());
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
                    bunnyEventBus.paymentRollback(rollbackRequest, async -> test.verify(() -> {
                        val rollbackResponse = async.result();
                        assertEquals(rollbackRequest.getChargingType(), rollbackResponse.getChargingType());
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
}

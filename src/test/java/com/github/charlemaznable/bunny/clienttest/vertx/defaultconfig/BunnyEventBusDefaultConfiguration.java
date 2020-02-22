package com.github.charlemaznable.bunny.clienttest.vertx.defaultconfig;

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
import com.github.charlemaznable.bunny.client.vertx.VertxBunnyImport;
import com.github.charlemaznable.bunny.client.vertx.VertxWiredEvent;
import com.github.charlemaznable.core.codec.NonsenseSignature;
import com.github.charlemaznable.core.miner.MinerFactory;
import com.github.charlemaznable.core.net.ohclient.OhFactory;
import io.vertx.core.eventbus.Message;
import lombok.val;
import org.n3r.diamond.client.impl.MockDiamondServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;

import static com.github.charlemaznable.bunny.client.domain.BunnyBaseResponse.RESP_CODE_OK;
import static com.github.charlemaznable.bunny.client.domain.BunnyBaseResponse.RESP_DESC_SUCCESS;
import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.codec.Json.spec;
import static com.github.charlemaznable.core.codec.Json.unJson;
import static org.joor.Reflect.onClass;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@VertxBunnyImport
@Configuration
public class BunnyEventBusDefaultConfiguration {

    static boolean READY = false;
    static final String PAYMENT_ID = "paymentId";
    static final String BALANCE = "balance";
    static final String UNIT = "unit";
    private NonsenseSignature nonsenseSignature = new NonsenseSignature();

    @PostConstruct
    public void postConstruct() {
        onClass(OhFactory.class).field("ohCache").call("invalidateAll");
        onClass(MinerFactory.class).field("minerCache").call("invalidateAll");
        MockDiamondServer.setUpMockServer();
    }

    @PreDestroy
    public void preDestroy() {
        MockDiamondServer.tearDownMockServer();
    }

    @EventListener
    public void initialEventBus(VertxWiredEvent event) {
        val eventBus = event.vertx().eventBus();
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

        READY = true;
    }

    private Map<String, Object> verifyRequestMap(Message<String> message) {
        val requestBody = message.body();
        val requestMap = unJson(requestBody);
        assertTrue(nonsenseSignature.verify(requestMap));
        return requestMap;
    }
}

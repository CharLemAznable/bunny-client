package com.github.charlemaznable.bunny.clienttest.vertx.errorconfig;

import com.github.charlemaznable.bunny.client.config.BunnyClientConfig;
import com.github.charlemaznable.bunny.client.domain.CalculateResponse;
import com.github.charlemaznable.bunny.client.domain.PaymentAdvanceResponse;
import com.github.charlemaznable.bunny.client.vertx.VertxBunnyImport;
import com.github.charlemaznable.bunny.client.vertx.VertxWiredEvent;
import com.github.charlemaznable.core.miner.MinerFactory;
import com.github.charlemaznable.core.net.ohclient.OhFactory;
import lombok.val;
import org.n3r.diamond.client.impl.MockDiamondServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static com.github.charlemaznable.core.codec.Json.json;
import static org.joor.Reflect.onClass;

@VertxBunnyImport
@Configuration
public class BunnyEventBusErrorConfiguration {

    static boolean READY = false;

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

    @Bean
    public BunnyClientConfig bunnyClientConfig() {
        return new BunnyClientConfig() {
            @Override
            public String httpServerBaseUrl() {
                return "http://127.0.0.1:22115/bunny";
            }

            @Override
            public String eventBusAddressPrefix() {
                return "/rabbit";
            }
        };
    }

    @EventListener
    public void initialEventBus(VertxWiredEvent event) {
        val eventBus = event.vertx().eventBus();
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

        READY = true;
    }
}


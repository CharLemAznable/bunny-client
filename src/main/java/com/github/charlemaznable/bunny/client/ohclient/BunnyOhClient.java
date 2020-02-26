package com.github.charlemaznable.bunny.client.ohclient;

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
import com.github.charlemaznable.core.net.common.ContentFormat;
import com.github.charlemaznable.core.net.common.HttpMethod;
import com.github.charlemaznable.core.net.common.Mapping;
import com.github.charlemaznable.core.net.common.ParameterBundle;
import com.github.charlemaznable.core.net.common.RequestMethod;
import com.github.charlemaznable.core.net.common.ResponseParse;
import com.github.charlemaznable.core.net.ohclient.OhClient;

@OhClient
@Mapping(urlProvider = BunnyOhClientUrlProvider.class)
@RequestMethod(HttpMethod.POST)
@ContentFormat(BunnyOhClientContentFormatter.class)
@ResponseParse(BunnyOhClientResponseParser.class)
public interface BunnyOhClient {

    @Mapping("/calculate")
    CalculateResponse calculate(@ParameterBundle CalculateRequest request);

    @Mapping("/charge")
    ChargeResponse charge(@ParameterBundle ChargeRequest request);

    @Mapping("/payment/advance")
    PaymentAdvanceResponse paymentAdvance(@ParameterBundle PaymentAdvanceRequest request);

    @Mapping("/payment/commit")
    PaymentCommitResponse paymentCommit(@ParameterBundle PaymentCommitRequest request);

    @Mapping("/payment/rollback")
    PaymentRollbackResponse paymentRollback(@ParameterBundle PaymentRollbackRequest request);
}

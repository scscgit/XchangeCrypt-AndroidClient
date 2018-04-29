package io.swagger.client.auth;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import io.swagger.client.ApiException;
import io.swagger.client.api.TradingPanelBridgeBrokerDataOrdersApi;

/**
 * Created by Peter on 26.04.2018.
 */

public class FastAccessTokenApiKey extends ApiKeyAuth {
    public FastAccessTokenApiKey() {
        super("header", "authorization");
    }

    @Override
    public void setApiKey(String apiKey) {
        super.setApiKey("Bearer "+apiKey);
    }
}

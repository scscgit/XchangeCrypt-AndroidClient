package bit.xchangecrypt.client.network;

import android.util.Log;
import android.widget.Toast;
import bit.xchangecrypt.client.R;
import bit.xchangecrypt.client.datamodel.Order;
import bit.xchangecrypt.client.datamodel.User;
import bit.xchangecrypt.client.exceptions.TradingException;
import bit.xchangecrypt.client.ui.MainActivity;
import io.swagger.client.ApiException;
import io.swagger.client.api.TradingPanelBrokerDataBridgeApi;
import io.swagger.client.api.TradingPanelOrdersBridgeApi;
import io.swagger.client.api.UserBridgeApi;
import io.swagger.client.model.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Peter on 29.04.2018.
 */
public class TradingApiHelper {
    public static List<String> API_TRADING_DOMAINS = Arrays.asList(
        "https://convergenceservice-xchangecrypt.azurewebsites.net/api/v1/trading",
        "https://192.168.0.20/api/v1/trading",
//        "https://192.168.0.20:8000/api/v1/trading",
        "https://192.168.8.101/api/v1/trading"
//        "https://rest-demo.tradingview.com/tradingview/v1",
    );

    public static List<String> API_USER_DOMAINS = Arrays.asList(
        "https://convergenceservice-xchangecrypt.azurewebsites.net/api/v1/user",
        "https://192.168.0.20/api/v1/user",
//        "https://192.168.0.20:8000/api/v1/user",
        "https://192.168.8.101/api/v1/user"
//        "https://rest-demo.tradingview.com/tradingview/v1/fake-invalid-api",
    );

    // API helper context
    private TradingPanelBrokerDataBridgeApi tradingBrokerApi;
    private TradingPanelOrdersBridgeApi tradingOrdersApi;
    private UserBridgeApi userApi;
    private MainActivity mainActivity;
    private int apiDomainIndex;

    public void tryNextDomain() {
        mainActivity.runOnUiThread(() -> {
            Toast.makeText(mainActivity,
                mainActivity.getString(R.string.refresher_debug_trading_api_connection_failed, API_TRADING_DOMAINS.get(apiDomainIndex)),
                Toast.LENGTH_LONG
            ).show();
        });

        if (apiDomainIndex + 1 >= API_TRADING_DOMAINS.size()) {
            apiDomainIndex = 0;
        } else {
            apiDomainIndex++;
        }
        Log.e(TradingApiHelper.class.getSimpleName(), "Switched trading API to " + API_TRADING_DOMAINS.get(apiDomainIndex));
    }

    public TradingApiHelper(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void createTradingBrokerApi() {
        if (tradingBrokerApi == null) {
            this.tradingBrokerApi = new TradingPanelBrokerDataBridgeApi(API_TRADING_DOMAINS.get(apiDomainIndex));
        }
    }

    public void createTradingOrdersApi() {
        if (tradingOrdersApi == null) {
            this.tradingOrdersApi = new TradingPanelOrdersBridgeApi(API_TRADING_DOMAINS.get(apiDomainIndex));
        }
    }

    public void createUserApi() {
        if (userApi == null) {
            this.userApi = new UserBridgeApi(API_USER_DOMAINS.get(apiDomainIndex));
        }
    }

    public List<WalletDetails> accountBalance(final User user) {
        createUserApi();
        List<WalletDetails> accountWalletResponse = null;
        try {
            accountWalletResponse = userApi.wallets(user.getAccountId());
        } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
            e.printStackTrace();
            throw new TradingException("Cannot get user balance data", e);
        }
        if (accountWalletResponse != null) {
            return accountWalletResponse;
        } else {
            throw new TradingException("execution");
        }
    }

    public List<Instrument> instruments(final User user) {
        createTradingOrdersApi();
        InlineResponse20011 inlineResponse20011 = null;
        try {
            // By default, offline user uses accountId "0"
            inlineResponse20011 = tradingOrdersApi.accountsAccountIdInstrumentsGet(user == null ? "0" : user.getAccountId());
        } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
            e.printStackTrace();
            throw new TradingException("Cannot get trading data per account " + (user == null ? "0" : user.getAccountId()), e);
        }
        if (inlineResponse20011 != null) {
            if (inlineResponse20011.getS() == InlineResponse20011.SEnum.ok) {
                return inlineResponse20011.getD();
            } else {
                throw new TradingException("return " + inlineResponse20011.getErrmsg());
            }
        } else {
            throw new TradingException("execution");
        }
    }

    public Depth marketDepthForPair(final String pair) throws TradingException {
        createTradingOrdersApi();
        InlineResponse20013 inlineResponse20013 = null;
        try {
            inlineResponse20013 = tradingOrdersApi.depthGet(pair);
        } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
            e.printStackTrace();
            throw new TradingException("Cannot get trading data", e);
        }
        if (inlineResponse20013 != null) {
            if (inlineResponse20013.getS() == InlineResponse20013.SEnum.ok) {
                return inlineResponse20013.getD();
            } else {
                throw new TradingException("return " + inlineResponse20013.getErrmsg());
            }
        } else {
            throw new TradingException("execution");
        }
    }

    public BarsArrays historyBars(final String pair, final String resolution, Long from, Long to, Integer countback) {
        createTradingBrokerApi();
        BarsArrays response = null;
        try {
            response = tradingBrokerApi.historyGet(
                pair,
                resolution,
                from == null ? null : (double) from,
                to == null ? null : (double) to,
                countback == null ? null : (double) countback
            );
        } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
            e.printStackTrace();
            throw new TradingException("Cannot get trading data", e);
        }
        if (response != null) {
            if (response.getS() == BarsArrays.SEnum.ok) {
                return response;
            } else {
                throw new TradingException("return " + response.getErrmsg());
            }
        } else {
            throw new TradingException("execution");
        }
    }

    public List<Execution> accountExecutions(final User user, final String pair, final int maxCount) {
        createTradingOrdersApi();
        InlineResponse20010 inlineResponse20010 = null;
        try {
            inlineResponse20010 = tradingOrdersApi.accountsAccountIdExecutionsGet(user.getAccountId(), pair, BigDecimal.valueOf(maxCount).intValue());
        } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
            e.printStackTrace();
            throw new TradingException("Cannot get trading data per account " + user.getAccountId(), e);
        }
        if (inlineResponse20010 != null) {
            if (inlineResponse20010.getS() == InlineResponse20010.SEnum.ok) {
                return inlineResponse20010.getD();
            } else {
                throw new TradingException("return " + inlineResponse20010.getErrmsg());
            }
        } else {
            throw new TradingException("execution");
        }
    }

    public List<io.swagger.client.model.Order> accountOrders(final User user) {
        createTradingOrdersApi();
        InlineResponse2004 inlineResponse2004 = null;
        try {
            inlineResponse2004 = tradingOrdersApi.accountsAccountIdOrdersGet(user.getAccountId());
        } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
            e.printStackTrace();
            throw new TradingException("Cannot get trading data per account " + user.getAccountId(), e);
        }
        if (inlineResponse2004 != null) {
            if (inlineResponse2004.getS() == InlineResponse2004.SEnum.ok) {
                return inlineResponse2004.getD();
            } else {
                throw new TradingException("return " + inlineResponse2004.getErrmsg());
            }
        } else {
            throw new TradingException("execution");
        }
    }

    public List<io.swagger.client.model.Order> accountOrdersHistory(final User user, final int count) {
        createTradingOrdersApi();
        InlineResponse2004 response = null;
        try {
            response = tradingOrdersApi.accountsAccountIdOrdersHistoryGet(user.getAccountId(), new BigDecimal(count).doubleValue());
        } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
            e.printStackTrace();
            throw new TradingException("Cannot get trading order history for account " + user.getAccountId(), e);
        }
        if (response != null) {
            if (response.getS() == InlineResponse2004.SEnum.ok) {
                return response.getD();
            } else {
                throw new TradingException("return " + response.getErrmsg());
            }
        } else {
            throw new TradingException("execution");
        }
    }

    public void sendTradingOffer(final Order offer) {
        createTradingOrdersApi();
        BigDecimal limitPrice = null;
        BigDecimal stopPrice = null;
        switch (offer.getType()) {
            case MARKET:
                limitPrice = null;
                stopPrice = null;
                break;
            case LIMIT:
                limitPrice = BigDecimal.valueOf(offer.getLimitPrice());
                break;
            case STOP:
                stopPrice = BigDecimal.valueOf(offer.getStopPrice());
                break;
            case STOPLIMIT:
                limitPrice = BigDecimal.valueOf(offer.getLimitPrice());
                stopPrice = BigDecimal.valueOf(offer.getStopPrice());
        }
        InlineResponse2005 response = null;
        BigDecimal stopLoss = null;
        BigDecimal takeProfit = null;
        if (offer.getStopLoss() != null) {
            stopLoss = BigDecimal.valueOf(offer.getStopLoss());
        }
        if (offer.getTakeProfit() != null) {
            takeProfit = BigDecimal.valueOf(offer.getTakeProfit());
        }
        try {
            response = tradingOrdersApi.accountsAccountIdOrdersPost(
                mainActivity.getContentProvider().getUser().getAccountId(),
                offer.getBaseCurrency() + "_" + offer.getQuoteCurrency(),
                BigDecimal.valueOf(offer.getBaseCurrencyAmount()).doubleValue(),
                offer.getSide().toString(),
                offer.getType().toString(),
                limitPrice == null ? null : limitPrice.doubleValue(),
                stopPrice == null ? null : stopPrice.doubleValue(),
                null,
                null,
                stopLoss == null ? null : stopLoss.doubleValue(),
                takeProfit == null ? null : takeProfit.doubleValue(),
                null,
                null
            );
        } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
            e.printStackTrace();
            throw new TradingException("Cannot send trading offer", e);
        }
        if (response != null) {
            if (response.getS() == InlineResponse2005.SEnum.ok) {
                offer.setOrderId(response.getD().getOrderId());
            } else {
                throw new TradingException("return " + response.getErrmsg());
            }
        } else {
            throw new TradingException("execution");
        }
    }

    public void deleteTradingOffer(final String orderId) {
        createTradingOrdersApi();
        InlineResponse2007 response = null;
        try {
            response = tradingOrdersApi.accountsAccountIdOrdersOrderIdDelete(
                mainActivity.getContentProvider().getUser().getAccountId(),
                orderId
            );
        } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
            e.printStackTrace();
            throw new TradingException("Cannot delete trade offer", e);
        }
        if (response != null) {
            if (response.getS() != InlineResponse2007.SEnum.ok) {
                throw new TradingException("return " + response.getErrmsg());
            }
        } else {
            throw new TradingException("execution");
        }
    }

    public void generateWallet(final String coinSymbol) {
        createUserApi();
        String errorResponse = null;
        try {
            errorResponse = userApi.walletGenerate(
                mainActivity.getContentProvider().getUser().getAccountId(),
                coinSymbol
            );
        } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
            e.printStackTrace();
            throw new TradingException("Cannot generate wallet", e);
        }
        if (errorResponse != null && !"".equals(errorResponse)) {
            throw new TradingException("return " + errorResponse);
        }
    }

    public AuthorizationResponse authorize(final User user) {
        createTradingOrdersApi();
        mainActivity.showProgressDialog("Prebieha overenie identity");
        InlineResponse200 response = null;
        try {
            response = tradingOrdersApi.authorizePost(user.getLogin(), user.getPassword());
        } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
            e.printStackTrace();
            throw new TradingException("Cannot authorize user " + user.getLogin() + " with id " + user.getAccountId(), e);
        }
        if (response != null) {
            if (response.getS() == InlineResponse200.SEnum.ok) {
                user.setAccessToken(response.getD().getAccessToken());
                user.setExpiration(response.getD().getExpiration().doubleValue());
                return response.getD();
            } else {
                throw new TradingException("return " + response.getErrmsg());
            }
        } else {
            throw new TradingException("execution");
        }
    }
}

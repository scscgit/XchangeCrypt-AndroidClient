package bit.xchangecrypt.client.network;

import android.widget.Toast;
import bit.xchangecrypt.client.datamodel.Order;
import bit.xchangecrypt.client.datamodel.User;
import bit.xchangecrypt.client.exceptions.TradingException;
import bit.xchangecrypt.client.ui.MainActivity;
import io.swagger.client.ApiException;
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
        "https://192.168.0.20/api/v1/trading",
        "https://192.168.0.20:8000/api/v1/trading",
        "https://192.168.8.101/api/v1/trading",
        "https://rest-demo.tradingview.com/tradingview/v1",
        "https://xchangecrypttest-convergencebackend.azurewebsites.net/api/v1/trading"
    );

    public static List<String> API_USER_DOMAINS = Arrays.asList(
        "https://192.168.0.20/api/v1/user",
        "https://192.168.0.20:8000/api/v1/user",
        "https://192.168.8.101/api/v1/user",
        "https://rest-demo.tradingview.com/tradingview/v1/fake-invalid-api",
        "https://xchangecrypttest-convergencebackend.azurewebsites.net/api/v1/user"
    );

    // API helper context
    private TradingPanelOrdersBridgeApi tradingApi;
    private UserBridgeApi userApi;
    private MainActivity mainActivity;
    private int apiDomainIndex;

    public void tryNextDomain() {
        mainActivity.runOnUiThread(() -> {
            Toast.makeText(mainActivity,
                "Connection to trading API " + API_TRADING_DOMAINS.get(apiDomainIndex) + " failed, trying next domain",
                Toast.LENGTH_SHORT).show();
        });

        if (apiDomainIndex + 1 >= API_TRADING_DOMAINS.size()) {
            apiDomainIndex = 0;
        } else {
            apiDomainIndex++;
        }
    }

    public TradingApiHelper(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void createTradingApi() {
        if (tradingApi == null) {
            this.tradingApi = new TradingPanelOrdersBridgeApi(API_TRADING_DOMAINS.get(apiDomainIndex));
        }
    }

    public void createUserApi() {
        if (userApi == null) {
            this.userApi = new UserBridgeApi(API_USER_DOMAINS.get(apiDomainIndex));
        }
    }

    public List<WalletDetails> accountBalance() {
        createUserApi();
        List<WalletDetails> accountWalletResponse = null;
        try {
            accountWalletResponse = userApi.wallets();
        } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
            e.printStackTrace();
            throw new TradingException("Cannot get user balance data");
        }
        if (accountWalletResponse != null) {
            return accountWalletResponse;
        } else {
            throw new TradingException("execution");
        }
    }

    public List<Instrument> instruments(final User user) {
        createTradingApi();
        InlineResponse20011 inlineResponse20011 = null;
        try {
            inlineResponse20011 = tradingApi.accountsAccountIdInstrumentsGet(user.getAccountId());
        } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
            e.printStackTrace();
            throw new TradingException("Cannot get trading data per account " + user.getAccountId(), e);
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

    public List<Execution> accountExecutions(final User user, final String pair, final int maxCount) {
        createTradingApi();
        InlineResponse20010 inlineResponse20010 = null;
        try {
            inlineResponse20010 = tradingApi.accountsAccountIdExecutionsGet(user.getAccountId(), pair, BigDecimal.valueOf(maxCount).intValue());
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
        createTradingApi();
        InlineResponse2004 inlineResponse2004 = null;
        try {
            inlineResponse2004 = tradingApi.accountsAccountIdOrdersGet(user.getAccountId());
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

    public Depth marketDepthForPair(final String pair) throws TradingException {
        createTradingApi();
        InlineResponse20013 inlineResponse20013 = null;
        try {
            inlineResponse20013 = tradingApi.depthGet(pair);
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

    public void sendTradingOffer(final Order offer) {
        createTradingApi();
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
            response = tradingApi.accountsAccountIdOrdersPost(
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

    public void deleteTradingOffer(final Order offer) {
        createTradingApi();
        InlineResponse2007 response = null;
        try {
            response = tradingApi.accountsAccountIdOrdersOrderIdDelete(
                mainActivity.getContentProvider().getUser().getAccountId(),
                offer.getOrderId()
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

    public List<io.swagger.client.model.Order> accountOrdersHistory(final User user, final int count) {
        createTradingApi();
        InlineResponse2004 response = null;
        try {
            response = tradingApi.accountsAccountIdOrdersHistoryGet(user.getAccountId(), new BigDecimal(count).doubleValue());
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

    public AuthorizationResponse authorize(final User user) {
        createTradingApi();
        mainActivity.showProgressDialog("Prebieha overenie identity");
        InlineResponse200 response = null;
        try {
            response = tradingApi.authorizePost(user.getLogin(), user.getPassword());
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

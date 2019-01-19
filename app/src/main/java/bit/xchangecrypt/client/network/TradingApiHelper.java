package bit.xchangecrypt.client.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import bit.xchangecrypt.client.datamodel.Order;
import bit.xchangecrypt.client.datamodel.User;
import bit.xchangecrypt.client.ui.MainActivity;
import io.swagger.client.ApiException;
import io.swagger.client.ApiInvoker;
import io.swagger.client.api.TradingPanelOrdersBridgeApi;
import io.swagger.client.api.UserBridgeApi;
import io.swagger.client.auth.ApiKeyAuth;
import io.swagger.client.model.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Peter on 29.04.2018.
 */
public class TradingApiHelper {
    public static List<String> API_TRADING_DOMAINS = Arrays.asList(
        "https://192.168.0.20/api/v1/trading",
        "https://192.168.8.101/api/v1/trading",
        "https://rest-demo.tradingview.com/tradingview/v1",
        "https://xchangecrypttest-convergencebackend.azurewebsites.net/api/v1/trading"
    );

    public static List<String> API_USER_DOMAINS = Arrays.asList(
        "https://192.168.0.20/api/v1/user",
        "https://192.168.8.101/api/v1/user",
        "https://rest-demo.tradingview.com/tradingview/v1/fake-invalid-api",
        "https://xchangecrypttest-convergencebackend.azurewebsites.net/api/v1/user"
    );

    // API helper context
    private TradingPanelOrdersBridgeApi tradingApi;
    private UserBridgeApi userApi;
    private MainActivity mainActivity;
    private Context context;
    private List<Integer> pendingTask;
    private int apiDomainIndex;

    // Asynchronously loaded responses
    private InlineResponse200 authorizationResponses;
    private HashMap<String, InlineResponse2004> accountOrders;
    private HashMap<String, InlineResponse2004> historyAccountOrders;
    private HashMap<String, InlineResponse20013> depthData;
    private List<WalletDetails> walletDetails;
    private List<Instrument> instruments;
    private HashMap<String, List<Execution>> executionHashMap = new HashMap<>();

    public TradingApiHelper(MainActivity mainActivity, Context context) {
        this.context = context;
        this.mainActivity = mainActivity;
        this.accountOrders = new HashMap<>();
        this.depthData = new HashMap<>();
        this.pendingTask = new CopyOnWriteArrayList<>();
        this.historyAccountOrders = new HashMap<>();
    }

    public ApiKeyAuth getApiAuthentication() {
        return (ApiKeyAuth) ApiInvoker.getInstance().getAuthentication("Bearer");
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

    public InlineResponse200 getAuthorizationResponses() {
        return authorizationResponses;
    }

    public InlineResponse2004 getAccountOrders(String accountId) {
        return accountOrders.get(accountId);
    }

    public InlineResponse2004 getHistoryAccountOrders(String accountId) {
        return historyAccountOrders.get(accountId);
    }

    public void deleteFromPendingTask(int taskId) {
        pendingTask.remove((Integer) taskId);
    }

    public boolean noPendingTask() {
        return pendingTask.isEmpty();
    }

    public InlineResponse20013 getDepthData(String pair) {
        return depthData.get(pair);
    }

    public List<WalletDetails> getWalletDetailsAccounts() {
        return walletDetails;
    }

    public List<Instrument> getInstruments() {
        return instruments;
    }

    public List<Execution> getExecution(String pair) {
        return executionHashMap.get(pair);
    }

    public void putExecutions(String pair, List<Execution> executions) {
        executionHashMap.put(pair, executions);
    }

    public void accountBalance(final int taskId) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Void, Void, List<WalletDetails>>() {
            @Override
            protected List<WalletDetails> doInBackground(Void... voids) {
//                mainActivity.showProgressDialog("Načítavám dáta");
                createUserApi();
                pendingTask.add(taskId);
                List<WalletDetails> accountWalletResponse = null;
                try {
                    accountWalletResponse = userApi.wallets();
                } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
                    //throw new TradingException("Cannot get trading data per account " + user.getAccountId(), e);
                    e.printStackTrace();
                    Intent i = new Intent("account_wallet_update");
                    i.putExtra("error", "execution");
                    i.putExtra("taskId", taskId);
                    context.sendBroadcast(i);
                }
                return accountWalletResponse;
            }

            @Override
            protected void onPostExecute(List<WalletDetails> accountWalletResponse) {
                Intent i = new Intent("account_wallet_update");
                if (accountWalletResponse != null) {
                    walletDetails = accountWalletResponse;
                } else {
                    i.putExtra("error", "execution");
                }
                i.putExtra("taskId", taskId);
                context.sendBroadcast(i);
            }
        }.execute();
    }

    public void instrument(final int taskId, final User user) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Void, Void, InlineResponse20011>() {
            @Override
            protected InlineResponse20011 doInBackground(Void... voids) {
//                mainActivity.showProgressDialog("Načítavám dáta");
                createTradingApi();
                pendingTask.add(taskId);
                InlineResponse20011 inlineResponse20011 = null;
                try {
                    inlineResponse20011 = tradingApi.accountsAccountIdInstrumentsGet(user.getAccountId());
                } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
                    //throw new TradingException("Cannot get trading data per account " + user.getAccountId(), e);
                    e.printStackTrace();
                    Intent i = new Intent("account_instruments");
                    i.putExtra("error", "execution");
                    i.putExtra("accountId", user.getAccountId());
                    i.putExtra("taskId", taskId);
                    context.sendBroadcast(i);
                }
                return inlineResponse20011;
            }

            @Override
            protected void onPostExecute(InlineResponse20011 inlineResponse20011) {
                Intent i = new Intent("account_instruments");
                if (inlineResponse20011 != null) {
                    if (inlineResponse20011.getS() == InlineResponse20011.SEnum.ok) {
                        instruments = inlineResponse20011.getD();
                    } else {
                        i.putExtra("error", "return " + inlineResponse20011.getErrmsg());
                    }
                } else {
                    i.putExtra("error", "execution");
                }
                i.putExtra("accountId", user.getAccountId());
                i.putExtra("taskId", taskId);
                context.sendBroadcast(i);
            }
        }.execute();
    }

    public void accountTransactionHistoryForPair(final int taskId, final User user, final String pair, final int maxCount) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Void, Void, InlineResponse20010>() {
            @Override
            protected InlineResponse20010 doInBackground(Void... voids) {
//                mainActivity.showProgressDialog("Načítavám dáta");
                createTradingApi();
                pendingTask.add(taskId);
                InlineResponse20010 inlineResponse20010 = null;
                try {
                    inlineResponse20010 = tradingApi.accountsAccountIdExecutionsGet(user.getAccountId(), pair, BigDecimal.valueOf(maxCount).intValue());
                } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
                    //throw new TradingException("Cannot get trading data per account " + user.getAccountId(), e);
                    e.printStackTrace();
                    Intent i = new Intent("account_executions");
                    i.putExtra("error", "execution");
                    i.putExtra("accountId", user.getAccountId());
                    i.putExtra("pair", pair);
                    i.putExtra("taskId", taskId);
                    context.sendBroadcast(i);
                }
                return inlineResponse20010;
            }

            @Override
            protected void onPostExecute(InlineResponse20010 inlineResponse20010) {
                Intent i = new Intent("account_executions");
                if (inlineResponse20010 != null) {
                    if (inlineResponse20010.getS() == InlineResponse20010.SEnum.ok) {
                        putExecutions(pair, inlineResponse20010.getD());
                    } else {
                        i.putExtra("error", "return " + inlineResponse20010.getErrmsg());
                    }
                } else {
                    i.putExtra("error", "execution");
                }
                i.putExtra("accountId", user.getAccountId());
                i.putExtra("taskId", taskId);
                i.putExtra("pair", pair);
                context.sendBroadcast(i);
            }
        }.execute();
    }

    public void accountOrderHistory(final int taskId, final User user) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Void, Void, InlineResponse2004>() {
            @Override
            protected InlineResponse2004 doInBackground(Void... voids) {
//                mainActivity.showProgressDialog("Načítavám dáta");
                createTradingApi();
                pendingTask.add(taskId);
                InlineResponse2004 inlineResponse2004 = null;
                try {
                    inlineResponse2004 = tradingApi.accountsAccountIdOrdersGet(user.getAccountId());
                } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
                    //throw new TradingException("Cannot get trading data per account " + user.getAccountId(), e);
                    e.printStackTrace();
                    Intent i = new Intent("account_offer_update");
                    i.putExtra("error", "execution");
                    i.putExtra("accountId", user.getAccountId());
                    i.putExtra("taskId", taskId);
                    context.sendBroadcast(i);
                }
                return inlineResponse2004;
            }

            @Override
            protected void onPostExecute(InlineResponse2004 inlineResponse2004) {
                Intent i = new Intent("account_offer_update");
                if (inlineResponse2004 != null) {
                    if (inlineResponse2004.getS() == InlineResponse2004.SEnum.ok) {
                        accountOrders.put(user.getAccountId(), inlineResponse2004);
                    } else {
                        i.putExtra("error", "return " + inlineResponse2004.getErrmsg());
                    }
                } else {
                    i.putExtra("error", "execution");
                }
                i.putExtra("accountId", user.getAccountId());
                i.putExtra("taskId", taskId);
                context.sendBroadcast(i);
            }
        }.execute();
    }

    public void marketDepthForPair(final int taskId, final String pair) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask depthTask = new AsyncTask<Void, Void, InlineResponse20013>() {
            @Override
            protected InlineResponse20013 doInBackground(Void... voids) {
                //               mainActivity.showProgressDialog("Načítavám dáta");
                createTradingApi();
                pendingTask.add(taskId);
                InlineResponse20013 inlineResponse20013 = null;
                try {
                    inlineResponse20013 = tradingApi.depthGet(pair);
                } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
                    //throw new TradingException("Cannot get trading data", e);
                    e.printStackTrace();
                }
                return inlineResponse20013;
            }

            @Override
            protected void onPostExecute(InlineResponse20013 inlineResponse20013) {
                Intent i = new Intent("depth_update");
                if (inlineResponse20013 != null) {
                    if (inlineResponse20013.getS() == InlineResponse20013.SEnum.ok) {
                        depthData.put(pair, inlineResponse20013);
                    } else {
                        i.putExtra("error", "return " + inlineResponse20013.getErrmsg());
                    }
                } else {
                    i.putExtra("error", "execution");
                }
                i.putExtra("pair", pair);
                i.putExtra("taskId", taskId);
                context.sendBroadcast(i);
            }
        }.execute();
    }

    public void sendTradingOffer(final int taskId, final Order offer) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask depthTask = new AsyncTask<Void, Void, InlineResponse2005>() {
            @Override
            protected InlineResponse2005 doInBackground(Void... offers) {
                //               mainActivity.showProgressDialog("Načítavám dáta");
                createTradingApi();
                pendingTask.add(taskId);
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
//String accountId, String instrument, BigDecimal qty, String side, String type, BigDecimal limitPrice, BigDecimal stopPrice, String durationType, BigDecimal durationDateTime, BigDecimal stopLoss, BigDecimal takeProfit, String digitalSignature, String requestId)
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
                    //throw new TradingException("Cannot send trading offer", e);
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            protected void onPostExecute(InlineResponse2005 inlineResponse2005) {
                Intent i = new Intent("order_send");
                if (inlineResponse2005 != null) {
                    if (inlineResponse2005.getS() == InlineResponse2005.SEnum.ok) {
                        offer.setOrderId(inlineResponse2005.getD().getOrderId());
                    } else {
                        i.putExtra("error", "return " + inlineResponse2005.getErrmsg());
                    }
                } else {
                    i.putExtra("error", "execution");
                }
                i.putExtra("taskId", taskId);
                context.sendBroadcast(i);
            }
        }.execute();
    }

    public void deleteTradingOffer(final int taskId, final Order offer, final User user) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask depthTask = new AsyncTask<Void, Void, InlineResponse2007>() {
            @Override
            protected InlineResponse2007 doInBackground(Void... offers) {
                //               mainActivity.showProgressDialog("Načítavám dáta");
                pendingTask.add(taskId);

                InlineResponse2007 response = null;
                try {
                    response = tradingApi.accountsAccountIdOrdersOrderIdDelete(user.getAccountId(), offer.getOrderId());
                } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
                    // throw new TradingException("Cannot send trading order", e);
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            protected void onPostExecute(InlineResponse2007 inlineResponse2007) {
                Intent i = new Intent("order_delete");
                if (inlineResponse2007 != null) {
                    if (inlineResponse2007.getS() != InlineResponse2007.SEnum.ok) {
                        i.putExtra("error", "return " + inlineResponse2007.getErrmsg());
                    }
                } else {
                    i.putExtra("error", "execution");
                }
                i.putExtra("orderId", offer.getOrderId());
                i.putExtra("taskId", taskId);
                context.sendBroadcast(i);
            }
        }.execute();
    }

    public void transactionHistoryForAccount(final int taskId, final User user, final int count) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask depthTask = new AsyncTask<Void, Void, InlineResponse2004>() {
            @Override
            protected InlineResponse2004 doInBackground(Void... voids) {
                //               mainActivity.showProgressDialog("Načítavám dáta");
                pendingTask.add(taskId);
                InlineResponse2004 response = null;
                try {
                    response = tradingApi.accountsAccountIdOrdersHistoryGet(user.getAccountId(), new BigDecimal(count).doubleValue());
                } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
                    //throw new TradingException("Cannot get trading order history for account " + user.getAccountId(), e);
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            protected void onPostExecute(InlineResponse2004 inlineResponse2004) {
                Intent i = new Intent("account_history_update");
                if (inlineResponse2004 != null) {
                    if (inlineResponse2004.getS() == InlineResponse2004.SEnum.ok) {
                        historyAccountOrders.put(user.getAccountId(), inlineResponse2004);
                    } else {
                        i.putExtra("error", "return " + inlineResponse2004.getErrmsg());
                    }
                } else {
                    i.putExtra("error", "execution");
                }
                i.putExtra("taskId", taskId);
                context.sendBroadcast(i);
            }
        }.execute();
    }

    public void authorize(final int taskId, final User user) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask depthTask = new AsyncTask<Void, Void, InlineResponse200>() {
            @Override
            protected InlineResponse200 doInBackground(Void... voids) {
                //             mainActivity.showProgressDialog("Načítavám dáta");
                pendingTask.add(taskId);
                InlineResponse200 response = null;
                try {
                    response = tradingApi.authorizePost(user.getLogin(), user.getPassword());
                } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
                    //throw new TradingException("Cannot authorize user " + user.getLogin() + " with id " + user.getAccountId(), e);
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            protected void onPostExecute(InlineResponse200 inlineResponse200) {
                Intent i = new Intent("auth_update");
                if (inlineResponse200 != null) {
                    if (inlineResponse200.getS() == InlineResponse200.SEnum.ok) {
                        authorizationResponses = inlineResponse200;
                        user.setAccessToken(inlineResponse200.getD().getAccessToken());
                        user.setExpiration(inlineResponse200.getD().getExpiration().doubleValue());
                    } else {
                        i.putExtra("error", "return " + inlineResponse200.getErrmsg());
                    }
                } else {
                    i.putExtra("error", "execution");
                }
                i.putExtra("taskId", taskId);
                context.sendBroadcast(i);
            }
        }.execute();
    }
}

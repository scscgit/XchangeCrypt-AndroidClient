package cloud.coders.sk.xchangecrypt.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import cloud.coders.sk.xchangecrypt.datamodel.Order;
import cloud.coders.sk.xchangecrypt.datamodel.OrderType;
import cloud.coders.sk.xchangecrypt.datamodel.User;
import cloud.coders.sk.xchangecrypt.exceptions.TradingException;
import cloud.coders.sk.xchangecrypt.ui.MainActivity;
import io.swagger.client.ApiException;
import io.swagger.client.api.TradingPanelBridgeBrokerDataOrdersApi;
import io.swagger.client.model.InlineResponse200;
import io.swagger.client.model.InlineResponse20013;
import io.swagger.client.model.InlineResponse2004;
import io.swagger.client.model.InlineResponse2005;
import io.swagger.client.model.InlineResponse2007;

/**
 * Created by Peter on 29.04.2018.
 */

public class TradingApiHelper {

    private TradingPanelBridgeBrokerDataOrdersApi tradingApi;
    private MainActivity mainActivity;
    private Context context;
    private List<Integer> pendingTask;

    public TradingApiHelper(MainActivity mainActivity, Context context) {
       // this.tradingApi = new TradingPanelBridgeBrokerDataOrdersApi();
        this.context = context;
        this.mainActivity = mainActivity;
        this.accountOrders = new HashMap<>();
        this.depthData = new HashMap<>();
        this.pendingTask = new ArrayList<>();
    }

    public void createTradingApi(){
        if (tradingApi == null) {
            this.tradingApi = new TradingPanelBridgeBrokerDataOrdersApi();
        }
    }

    private InlineResponse200 authorizationResponses;

    public InlineResponse200 getAuthorizationResponses() {
        return authorizationResponses;
    }

    private HashMap<String, InlineResponse2004> accountOrders;

    public InlineResponse2004 getAccountOrders(String accountId) {
        return accountOrders.get(accountId);
    }

    private HashMap<String, InlineResponse2004> historyAccountOrders;

    public InlineResponse2004 getHistoryAccountOrders(String accountId) {
        return accountOrders.get(accountId);
    }

    private HashMap<String, InlineResponse20013> depthData;

    public void deleteFromPendingTask(int taskId){
        pendingTask.remove(taskId);
    }

    public boolean noPendingTask(){
        return pendingTask.isEmpty();
    }


    public InlineResponse20013 getDepthData(String pair) {
        return depthData.get(pair);
    }

    //0
    public void tradingOffersPerAccount(final int taskId, final User user) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Void, Void, InlineResponse2004>() {
            @Override
            protected InlineResponse2004 doInBackground(Void... voids) {
                createTradingApi();
                pendingTask.add(taskId);
                InlineResponse2004 inlineResponse2004 = null;
                try {
                    inlineResponse2004 = tradingApi.accountsAccountIdOrdersGet(user.getAccountId());
                } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
                    throw new TradingException("Cannot get trading data per account " + user.getAccountId(), e);
                }
                return inlineResponse2004;
            }

            @Override
            protected void onPostExecute(InlineResponse2004 response2004) {
                // mainActivity.addToAccountOrders(accountId,response2004);
                accountOrders.put(user.getAccountId(), response2004);
                Intent i = new Intent("account_offer_update");
                i.putExtra("accountId", user.getAccountId());
                i.putExtra("taskId", taskId);
                context.sendBroadcast(i);
            }
        }.execute();
    }

    //FX_IDC:ZARPLX
    public void tradingOffersForCurrencyPair(final int taskId, final String pair) {

        @SuppressLint("StaticFieldLeak")
        AsyncTask depthTask = new AsyncTask<Void, Void, InlineResponse20013>() {

            @Override
            protected InlineResponse20013 doInBackground(Void... voids) {
                createTradingApi();
                pendingTask.add(taskId);
                InlineResponse20013 inlineResponse20013 = null;
                try {
                    inlineResponse20013 = tradingApi.depthGet(pair);
                } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
                    throw new TradingException("Cannot get trading data", e);
                }

                return inlineResponse20013;
            }

            @Override
            protected void onPostExecute(InlineResponse20013 inlineResponse20013) {
                //mainActivity.addToDepthData(pair,inlineResponse20013);
                depthData.put(pair, inlineResponse20013);
                Intent i = new Intent("depth_update");
                i.putExtra("pair", pair);
                i.putExtra("taskId", taskId);
                context.sendBroadcast(i);
            }
        }.execute();
    }

    public void sendTradingOffer(final int taskId, final Order offer) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask depthTask = new AsyncTask<Order, Void, InlineResponse2005>() {

            @Override
            protected InlineResponse2005 doInBackground(Order... offers) {
                createTradingApi();
                pendingTask.add(taskId);
                Order offer = offers[0];

                Double limitPrice = null;
                if (offer.getType() == OrderType.limit) {
                    limitPrice = offer.getPrice();
                }
                Double stopPrice = null;
                if (offer.getType() == OrderType.stop) {
                    stopPrice = offer.getPrice();
                }
                InlineResponse2005 response = null;
                try {
                    response = tradingApi.accountsAccountIdOrdersPost(
                            mainActivity.getContentProvider().getUser().getAccountId(),
                            offer.getBaseCurrency() + ":" + offer.getQuoteCurrency(),
                            new BigDecimal(1.0),
                            offer.getSide().toString(),
                            offer.getType().toString(),
                            new BigDecimal(limitPrice),
                            new BigDecimal(stopPrice),
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                    );
                } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
                    throw new TradingException("Cannot send trading order", e);
                }
                return response;
            }

            @Override
            protected void onPostExecute(InlineResponse2005 response2005) {
                //mainActivity.addToDepthData(pair,inlineResponse20013);
                offer.setOrderId(response2005.getD().getOrderId());
                Intent i = new Intent("order_send");
                i.putExtra("taskId", taskId);
                context.sendBroadcast(i);
            }
        }.execute();
    }

    public void deleteTradingOffer(final int taskId, final Order offer, final User user) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask depthTask = new AsyncTask<Order, Void, InlineResponse2007>() {

            @Override
            protected InlineResponse2007 doInBackground(Order... offers) {
                pendingTask.add(taskId);
                Order offer = offers[0];

                InlineResponse2007 response = null;
                try {
                    response = tradingApi.accountsAccountIdOrdersOrderIdDelete(user.getAccountId(), offer.getOrderId());
                } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
                    throw new TradingException("Cannot send trading order", e);
                }
                return response;
            }

            @Override
            protected void onPostExecute(InlineResponse2007 response2007) {
                System.out.print("S: " + response2007.getS());
                System.out.print("Err: " + response2007.getErrmsg());
                Intent i = new Intent("order_delete");
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
                pendingTask.add(taskId);
                InlineResponse2004 response = null;
                try {
                    response = tradingApi.accountsAccountIdOrdersHistoryGet(user.getAccountId(), new BigDecimal(count));
                } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
                    throw new TradingException("Cannot get trading order history for account " + user.getAccountId(), e);
                }
                return response;
            }

            @Override
            protected void onPostExecute(InlineResponse2004 inlineResponse2004) {
                historyAccountOrders.put(user.getAccountId(), inlineResponse2004);
                Intent i = new Intent("account_history_update");
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
                pendingTask.add(taskId);
                InlineResponse200 response = null;
                try {
                    response = tradingApi.authorizePost(user.getLogin(), user.getPassword());
                } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
                    throw new TradingException("Cannot authorize user " + user.getLogin() + " with id " + user.getAccountId(), e);
                }
                return response;
            }

            @Override
            protected void onPostExecute(InlineResponse200 inlineResponse200) {
                authorizationResponses = inlineResponse200;
                user.setAccessToken(inlineResponse200.getD().getAccessToken());
                user.setExpiration(inlineResponse200.getD().getExpiration().doubleValue());
                Intent i = new Intent("auth_update");
                i.putExtra("taskId", taskId);
                context.sendBroadcast(i);
            }
        }.execute();
    }

}

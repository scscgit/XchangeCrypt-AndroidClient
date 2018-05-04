package cloud.coders.sk.xchangecrypt.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
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
        //this.tradingApi = new TradingPanelBridgeBrokerDataOrdersApi();
        initializeTradingApi();
        this.context = context;
        this.mainActivity = mainActivity;
        this.accountOrders = new HashMap<>();
        this.depthData = new HashMap<>();
        this.pendingTask = new CopyOnWriteArrayList<>();
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
        pendingTask.remove((Integer)taskId);
    }

    public boolean noPendingTask(){
        return pendingTask.isEmpty();
    }


    public InlineResponse20013 getDepthData(String pair) {
        return depthData.get(pair);
    }


    public void initializeTradingApi() {
        @SuppressLint("StaticFieldLeak")
        AsyncTask task = new AsyncTask<Void, Void, TradingPanelBridgeBrokerDataOrdersApi>() {
            @Override
            protected TradingPanelBridgeBrokerDataOrdersApi doInBackground(Void... voids) {
//                mainActivity.showProgressDialog("Načítavám dáta");
                createTradingApi();
                //pendingTask.add(taskId);
                InlineResponse2004 inlineResponse2004 = null;
                TradingPanelBridgeBrokerDataOrdersApi api;
//                try {
//                    api = new TradingPanelBridgeBrokerDataOrdersApi();
//                } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
//                    throw new TradingException("Cannot initialize trading api" , e);
//                }
                return api = new TradingPanelBridgeBrokerDataOrdersApi();
            }

            @Override
            protected void onPostExecute(TradingPanelBridgeBrokerDataOrdersApi api) {
                  tradingApi = api;
                //                accountOrders.put(user.getAccountId(), response2004);
//                Intent i = new Intent("account_offer_update");
//                i.putExtra("accountId", user.getAccountId());
//                i.putExtra("taskId", taskId);
//                context.sendBroadcast(i);
            }
        }.execute();
    }



    //0
    public void tradingOffersPerAccount(final int taskId, final User user) {
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
                    if (inlineResponse2004.getS().equals("ok")) {
                        accountOrders.put(user.getAccountId(), inlineResponse2004);
                    }else {
                        i.putExtra("error", "return " + inlineResponse2004.getErrmsg());
                    }
                }
                else {
                    i.putExtra("error", "execution");
                }
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
                    if (inlineResponse20013.getS().equals("ok")) {
                        depthData.put(pair, inlineResponse20013);
                    }else {
                        i.putExtra("error", "return " + inlineResponse20013.getErrmsg());
                    }
                }
                else {
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
                Order order = offer;

                double price = 0;
                BigDecimal limitPrice = null;
                BigDecimal stopPrice = null;
                switch(order.getType()){
                    case market:
                        limitPrice = null;
                        stopPrice = null;
                        break;
                    case limit:
                        limitPrice = BigDecimal.valueOf(order.getLimitPrice());
                        break;
                    case stop:
                        stopPrice = BigDecimal.valueOf(order.getStopPrice());
                        break;
                    case stoplimit:
                        limitPrice = BigDecimal.valueOf(order.getLimitPrice());
                        stopPrice = BigDecimal.valueOf(order.getStopPrice());
                }
                InlineResponse2005 response = null;
//String accountId, String instrument, BigDecimal qty, String side, String type, BigDecimal limitPrice, BigDecimal stopPrice, String durationType, BigDecimal durationDateTime, BigDecimal stopLoss, BigDecimal takeProfit, String digitalSignature, String requestId)
                BigDecimal stopLoss = null;
                BigDecimal takeProfit = null;
                if (order.getStopLoss() != null){
                    stopLoss = BigDecimal.valueOf(order.getStopLoss());
                }
                if (order.getTakeProfit() != null){
                    takeProfit = BigDecimal.valueOf(order.getTakeProfit());
                }
                try {
                    response = tradingApi.accountsAccountIdOrdersPost(
                            mainActivity.getContentProvider().getUser().getAccountId(),
                            offer.getBaseCurrency() + "_" + offer.getQuoteCurrency(),
                            BigDecimal.valueOf(order.getBaseCurrencyAmount()),
                            offer.getSide().toString(),
                            offer.getType().toString(),
                            limitPrice,
                            stopPrice,
                            null,
                            null,
                            stopLoss,
                            takeProfit,
                            null,
                            null
                    );
                } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
                    //throw new TradingException("Cannot send trading order", e);
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            protected void onPostExecute(InlineResponse2005 inlineResponse2005) {
                Intent i = new Intent("order_send");
                if (inlineResponse2005 != null) {
                    if (inlineResponse2005.getS().equals("ok")) {
                        offer.setOrderId(inlineResponse2005.getD().getOrderId());
                    }else {
                        i.putExtra("error", "return " + inlineResponse2005.getErrmsg());
                    }
                }
                else {
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
                    if (!inlineResponse2007.getS().equals("ok")) {
                        i.putExtra("error", "return " + inlineResponse2007.getErrmsg());
                    }
                }
                else {
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
                    response = tradingApi.accountsAccountIdOrdersHistoryGet(user.getAccountId(), new BigDecimal(count));
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
                    if (inlineResponse2004.getS().equals("ok")) {
                        historyAccountOrders.put(user.getAccountId(), inlineResponse2004);
                    }else {
                        i.putExtra("error", "return " + inlineResponse2004.getErrmsg());
                    }
                }
                else {
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
                    if (inlineResponse200.getS().equals("ok")) {
                        authorizationResponses = inlineResponse200;
                        user.setAccessToken(inlineResponse200.getD().getAccessToken());
                        user.setExpiration(inlineResponse200.getD().getExpiration().doubleValue());
                    }else {
                        i.putExtra("error", "return " + inlineResponse200.getErrmsg());
                    }
                }
                else {
                    i.putExtra("error", "execution");
                }
                i.putExtra("taskId", taskId);
                context.sendBroadcast(i);
            }
        }.execute();
    }

}

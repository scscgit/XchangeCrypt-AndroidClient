package cloud.coders.sk.xchangecrypt.core;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cloud.coders.sk.xchangecrypt.datamodel.Coin;
import cloud.coders.sk.xchangecrypt.datamodel.CurrencyPair;
import cloud.coders.sk.xchangecrypt.datamodel.MyTransaction;
import cloud.coders.sk.xchangecrypt.datamodel.Order;
import cloud.coders.sk.xchangecrypt.datamodel.OrderSide;
import cloud.coders.sk.xchangecrypt.datamodel.User;
import cloud.coders.sk.xchangecrypt.utils.InternalStorage;


/**
 * Created by V3502484 on 16. 9. 2016.
 */
public class ContentProvider {
    private static ContentProvider instance;
    private static Context context;

    private final String transactionHistoryTag = "TransactionHistory";
    private final String coinsBalanceTag = "Balance";
    private final String marketOrdersTag = "MarketOrders";
    private final String accountOrdersTag = "AccountOrders";

    private List<MyTransaction> accountTransactionHistory;
    private List<Coin> coinsBalance;
    private HashMap<String, List<Order>> marketOrders;
    private List<Order> accountOrders;

    private CurrencyPair actualCurrencyPair;
    private OrderSide currentOrderSide;


    private User user;

    private HashMap<String, Double> marketPrices;

    private ContentProvider(Context context) {
        ContentProvider.context = context;
        accountTransactionHistory = new ArrayList<>();
        coinsBalance = new ArrayList<>();
        marketOrders = new HashMap<>();
        marketPrices = new HashMap<>();
    }

    public static ContentProvider getInstance(Context context) {
        if (instance == null)
            instance = new ContentProvider(context);
        return instance;
    }

    public void destroy() {
        instance = null;
    }

    public void releaseLoadedData() {
      accountTransactionHistory = null;
      coinsBalance = null;
      marketOrders = null;
      accountOrders = null;
    }

    @SuppressWarnings("unchecked")
    public void  loadContentFromCache() {
        List<MyTransaction> cachedAccountTransaction = (List<MyTransaction>) InternalStorage.readObject(context, transactionHistoryTag+user.getAccountId());
        if (cachedAccountTransaction != null) {
            accountTransactionHistory = cachedAccountTransaction;
        }

        List<Coin> cachedcoins = (List<Coin>) InternalStorage.readObject(context, coinsBalanceTag+user.getAccountId());
        if (cachedcoins != null) {
            coinsBalance = cachedcoins;
        }

        HashMap<String, List<Order>> cachedMarketOrders = (HashMap<String, List<Order>>) InternalStorage.readObject(context, marketOrdersTag);
        if (cachedMarketOrders != null) {
            marketOrders = cachedMarketOrders;
        }

        List<Order> cahedAccountOrder = (List<Order>) InternalStorage.readObject(context, accountOrdersTag+user.getAccountId());
        if (cachedAccountTransaction != null) {
            accountOrders = cahedAccountOrder;
        }

    }

    public void saveAccountHistory(){
        InternalStorage.writeObject(context, transactionHistoryTag+user.getAccountId(), accountTransactionHistory);
    }

    public void saveCoinsBalance(){
        InternalStorage.writeObject(context, coinsBalanceTag+user.getAccountId(), coinsBalance);
    }

    public void saveMarketOrders(){
        InternalStorage.writeObject(context, marketOrdersTag, marketOrders);
    }

    public void saveAccountOrders(){
        InternalStorage.writeObject(context, accountOrdersTag+user.getAccountId(), accountOrders);
    }

    public List<MyTransaction> getAccountTransactionHistory() {
        return accountTransactionHistory;
    }

    public void setAccountTransactionHistory(List<MyTransaction> accountTransactionHistory) {
        this.accountTransactionHistory = accountTransactionHistory;
        saveAccountHistory();
    }

    public List<Coin> getCoinsBalance() {
        return coinsBalance;
    }

    public void setCoinsBalance(List<Coin> coinsBalance) {
        this.coinsBalance = coinsBalance;
        saveCoinsBalance();
    }

    public Coin getCoinByName(String name){
        for (Coin coin:coinsBalance) {
            if (coin.getName().equals(name)){
                return coin;
            }
        }
        return null;
    }


    public HashMap<String, List<Order>> getMarketOrders() {
        return marketOrders;
    }

    public List<Order> getMarketOrderForPairAndSide(String base, String quote, OrderSide side)
    {
        List<Order> filteredList = new ArrayList<>();
        for (Order order:marketOrders.get(base+"_"+quote)) {
            if (order.getBaseCurrency().equals(base) && order.getQuoteCurrency().equals(quote) && order.getSide() == side){
               filteredList.add(order);
           }
        }
        return filteredList;
    }
    public void addToOrders(String pair, List<Order> offers){
        marketOrders.put(pair,offers);
        saveMarketOrders();
    }

    public List<Order> getMarketOrdersForPair(String pair){
       return marketOrders.get(pair);
    }

    public void removeOrderById(String orderId){
        for (Iterator<Order> iterator = accountOrders.iterator(); iterator.hasNext();) {
            Order order = iterator.next();
            if (order.getOrderId().equals(orderId)) {
                iterator.remove();
                return;
            }
        }
        saveAccountOrders();
    }

    public List<Order> getUserOrdersByCurrencyPairAndSide(String base, String quote, OrderSide side){
        List<Order> filteredOrders = new ArrayList<>();
        if (accountOrders == null) return new ArrayList<>();
        for (Order order:accountOrders) {
            if (order.getBaseCurrency().equals(base) && order.getQuoteCurrency().equals(quote) && order.getSide() == side)
            {
                filteredOrders.add(order);
            }
        }
        return filteredOrders;
    }

    public User getUser() {
        return user;
    }


    public List<Order> getAccountOrders() {
        return accountOrders;
    }

    public void setAccountOrders(List<Order> accountOrders) {
        this.accountOrders = accountOrders;
        saveAccountOrders();
    }

    public void setUser(User user) {
        this.user = user;
        loadContentFromCache();
    }

    public CurrencyPair getActualCurrencyPair() {
        return actualCurrencyPair;
    }

    public void setActualCurrencyPair(CurrencyPair actualCurrencyPair) {
        this.actualCurrencyPair = actualCurrencyPair;
    }

    public OrderSide getCurrentOrderSide() {
        return currentOrderSide;
    }

    public void setCurrentOrderSide(OrderSide currentOrderSide) {
        this.currentOrderSide = currentOrderSide;
    }

    public void addMarketPrice(String pair, double price){
        marketPrices.put(pair,price);
    }

    public double getMarketPricePerPair(String pair){
        return marketPrices.get(pair);
    }
}

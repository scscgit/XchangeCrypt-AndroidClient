package cloud.coders.sk.xchangecrypt.core;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cloud.coders.sk.xchangecrypt.datamodel.Coin;
import cloud.coders.sk.xchangecrypt.datamodel.ContentCacheType;
import cloud.coders.sk.xchangecrypt.datamodel.MyTransaction;
import cloud.coders.sk.xchangecrypt.datamodel.Order;
import cloud.coders.sk.xchangecrypt.datamodel.OrderSide;
import cloud.coders.sk.xchangecrypt.datamodel.User;
import cloud.coders.sk.xchangecrypt.util.InternalStorage;

/**
 * Created by V3502484 on 16. 9. 2016.
 */
public class ContentProvider {
    private static ContentProvider instance;
    private static Context context;

    // Public cache tags
    private final String MARKET_DEPTH_TAG = "MarketDepth";

    // Account-specific cache tags
    private final String ACCOUNT_ORDERS_TAG = "AccountOrders_";
    private final String TRANSACTION_HISTORY_TAG = "TransactionHistory_";
    private final String COINS_BALANCE_TAG = "CoinsBalance_";

    // Current content description
    private String currentCurrencyPair;
    private OrderSide currentOrderSide;

    // Current content to be displayed
    private HashMap<String, Double> marketPriceBuy;
    private HashMap<String, Double> marketPriceSell;
    private HashMap<String, List<Order>> marketDepth;
    private List<Order> accountOrders;
    private List<MyTransaction> accountOrderHistory;
    private HashMap<String, List<MyTransaction>> accountTransactionHistoryMap;
    private List<String> instruments;
    private List<Coin> coinsBalance;

    // The MSAL representation of the authenticated user and his scopes
    private String[] scopes;
    private User user;

    // Metadata describing how fresh the content provider's content is
    private HashMap<ContentCacheType, Date> lastUpdates;

    private ContentProvider() {
        accountOrderHistory = new ArrayList<>();
        coinsBalance = new ArrayList<>();
        marketDepth = new HashMap<>();
        marketPriceBuy = new HashMap<>();
        marketPriceSell = new HashMap<>();
        lastUpdates = new HashMap<>();
        accountTransactionHistoryMap = new HashMap<>();
        instruments = new ArrayList<>();
    }

    public static ContentProvider getInstance(Context context) {
        ContentProvider.context = context;
        if (instance == null) {
            instance = new ContentProvider();
        }
        return instance;
    }

    public void destroy() {
        instance = null;
    }

    @SuppressWarnings("unchecked")
    public void loadContentFromCache() {
        List<MyTransaction> cachedAccountTransactionHistory = (List<MyTransaction>)
                InternalStorage.readObject(context, TRANSACTION_HISTORY_TAG + user.getAccountId());
        if (cachedAccountTransactionHistory != null) {
            accountOrderHistory = cachedAccountTransactionHistory;
        }

        List<Coin> cachedCoinsBalance = (List<Coin>)
                InternalStorage.readObject(context, COINS_BALANCE_TAG + user.getAccountId());
        if (cachedCoinsBalance != null) {
            coinsBalance = cachedCoinsBalance;
        }

        HashMap<String, List<Order>> cachedMarketOrders = (HashMap<String, List<Order>>)
                InternalStorage.readObject(context, MARKET_DEPTH_TAG);
        if (cachedMarketOrders != null) {
            marketDepth = cachedMarketOrders;
        }

        List<Order> cachedAccountOrders = (List<Order>)
                InternalStorage.readObject(context, ACCOUNT_ORDERS_TAG + user.getAccountId());
        if (cachedAccountOrders != null) {
            accountOrders = cachedAccountOrders;
        }
    }

    public Date getLastUpdateTime(ContentCacheType updateType) {
        return lastUpdates.get(updateType);
    }

    public void setLastUpdateTime(ContentCacheType updateType, Date date) {
        this.lastUpdates.put(updateType, date);
    }

    public void saveAccountHistory() {
        InternalStorage.writeObject(context, TRANSACTION_HISTORY_TAG + user.getAccountId(), accountOrderHistory);
    }

    public void saveCoinsBalance() {
        InternalStorage.writeObject(context, COINS_BALANCE_TAG + user.getAccountId(), coinsBalance);
    }

    public void saveDepthOrders() {
        InternalStorage.writeObject(context, MARKET_DEPTH_TAG, marketDepth);
    }

    public void saveAccountOrders() {
        InternalStorage.writeObject(context, ACCOUNT_ORDERS_TAG + user.getAccountId(), accountOrders);
    }

    public String getCurrentCurrencyPair() {
        if (currentCurrencyPair == null) {
            throw new RuntimeException("ContentProvider not initialized. Refactoring gone wrong?");
        }
        return currentCurrencyPair;
    }

    public void setCurrentCurrencyPair(String currentCurrencyPair) {
        // The current pair is always initialized even if it's empty
        if (marketDepth.get(currentCurrencyPair) == null) {
            marketDepth.put(currentCurrencyPair, new ArrayList<>());
        }
        this.currentCurrencyPair = currentCurrencyPair;
    }

    public OrderSide getCurrentOrderSide() {
        return currentOrderSide;
    }

    public void setCurrentOrderSide(OrderSide currentOrderSide) {
        this.currentOrderSide = currentOrderSide;
    }

    public double getMarketPrice(String currencyPair, OrderSide side) {
        if (side == OrderSide.buy) {
            return marketPriceBuy.get(currencyPair);
        } else if (side == OrderSide.sell) {
            return marketPriceSell.get(currencyPair);
        }
        throw new RuntimeException("Unexpected side argument");
    }

    public void setMarketPrice(String currencyPair, double price, OrderSide side) {
        if (side == OrderSide.buy) {
            marketPriceBuy.put(currencyPair, price);
        } else if (side == OrderSide.sell) {
            marketPriceSell.put(currencyPair, price);
        }
        throw new RuntimeException("Unexpected side argument");
    }

    public List<Order> getMarketDepthOrders(String currencyPair) {
        return marketDepth.get(currencyPair);
    }

    public void setMarketDepthOrders(String currencyPair, List<Order> orders) {
        marketDepth.put(currencyPair, orders);
        saveDepthOrders();
        setLastUpdateTime(ContentCacheType.DEPTH_ORDERS, new Date());
    }

    public List<Order> getMarketDepthOrdersForPairAndSide(String base, String quote, OrderSide side) {
        List<Order> filteredList = new ArrayList<>();
        for (Order order : marketDepth.get(base + "_" + quote)) {
            if (order.getBaseCurrency().equals(base) && order.getQuoteCurrency().equals(quote) && order.getSide() == side) {
                filteredList.add(order);
            }
        }
        return filteredList;
    }

    public List<Order> getAccountOrders() {
        return accountOrders;
    }

    public void setAccountOrders(List<Order> accountOrders) {
        this.accountOrders = accountOrders;
        saveAccountOrders();
        setLastUpdateTime(ContentCacheType.ACCOUNT_ORDERS, new Date());
    }

    public void removeAccountOrderById(String orderId) {
        for (Iterator<Order> iterator = accountOrders.iterator(); iterator.hasNext(); ) {
            Order order = iterator.next();
            if (order.getOrderId().equals(orderId)) {
                iterator.remove();
                return;
            }
        }
        saveAccountOrders();
    }

    public List<Order> getAccountOrdersByCurrencyPairAndSide(String base, String quote, OrderSide side) {
        List<Order> filteredOrders = new ArrayList<>();
        if (accountOrders == null) {
            return new ArrayList<>();
        }
        for (Order order : accountOrders) {
            if (order.getBaseCurrency().equals(base) && order.getQuoteCurrency().equals(quote) && order.getSide() == side) {
                filteredOrders.add(order);
            }
        }
        return filteredOrders;
    }

    public List<MyTransaction> getAccountOrderHistory() {
        return accountOrderHistory;
    }

    public void setAccountOrderHistory(List<MyTransaction> accountOrderHistory) {
        this.accountOrderHistory = accountOrderHistory;
        saveAccountHistory();
        setLastUpdateTime(ContentCacheType.ACCOUNT_ORDER_HISTORY, new Date());
    }

    public List<MyTransaction> getAccountTransactionHistory(String currencyPair) {
        return accountTransactionHistoryMap.get(currencyPair);
    }

    public void setAccountTransactionHistory(String currencyPair, List<MyTransaction> accountTransactionHistory) {
        accountTransactionHistoryMap.put(currencyPair, accountTransactionHistory);
        // TODO: save to cache
        setLastUpdateTime(ContentCacheType.ACCOUNT_TRANSACTION_HISTORY, new Date());
    }

    public List<String> getInstruments() {
        return instruments;
    }

    public void setInstruments(List<String> instruments) {
        this.instruments = instruments;
        // TODO: save to cache
        setLastUpdateTime(ContentCacheType.INSTRUMENTS, new Date());
    }

    public List<Coin> getCoinsBalance() {
        return coinsBalance;
    }

    public void setCoinsBalance(List<Coin> coinsBalance) {
        this.coinsBalance = coinsBalance;
        saveCoinsBalance();
        setLastUpdateTime(ContentCacheType.BALANCE, new Date());
    }

    public Coin getCoinBalanceByName(String coinSymbol) {
        for (Coin coin : coinsBalance) {
            if (coin.getSymbolName().equals(coinSymbol)) {
                return coin;
            }
        }
        return null;
    }

    public String[] getScopes() {
        return scopes;
    }

    public void setScopes(String[] scopes) {
        this.scopes = scopes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        loadContentFromCache();
    }
}

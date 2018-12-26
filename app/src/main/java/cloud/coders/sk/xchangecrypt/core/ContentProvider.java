package cloud.coders.sk.xchangecrypt.core;

import android.content.Context;
import android.util.Log;

import com.annimon.stream.Stream;

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

    private final String TAG = ContentProvider.class.getSimpleName();

    // Public cache tags
    private final String CURRENT_CURRENCY_PAIR_TAG = "CurrentCurrencyPair";
    private final String CURRENT_ORDER_SIDE_TAG = "CurrentOrderSide";
    private final String INSTRUMENTS_TAG = "Instruments";
    private final String MARKET_DEPTH_TAG = "MarketDepth";

    // Account-specific cache tags
    private final String ACCOUNT_ORDERS_TAG = "AccountOrders_";
    private final String ACCOUNT_ORDER_HISTORY_TAG = "AccountOrderHistory_";
    private final String ACCOUNT_TRANSACTION_HISTORY_TAG = "AccountTransactionHistory_";
    private final String COINS_BALANCE_TAG = "CoinsBalance_";

    // Current content description
    private String currentCurrencyPair;
    private OrderSide currentOrderSide;

    // Current content to be displayed
    private List<String> instruments = new ArrayList<>();
    private HashMap<String, List<Order>> marketDepth = new HashMap<>();
    private List<Order> accountOrders = new ArrayList<>();
    private List<MyTransaction> accountOrderHistory = new ArrayList<>();
    private HashMap<String, List<MyTransaction>> accountTransactionHistoryMap = new HashMap<>();
    private List<Coin> coinsBalance = new ArrayList<>();

    // Generated views
    private HashMap<String, Double> marketPriceBuy = new HashMap<>();
    private HashMap<String, Double> marketPriceSell = new HashMap<>();

    // The MSAL representation of the authenticated user and his scopes
    private String[] scopes;
    private User user;

    // Metadata describing how fresh the content provider's content is
    private HashMap<ContentCacheType, Date> lastUpdates = new HashMap<>();

    private ContentProvider() {
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
    public boolean loadContentFromCache() {
        boolean fullyLoaded = true;
        String cachedCurrentCurrencyPair = (String)
                InternalStorage.readObject(context, CURRENT_CURRENCY_PAIR_TAG);
        if (cachedCurrentCurrencyPair != null) {
            currentCurrencyPair = cachedCurrentCurrencyPair;
        } else {
            fullyLoaded = false;
        }

        OrderSide cachedCurrentOrderSide = (OrderSide)
                InternalStorage.readObject(context, CURRENT_ORDER_SIDE_TAG);
        if (cachedCurrentOrderSide != null) {
            currentOrderSide = cachedCurrentOrderSide;
        } else {
            fullyLoaded = false;
        }

        List<String> cachedInstruments = (List<String>)
                InternalStorage.readObject(context, INSTRUMENTS_TAG);
        if (cachedInstruments != null) {
            instruments = cachedInstruments;
        } else {
            fullyLoaded = false;
        }

        HashMap<String, List<Order>> cachedMarketDepth = (HashMap<String, List<Order>>)
                InternalStorage.readObject(context, MARKET_DEPTH_TAG);
        if (cachedMarketDepth != null) {
            marketDepth = cachedMarketDepth;
        } else {
            fullyLoaded = false;
        }

        List<Order> cachedAccountOrders = (List<Order>)
                InternalStorage.readObject(context, ACCOUNT_ORDERS_TAG + user.getAccountId());
        if (cachedAccountOrders != null) {
            accountOrders = cachedAccountOrders;
        } else {
            fullyLoaded = false;
        }

        List<MyTransaction> cachedAccountOrderHistory = (List<MyTransaction>)
                InternalStorage.readObject(context, ACCOUNT_ORDER_HISTORY_TAG + user.getAccountId());
        if (cachedAccountOrderHistory != null) {
            accountOrderHistory = cachedAccountOrderHistory;
        } else {
            fullyLoaded = false;
        }

        HashMap<String, List<MyTransaction>> cachedAccountTransactionHistoryMap = (HashMap<String, List<MyTransaction>>)
                InternalStorage.readObject(context, ACCOUNT_TRANSACTION_HISTORY_TAG + user.getAccountId());
        if (cachedAccountTransactionHistoryMap != null) {
            accountTransactionHistoryMap = cachedAccountTransactionHistoryMap;
        } else {
            fullyLoaded = false;
        }

        List<Coin> cachedCoinsBalance = (List<Coin>)
                InternalStorage.readObject(context, COINS_BALANCE_TAG + user.getAccountId());
        if (cachedCoinsBalance != null) {
            coinsBalance = cachedCoinsBalance;
        } else {
            fullyLoaded = false;
        }

        Log.d(TAG, "Result of loading content from cache: " + fullyLoaded);
        return fullyLoaded;
    }

    public Date getLastUpdateTime(ContentCacheType updateType) {
        return lastUpdates.get(updateType);
    }

    public void setLastUpdateTime(ContentCacheType updateType, Date date) {
        this.lastUpdates.put(updateType, date);
    }

    public void saveCurrentCurrencyPair() {
        InternalStorage.writeObject(context, CURRENT_CURRENCY_PAIR_TAG, currentCurrencyPair);
    }

    public void saveCurrentOrderSide() {
        InternalStorage.writeObject(context, CURRENT_ORDER_SIDE_TAG, currentOrderSide);
    }

    public void saveInstruments() {
        InternalStorage.writeObject(context, INSTRUMENTS_TAG, instruments);
    }

    public void saveDepthOrders() {
        InternalStorage.writeObject(context, MARKET_DEPTH_TAG, marketDepth);
    }

    public void saveAccountOrders() {
        InternalStorage.writeObject(context, ACCOUNT_ORDERS_TAG + user.getAccountId(), accountOrders);
    }

    public void saveAccountOrderHistory() {
        InternalStorage.writeObject(context, ACCOUNT_ORDER_HISTORY_TAG + user.getAccountId(), accountOrderHistory);
    }

    public void saveAccountTransactionHistory() {
        InternalStorage.writeObject(context, ACCOUNT_TRANSACTION_HISTORY_TAG + user.getAccountId(), accountTransactionHistoryMap);
    }

    public void saveCoinsBalance() {
        InternalStorage.writeObject(context, COINS_BALANCE_TAG + user.getAccountId(), coinsBalance);
    }

    public String getCurrentCurrencyPair() {
        if (currentCurrencyPair == null) {
            throw new RuntimeException("ContentProvider's current currency pair not initialized. Refactoring gone wrong?");
        }
        return currentCurrencyPair;
    }

    public void setCurrentCurrencyPair(String currentCurrencyPair) {
        // TODO: remove. The current pair is temporarily always initialized even if it's empty
        if (marketDepth.get(currentCurrencyPair) == null) {
            marketDepth.put(currentCurrencyPair, new ArrayList<>());
        }
        this.currentCurrencyPair = currentCurrencyPair;
        saveCurrentCurrencyPair();
    }

    public OrderSide getCurrentOrderSide() {
        return currentOrderSide;
    }

    public void setCurrentOrderSide(OrderSide currentOrderSide) {
        this.currentOrderSide = currentOrderSide;
        saveCurrentOrderSide();
    }

    public List<String> getInstruments() {
        return instruments;
    }

    public void setInstruments(List<String> instruments) {
        this.instruments = instruments;
        saveInstruments();
        setLastUpdateTime(ContentCacheType.INSTRUMENTS, new Date());
    }

    public double getMarketPrice(String currencyPair, OrderSide side) {
        if (side == OrderSide.BUY) {
            return marketPriceBuy.get(currencyPair);
        } else if (side == OrderSide.SELL) {
            return marketPriceSell.get(currencyPair);
        }
        throw new RuntimeException("Unexpected side argument");
    }

    public void setMarketPrice(String currencyPair, double price, OrderSide side) {
        if (side == OrderSide.BUY) {
            marketPriceBuy.put(currencyPair, price);
        } else if (side == OrderSide.SELL) {
            marketPriceSell.put(currencyPair, price);
        } else {
            throw new RuntimeException("Unexpected side argument");
        }
    }

    public List<Order> getMarketDepthOrders(String currencyPair) {
        return marketDepth.get(currencyPair);
    }

    public void setMarketDepthOrders(String currencyPair, List<Order> orders) {
        marketDepth.put(currencyPair, orders);
        saveDepthOrders();
        setLastUpdateTime(ContentCacheType.MARKET_DEPTH, new Date());
        Log.d(TAG, String.format("Set %d market depth orders for pair %s.", marketDepth.size(), currencyPair));
        calculateMarketPrices(currencyPair);
    }

    private void calculateMarketPrices(String currencyPair) {
        if (marketDepth.get(currencyPair).size() == 0) {
            marketPriceBuy.put(currencyPair, 0.0);
            marketPriceSell.put(currencyPair, 0.0);
            return;
        }
        marketPriceBuy.put(currencyPair,
                Stream.of(marketDepth.get(currencyPair))
                        .filter(depthItem -> depthItem.getSide() == OrderSide.SELL)
                        .map(Order::getLimitPrice)
                        .min(Double::compareTo)
                        .get()
        );
        marketPriceSell.put(currencyPair,
                Stream.of(marketDepth.get(currencyPair))
                        .filter(depthItem -> depthItem.getSide() == OrderSide.BUY)
                        .map(Order::getLimitPrice)
                        .max(Double::compareTo)
                        .get()
        );
        Log.d(TAG, String.format("Calculated market prices for pair %s.", currencyPair));
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
        saveAccountOrderHistory();
        setLastUpdateTime(ContentCacheType.ACCOUNT_ORDER_HISTORY, new Date());
    }

    public List<MyTransaction> getAccountTransactionHistory(String currencyPair) {
        return accountTransactionHistoryMap.get(currencyPair);
    }

    public void setAccountTransactionHistory(String currencyPair, List<MyTransaction> accountTransactionHistory) {
        accountTransactionHistoryMap.put(currencyPair, accountTransactionHistory);
        saveAccountTransactionHistory();
        setLastUpdateTime(ContentCacheType.ACCOUNT_TRANSACTION_HISTORY, new Date());
    }

    public List<Coin> getCoinsBalance() {
        return coinsBalance;
    }

    public void setCoinsBalance(List<Coin> coinsBalance) {
        this.coinsBalance = coinsBalance;
        saveCoinsBalance();
        setLastUpdateTime(ContentCacheType.COINS_BALANCE, new Date());
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

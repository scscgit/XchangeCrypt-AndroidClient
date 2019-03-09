package bit.xchangecrypt.client.core;

import android.content.Context;
import android.util.Log;
import bit.xchangecrypt.client.datamodel.*;
import bit.xchangecrypt.client.datamodel.enums.OrderSide;
import bit.xchangecrypt.client.exceptions.TradingException;
import bit.xchangecrypt.client.util.InternalStorage;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.*;

/**
 * Created by V3502484 on 16. 9. 2016.
 */
public class ContentProvider {
    private static ContentProvider instance;

    private final String TAG = ContentProvider.class.getSimpleName();

    // Public cache tags
    private final String CURRENT_CURRENCY_PAIR_TAG = "CurrentCurrencyPair";
    private final String CURRENT_ORDER_SIDE_TAG = "CurrentOrderSide";
    private final String INSTRUMENTS_TAG = "Instruments";
    private final String MARKET_DEPTH_TAG = "MarketDepth";

    // Account-specific cache tag prefixes
    private final String ACCOUNT_ORDERS_TAG = "AccountOrders_";
    private final String ACCOUNT_ORDER_HISTORY_TAG = "AccountOrderHistory_";
    private final String ACCOUNT_TRANSACTION_HISTORY_TAG = "AccountTransactionHistory_";
    private final String COINS_BALANCE_TAG = "CoinsBalance_";

    // Cache freshness meta-data tags
    private final String LAST_UPDATES_TAG = "LastUpdates";
    private final String LAST_UPDATES_OF_MARKET_DEPTH_TAG = "LastUpdatesOfMarketDepth";

    // Android-specific references
    private Context context;

    // Current content description
    private String currentCurrencyPair;
    private OrderSide currentOrderSide;

    // Current content to be displayed
    private List<String> instruments;
    private HashMap<String, List<Order>> marketDepthMap = new HashMap<>();
    private List<Order> accountOrders;
    private List<Order> accountOrderHistory;
    private HashMap<String, List<MyTransaction>> accountTransactionHistoryMap = new HashMap<>();
    private List<Coin> coinsBalance;

    // Locks
    private final Object marketDepthLock = new Object();

    // Generated views
    private HashMap<String, Double> marketPriceBuy = new HashMap<>();
    private HashMap<String, Double> marketPriceSell = new HashMap<>();
    private HashMap<String, List<Order>> marketDepthBuyMap = new HashMap<>();
    private HashMap<String, List<Order>> marketDepthSellMap = new HashMap<>();

    // The MSAL representation of the authenticated user
    private User user;

    // Metadata describing how fresh the content provider's content is
    private HashMap<ContentCacheType, Date> lastUpdates = new HashMap<>();
    private HashMap<String, Date> lastUpdatesOfMarketDepth = new HashMap<>();

    private ContentProvider() {
    }

    public static ContentProvider getInstance(Context context) {
        if (instance == null) {
            instance = new ContentProvider();
        }
        return instance.withContext(context);
    }

    private ContentProvider withContext(Context context) {
        this.context = context;
        return this;
    }

    public void destroy() {
        instance = null;
    }

    @SuppressWarnings("unchecked")
    public boolean loadContentFromCache() {
        boolean fullyLoaded = true;
        try {
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
                marketDepthMap = cachedMarketDepth;
                for (String currencyPair : marketDepthMap.keySet()) {
                    generateSortedMarketDepthOrdersAndMarketPrices(currencyPair);
                }
            } else {
                fullyLoaded = false;
            }

            // Loading cache freshness meta-data
            HashMap<ContentCacheType, Date> cachedLastUpdates = (HashMap<ContentCacheType, Date>)
                InternalStorage.readObject(context, LAST_UPDATES_TAG);
            if (cachedLastUpdates != null) {
                lastUpdates = cachedLastUpdates;
            } else {
                fullyLoaded = false;
            }

            HashMap<String, Date> cachedLastUpdatesOfMarketDepth = (HashMap<String, Date>)
                InternalStorage.readObject(context, LAST_UPDATES_OF_MARKET_DEPTH_TAG);
            if (cachedLastUpdatesOfMarketDepth != null) {
                lastUpdatesOfMarketDepth = cachedLastUpdatesOfMarketDepth;
            } else {
                fullyLoaded = false;
            }
        } catch (Exception e) {
            fullyLoaded = false;
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        // Only load the user cache if the user is actually logged in
        if (this.user != null && !loadPrivateUserContentFromCache()) {
            fullyLoaded = false;
        }

        Log.d(TAG, "Result of loading content from cache: " + fullyLoaded);
        return fullyLoaded;
    }

    @SuppressWarnings("unchecked")
    private boolean loadPrivateUserContentFromCache() {
        boolean fullyLoaded = true;
        try {
            List<Order> cachedAccountOrders = (List<Order>)
                InternalStorage.readObject(context, ACCOUNT_ORDERS_TAG + user.getUserId());
            if (cachedAccountOrders != null) {
                accountOrders = cachedAccountOrders;
            } else {
                fullyLoaded = false;
            }

            List<Order> cachedAccountOrderHistory = (List<Order>)
                InternalStorage.readObject(context, ACCOUNT_ORDER_HISTORY_TAG + user.getUserId());
            if (cachedAccountOrderHistory != null) {
                accountOrderHistory = cachedAccountOrderHistory;
            } else {
                fullyLoaded = false;
            }

            HashMap<String, List<MyTransaction>> cachedAccountTransactionHistoryMap = (HashMap<String, List<MyTransaction>>)
                InternalStorage.readObject(context, ACCOUNT_TRANSACTION_HISTORY_TAG + user.getUserId());
            if (cachedAccountTransactionHistoryMap != null) {
                accountTransactionHistoryMap = cachedAccountTransactionHistoryMap;
            } else {
                fullyLoaded = false;
            }

            List<Coin> cachedCoinsBalance = (List<Coin>)
                InternalStorage.readObject(context, COINS_BALANCE_TAG + user.getUserId());
            if (cachedCoinsBalance != null) {
                coinsBalance = cachedCoinsBalance;
            } else {
                fullyLoaded = false;
            }
        } catch (Exception e) {
            fullyLoaded = false;
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        return fullyLoaded;
    }

    public Date getLastUpdateTime(ContentCacheType updateType) {
        return lastUpdates.get(updateType);
    }

    public void setLastUpdateTime(ContentCacheType updateType, Date date) {
        this.lastUpdates.put(updateType, date);
        saveLastUpdates();
    }

    public Date getLastUpdateTimeOfMarketDepth(String currencyPair) {
        return lastUpdatesOfMarketDepth.get(currencyPair);
    }

    public void setLastUpdateTimeOfMarketDepth(String currencyPair, Date date) {
        this.lastUpdatesOfMarketDepth.put(currencyPair, date);
        saveLastUpdates();
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
        InternalStorage.writeObject(context, MARKET_DEPTH_TAG, marketDepthMap);
    }

    public void saveAccountOrders() {
        InternalStorage.writeObject(context, ACCOUNT_ORDERS_TAG + user.getUserId(), accountOrders);
    }

    public void saveAccountOrderHistory() {
        InternalStorage.writeObject(context, ACCOUNT_ORDER_HISTORY_TAG + user.getUserId(), accountOrderHistory);
    }

    public void saveAccountTransactionHistory() {
        InternalStorage.writeObject(context, ACCOUNT_TRANSACTION_HISTORY_TAG + user.getUserId(), accountTransactionHistoryMap);
    }

    public void saveCoinsBalance() {
        InternalStorage.writeObject(context, COINS_BALANCE_TAG + user.getUserId(), coinsBalance);
    }

    public void saveLastUpdates() {
        InternalStorage.writeObject(context, LAST_UPDATES_TAG, coinsBalance);
        InternalStorage.writeObject(context, LAST_UPDATES_OF_MARKET_DEPTH_TAG, coinsBalance);
    }

    public String getCurrentCurrencyPair() {
        if (currentCurrencyPair == null) {
            throw new RuntimeException("ContentProvider's current currency pair not initialized. Refactoring gone wrong?");
        }
        return currentCurrencyPair;
    }

    public void setCurrentCurrencyPair(String currentCurrencyPair) {
        // TODO: remove. The current pair is temporarily always initialized even if it's empty
        if (marketDepthMap.get(currentCurrencyPair) == null) {
            marketDepthMap.put(currentCurrencyPair, new ArrayList<>());
        }
        this.currentCurrencyPair = currentCurrencyPair;
        saveCurrentCurrencyPair();
        Log.d(TAG, String.format("Configured current currency pair %s", currentCurrencyPair));
    }

    public OrderSide getCurrentOrderSide() {
        return currentOrderSide;
    }

    public void setCurrentOrderSide(OrderSide currentOrderSide) {
        this.currentOrderSide = currentOrderSide;
        saveCurrentOrderSide();
        Log.d(TAG, String.format("Configured %s order side", currentOrderSide.toString()));
    }

    public List<String> getInstruments() {
        return instruments;
    }

    public void setInstruments(List<String> instruments) {
        this.instruments = instruments;
        saveInstruments();
        setLastUpdateTime(ContentCacheType.INSTRUMENTS, new Date());
        Log.d(TAG, String.format("Configured %d instruments", instruments.size()));
    }

    public double getMarketPrice(String currencyPair, OrderSide side) {
        if (side == OrderSide.BUY) {
            return marketPriceBuy.get(currencyPair);
        } else if (side == OrderSide.SELL) {
            return marketPriceSell.get(currencyPair);
        }
        throw new RuntimeException("Unexpected side argument");
    }

    public List<Order> getMarketDepthOrders(String currencyPair, OrderSide side) {
        synchronized (this.marketDepthLock) {
            switch (side) {
                case BUY:
                    return this.marketDepthBuyMap.get(currencyPair);
                case SELL:
                    return this.marketDepthSellMap.get(currencyPair);
                default:
                    throw new RuntimeException("Unexpected side argument");
            }
        }
    }

    public void setMarketDepthOrders(String currencyPair, List<Order> orders) {
        if (orders == null) {
            throw new IllegalArgumentException("orders");
        }
        synchronized (this.marketDepthLock) {
            // Sorting happens later on get
            this.marketDepthMap.put(currencyPair, orders);
            saveDepthOrders();
            setLastUpdateTimeOfMarketDepth(currencyPair, new Date());
            Log.d(TAG, String.format("Configured %d market depth orders for pair %s.", this.marketDepthMap.size(), currencyPair));
            generateSortedMarketDepthOrdersAndMarketPrices(currencyPair);
        }
    }

    private void generateSortedMarketDepthOrdersAndMarketPrices(String currencyPair) {
        List<Order> buyMap = Stream.of(this.marketDepthMap.get(currencyPair))
            .filter(order -> order.getSide() == OrderSide.BUY)
            // Sort buyers descending
            .sortBy(Order::getLimitPrice)
            .collect(Collectors.toList());
        List<Order> sellMap = Stream.of(this.marketDepthMap.get(currencyPair))
            .filter(order -> order.getSide() == OrderSide.SELL)
            // Sort sellers descending
            .sortBy(order -> -order.getLimitPrice())
            .collect(Collectors.toList());
        this.marketDepthBuyMap.put(currencyPair, buyMap);
        this.marketDepthSellMap.put(currencyPair, sellMap);
        // Assign market prices based on the sorted maps
        this.marketPriceBuy.put(currencyPair, sellMap.size() == 0 ? 0 : sellMap.get(0).getLimitPrice());
        this.marketPriceSell.put(currencyPair, buyMap.size() == 0 ? 0 : buyMap.get(0).getLimitPrice());
        Log.d(TAG, String.format("Generated sorted market depth orders and market prices for pair %s.", currencyPair));
    }

    public List<Order> getAccountOrders(String currencyPair, OrderSide side) {
        String[] currencies = currencyPair.split("_");
        return Stream.of(this.accountOrders)
            .filter(order -> order.getBaseCurrency().equals(currencies[0])
                && order.getQuoteCurrency().equals(currencies[1])
                && order.getSide() == side)
            .collect(Collectors.toList());
    }

    public void setAccountOrders(List<Order> accountOrders) {
        accountOrders = Stream.of(accountOrders)
            .sortBy(accountOrder -> null != accountOrder.getStopPrice()
                ? accountOrder.getStopPrice()
                : accountOrder.getLimitPrice())
            .collect(Collectors.toList());
        this.accountOrders = accountOrders;
        saveAccountOrders();
        setLastUpdateTime(ContentCacheType.ACCOUNT_ORDERS, new Date());
    }

    public void removeAccountOrderById(String orderId) {
        for (Iterator<Order> iterator = accountOrders.iterator(); iterator.hasNext(); ) {
            Order order = iterator.next();
            if (order.getOrderId().equals(orderId)) {
                iterator.remove();
                saveAccountOrders();
                return;
            }
        }
        throw new TradingException("Attempted to remove a reference to a non-existing order");
    }

    public List<Order> getAccountOrderHistory() {
        return accountOrderHistory;
    }

    public void setAccountOrderHistory(List<Order> accountOrderHistory) {
        accountOrderHistory = Stream.of(accountOrderHistory)
            .sortBy(Order::getOrderId)
            .collect(Collectors.toList());
        this.accountOrderHistory = accountOrderHistory;
        saveAccountOrderHistory();
        setLastUpdateTime(ContentCacheType.ACCOUNT_ORDER_HISTORY, new Date());
    }

    public List<MyTransaction> getAccountTransactionHistory(String currencyPair) {
        return accountTransactionHistoryMap.get(currencyPair);
    }

    public void setAccountTransactionHistory(String currencyPair, List<MyTransaction> accountTransactionHistory) {
        accountTransactionHistory = Stream.of(accountTransactionHistory)
            .sortBy(MyTransaction::getDate)
            .collect(Collectors.toList());
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
        if (coinsBalance == null) {
            return null;
        }
        for (Coin coin : coinsBalance) {
            if (coin.getSymbolName().equals(coinSymbol)) {
                return coin;
            }
        }
        return null;
    }

    public User getUser() {
        return user;
    }

    public boolean setUserAndLoadCache(User user) {
        this.user = user;
        accountOrders = null;
        accountOrderHistory = null;
        accountTransactionHistoryMap = new HashMap<>();
        coinsBalance = null;
        return loadContentFromCache();
    }

    public boolean isPublicExchangeLoaded() {
        return currentCurrencyPair != null
            && currentOrderSide != null
            && instruments != null
            && marketDepthMap.get(currentCurrencyPair) != null;
    }

    public boolean isWalletLoaded() {
        return coinsBalance != null;
    }

    public boolean isPrivateExchangeLoaded() {
        return isPublicExchangeLoaded()
            && isWalletLoaded()
            && accountOrders != null
            && accountOrderHistory != null
            && accountTransactionHistoryMap.get(currentCurrencyPair) != null;
    }
}
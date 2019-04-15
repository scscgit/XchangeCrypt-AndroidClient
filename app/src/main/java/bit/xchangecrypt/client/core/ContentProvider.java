package bit.xchangecrypt.client.core;

import android.util.Log;
import bit.xchangecrypt.client.R;
import bit.xchangecrypt.client.datamodel.*;
import bit.xchangecrypt.client.datamodel.enums.OrderSide;
import bit.xchangecrypt.client.exceptions.TradingException;
import bit.xchangecrypt.client.ui.MainActivity;
import bit.xchangecrypt.client.util.InternalStorage;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import io.swagger.client.model.BarsArrays;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
    private final String HISTORY_BARS_TAG = "HistoryBars";

    // Account-specific cache tag prefixes
    private final String ACCOUNT_ORDERS_TAG = "AccountOrders_";
    private final String ACCOUNT_ORDER_HISTORY_TAG = "AccountOrderHistory_";
    private final String ACCOUNT_TRANSACTION_HISTORY_TAG = "AccountTransactionHistory_";
    private final String COINS_BALANCE_TAG = "CoinsBalance_";
    private final String NOTIFICATIONS_TAG = "Notifications_";

    // Cache freshness meta-data tags
    private final String LAST_UPDATES_TAG = "LastUpdates";
    private final String LAST_UPDATES_OF_MARKET_DEPTH_TAG = "LastUpdatesOfMarketDepth";

    // Cache settings
    private int cacheExpireAfterMs = 30_000;

    // Android-specific references
    private MainActivity context;

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
    private HashMap<String, BarsArrays> historyBarsMap = new HashMap<>();

    // Locally relevant display data
    private String graphResolution = "1D";
    private boolean notifications;

    private ContentProvider() {
    }

    public static ContentProvider getInstance(MainActivity context) {
        if (instance == null) {
            instance = new ContentProvider();
        }
        return instance.withContext(context);
    }

    private ContentProvider withContext(MainActivity context) {
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

            HashMap<String, BarsArrays> cachedHistoryBars = (HashMap<String, BarsArrays>)
                InternalStorage.readObject(context, HISTORY_BARS_TAG);
            if (cachedHistoryBars != null) {
                historyBarsMap = cachedHistoryBars;
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

            // Notifications don't break fully loaded state
            Boolean notifications = (Boolean) InternalStorage.readObject(context, NOTIFICATIONS_TAG + user.getUserId());
            if (notifications != null) {
                this.notifications = notifications;
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

    public void saveHistoryBars() {
        InternalStorage.writeObject(context, HISTORY_BARS_TAG, historyBarsMap);
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

    public void saveNotifications() {
        InternalStorage.writeObject(context, NOTIFICATIONS_TAG + user.getUserId(), notifications);
    }

    public void saveLastUpdates() {
        InternalStorage.writeObject(context, LAST_UPDATES_TAG, lastUpdates);
        InternalStorage.writeObject(context, LAST_UPDATES_OF_MARKET_DEPTH_TAG, lastUpdatesOfMarketDepth);
    }

    public String getCurrentCurrencyPair() {
        if (currentCurrencyPair == null) {
            if (getInstruments().size() == 0) {
                try {
                    Log.w(TAG, "No instruments loaded yet, ever, even the cache is empty. Waiting for 2 more seconds, assuming this is the first run");
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (getInstruments().size() == 0) {
                throw new RuntimeException("ContentProvider's current currency pair not initialized and there are no instruments loaded yet.");
            }
            setCurrentCurrencyPair(getInstruments().get(0));
        }
        return currentCurrencyPair;
    }

    public void setCurrentCurrencyPair(String currentCurrencyPair) {
//        // TODO: remove. The current pair is temporarily always initialized even if it's empty
//        if (marketDepthMap.get(currentCurrencyPair) == null) {
//            marketDepthMap.put(currentCurrencyPair, new ArrayList<>());
//        }
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

    public BarsArrays getHistoryBars(String pair) {
        return historyBarsMap.get(pair);
    }

    public void setHistoryBars(String pair, BarsArrays historyBars) {
        this.historyBarsMap.put(pair, historyBars);
        saveHistoryBars();
        // NOTE: history bars don't need a last update time, because this is implicit in the getT() value
        Log.d(TAG, String.format("Set %d history bars for pair %s", historyBars.getT().size(), pair));
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
        // Notify of any new transactions
        if (isNotifications()) {
            if (accountTransactionHistoryMap.containsKey(currencyPair)) {
                List<MyTransaction> knownTransactions = accountTransactionHistoryMap.get(currencyPair);
                // Remove known transactions
                Stream.of(accountTransactionHistory)
                    .filter(newTransaction ->
                        Stream.of(knownTransactions)
                            .anyMatch(knownTransaction -> knownTransaction.getDate().equals(newTransaction.getDate()))
                    ).sortBy(MyTransaction::getDate)
                    .forEach(this::createNotification);
            }
        }

        accountTransactionHistory = Stream.of(accountTransactionHistory)
            .sortBy(MyTransaction::getDate)
            .collect(Collectors.toList());
        accountTransactionHistoryMap.put(currencyPair, accountTransactionHistory);
        saveAccountTransactionHistory();
        setLastUpdateTime(ContentCacheType.ACCOUNT_TRANSACTION_HISTORY, new Date());
    }

    private void createNotification(MyTransaction transaction) {
        Log.i(TAG, String.format("Notifying of a %s/%s %s transaction", transaction.getBaseCurrency(), transaction.getQuoteCurrency(), transaction.getSide() == OrderSide.BUY ? "buy" : "sell"));
        context.createNotification(
            transaction.getQuoteCurrency(),
            context.getString(
                transaction.getSide() == OrderSide.BUY ? R.string.wallet_tx_bought : R.string.wallet_tx_sold,
                transaction.getBaseCurrency() + "/" + transaction.getQuoteCurrency()
            ),
            context.getString(
                transaction.getSide() == OrderSide.BUY ? R.string.notification_tx_bought : R.string.notification_tx_sold,
                MainActivity.formatNumber(transaction.getAmount()),
                transaction.getBaseCurrency(),
                MainActivity.formatNumber(transaction.getPrice()),
                transaction.getQuoteCurrency(),
                MainActivity.formatNumber(transaction.getAmount() * transaction.getPrice()),
                transaction.getQuoteCurrency()
            )
        );
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

    public void setGraphResolution(String resolution) {
        this.graphResolution = resolution;
    }

    public String getGraphResolution() {
        return graphResolution;
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
            && instruments != null
            && marketDepthMap.get(currentCurrencyPair) != null
            && historyBarsMap.get(currentCurrencyPair) != null;
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

    @Deprecated
    public boolean isExchangeCacheNotExpired() {
        // NOTE: this implementation is not sufficient, do not use!
        Date currentDate = new Date();
        Date lastInstrumentUpdate = getLastUpdateTime(ContentCacheType.INSTRUMENTS);
        Date lastMarketDepthUpdate = getLastUpdateTimeOfMarketDepth(getCurrentCurrencyPair());
        Date lastAccountOrdersUpdate = getLastUpdateTime(ContentCacheType.ACCOUNT_ORDERS);

        boolean instrumentCached = lastInstrumentUpdate != null && currentDate.getTime() - lastInstrumentUpdate.getTime() < cacheExpireAfterMs;
        if (!instrumentCached) {
            Log.d(TAG, "Instrument cache has expired");
        }
        boolean marketCached = lastMarketDepthUpdate != null && currentDate.getTime() - lastMarketDepthUpdate.getTime() < cacheExpireAfterMs;
        if (!marketCached) {
            Log.d(TAG, "Market cache has expired");
        }
        boolean accountOrdersCached = lastAccountOrdersUpdate != null && currentDate.getTime() - lastAccountOrdersUpdate.getTime() < cacheExpireAfterMs;
        if (!accountOrdersCached) {
            Log.d(TAG, "Account orders cache has expired");
        }
        return instrumentCached && marketCached && accountOrdersCached;
    }

    @Deprecated
    public boolean isWalletCacheNotExpired() {
        // NOTE: this implementation is not sufficient, do not use!
        Date currentDate = new Date();
        Date lastHistoryUpdate = getLastUpdateTime(ContentCacheType.ACCOUNT_TRANSACTION_HISTORY);
        Date lastBalanceUpdate = getLastUpdateTime(ContentCacheType.COINS_BALANCE);

        boolean historyCached = lastHistoryUpdate != null && currentDate.getTime() - lastHistoryUpdate.getTime() < cacheExpireAfterMs;
        if (!historyCached) {
            Log.d(TAG, "Account history cache has expired");
        }
        boolean balanceCached = lastBalanceUpdate != null && currentDate.getTime() - lastBalanceUpdate.getTime() < cacheExpireAfterMs;
        if (!balanceCached) {
            Log.d(TAG, "Balance cache has expired");
        }
        return historyCached && balanceCached;
    }

    public void setNotifications(boolean isChecked) {
        notifications = isChecked;
        saveNotifications();
    }

    public boolean isNotifications() {
        return notifications;
    }
}

package cloud.coders.sk.xchangecrypt.core;

import android.content.Context;
import android.util.Log;

import com.annimon.stream.Collectors;
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
import cloud.coders.sk.xchangecrypt.datamodel.enums.OrderSide;
import cloud.coders.sk.xchangecrypt.datamodel.User;
import cloud.coders.sk.xchangecrypt.exceptions.TradingException;
import cloud.coders.sk.xchangecrypt.util.InternalStorage;

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

    // Generated views
    private HashMap<String, Double> marketPriceBuy = new HashMap<>();
    private HashMap<String, Double> marketPriceSell = new HashMap<>();
    private HashMap<String, List<Order>> marketDepthBuyMap = new HashMap<>();
    private HashMap<String, List<Order>> marketDepthSellMap = new HashMap<>();

    // The MSAL representation of the authenticated user and his scopes
    private String[] scopes;
    private User user;

    // Metadata describing how fresh the content provider's content is
    private HashMap<ContentCacheType, Date> lastUpdates = new HashMap<>();

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

            List<Order> cachedAccountOrders = (List<Order>)
                    InternalStorage.readObject(context, ACCOUNT_ORDERS_TAG + user.getAccountId());
            if (cachedAccountOrders != null) {
                accountOrders = cachedAccountOrders;
            } else {
                fullyLoaded = false;
            }

            List<Order> cachedAccountOrderHistory = (List<Order>)
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
        } catch (Exception e) {
            fullyLoaded = false;
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
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
        InternalStorage.writeObject(context, MARKET_DEPTH_TAG, marketDepthMap);
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
        switch (side) {
            case BUY:
                return this.marketDepthBuyMap.get(currencyPair);
            case SELL:
                return this.marketDepthSellMap.get(currencyPair);
            default:
                throw new RuntimeException("Unexpected side argument");
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

    public void setMarketDepthOrders(String currencyPair, List<Order> orders) {
        // Sorting happens later on get
        this.marketDepthMap.put(currencyPair, orders);
        saveDepthOrders();
        setLastUpdateTime(ContentCacheType.MARKET_DEPTH, new Date());
        Log.d(TAG, String.format("Configured %d market depth orders for pair %s.", this.marketDepthMap.size(), currencyPair));
        generateSortedMarketDepthOrdersAndMarketPrices(currencyPair);
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

    public List<Order> getAccountOrders(String currencyPair, OrderSide side) {
        String[] currencies = currencyPair.split("_");
        return Stream.of(this.accountOrders)
                .filter(order -> order.getBaseCurrency().equals(currencies[0])
                        && order.getQuoteCurrency().equals(currencies[1])
                        && order.getSide() == side)
                .collect(Collectors.toList());
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

    public boolean isPublicExchangeLoaded() {
        return currentCurrencyPair != null
                && currentOrderSide != null
                && instruments != null
                && marketDepthMap.get(currentCurrencyPair) != null;
    }

    public boolean isPrivateExchangeLoaded() {
        return isPublicExchangeLoaded()
                && isWalletLoaded()
                && accountOrders != null
                && accountOrderHistory != null
                && accountTransactionHistoryMap.get(currentCurrencyPair) != null;
    }

    public boolean isWalletLoaded() {
        return coinsBalance != null;
    }
}

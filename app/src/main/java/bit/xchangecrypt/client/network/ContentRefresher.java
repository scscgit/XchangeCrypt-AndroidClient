package bit.xchangecrypt.client.network;

import android.util.Log;
import android.widget.Toast;
import bit.xchangecrypt.client.core.ContentProvider;
import bit.xchangecrypt.client.datamodel.Coin;
import bit.xchangecrypt.client.datamodel.MyTransaction;
import bit.xchangecrypt.client.datamodel.Order;
import bit.xchangecrypt.client.datamodel.User;
import bit.xchangecrypt.client.datamodel.enums.OrderSide;
import bit.xchangecrypt.client.datamodel.enums.OrderType;
import bit.xchangecrypt.client.exceptions.TradingException;
import bit.xchangecrypt.client.ui.MainActivity;
import com.annimon.stream.Stream;
import io.swagger.client.ApiInvoker;
import io.swagger.client.auth.ApiKeyAuth;
import io.swagger.client.model.Depth;
import io.swagger.client.model.Execution;
import io.swagger.client.model.Instrument;
import io.swagger.client.model.WalletDetails;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static bit.xchangecrypt.client.listeners.FragmentSwitcherInterface.*;

public class ContentRefresher {
    public static final String TAG = ContentRefresher.class.getSimpleName();
    private static final int NUMBER_OF_THREADS = 5;
    private static final int DELAY_INTERVAL_SECONDS = 5;

    private static ContentRefresher instance;

    private MainActivity context;
    private ScheduledThreadPoolExecutor executor;
    private TradingApiHelper tradingApiHelper;
    private boolean loaded;
    private Integer fragmentIdSwitchTarget;
    private int accountOrdersHistoryCount = 30;
    private int accountExecutionsCount = 30;

    private ContentRefresher() {
        this.executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(NUMBER_OF_THREADS);
    }

    public static ContentRefresher getInstance(MainActivity context) {
        if (instance == null) {
            instance = new ContentRefresher().withContext(context);
            instance.runPeriodically(instance::reloadContent);
            instance.tradingApiHelper = new TradingApiHelper(context);
        }
        return instance;
    }

    private ContentRefresher withContext(MainActivity context) {
        this.context = context;
        return this;
    }

    private ContentProvider getContentProvider() {
        return ContentProvider.getInstance(this.context);
    }

    public ApiKeyAuth getApiAuthentication() {
        return (ApiKeyAuth) ApiInvoker.getInstance().getAuthentication("Bearer");
    }

    public void setUser(User user) {
        loaded = false;
        loaded = getContentProvider().setUserAndLoadCache(user);
        if (fragmentIdSwitchTarget == null) {
            fragmentIdSwitchTarget = FRAGMENT_EXCHANGE;
        }
        switchFragment(fragmentIdSwitchTarget);
    }

    public synchronized void switchFragment(int fragmentId) {
        fragmentIdSwitchTarget = fragmentId;
        context.showProgressDialog("Načítavam dáta zmenárne");
        tryFragmentSwitch();
    }

    private ScheduledFuture<?> runPeriodically(Runnable runnable) {
        // Schedules the task to run after a constant delay which starts after its previous run finishes
        return this.executor.scheduleWithFixedDelay(runnable, 0, DELAY_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    private void reloadContent() {
        try {
            // Wait for initialization
            if (fragmentIdSwitchTarget == null || getContentProvider().getUser() == null) {
                Log.d(TAG, "Refresher is not ready yet, waiting...");
                return;
            }
            // Choose loaders
            Runnable[] loaders;
            int reloadFragment;
            synchronized (this) {
                reloadFragment = fragmentIdSwitchTarget;
                switch (fragmentIdSwitchTarget) {
                    case FRAGMENT_EXCHANGE:
                        loaders = new Runnable[]{
                            this::loadInstruments,
                            this::loadMarketDepth,
                            this::loadAccountOrders,
                            this::loadAccountOrdersHistory,
                            this::loadAccountExecutions,
                            //TODO: optional
                            this::loadBalance
                        };
                        break;
                    case FRAGMENT_WALLET:
                        loaders = new Runnable[]{
                            this::loadBalance
                        };
                        break;
                    default:
                        throw new IllegalArgumentException("fragmentIdSwitchTarget");
                }
            }

            // Run loaders in threads
            List<Thread> threads = Stream.of(loaders).map(Thread::new).toList();
            // Start all loaders
            Throwable[] uncaughtException = new Throwable[1];
            for (Thread thread : threads) {
                // Preventing application crash on thread exception, we handle it after thread joins
                thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    public void uncaughtException(Thread th, Throwable ex) {
                        uncaughtException[0] = ex;
                    }
                });
                thread.start();
            }
            // Join all loaders
            try {
                for (int i = 0; i < threads.size(); i++) {
                    Thread thread = threads.get(i);
                    thread.join();
                    // Display progress
                    context.setProgressDialogPostfix(" (" + (i + 2) + "/" + threads.size() + ")");
                }
                if (uncaughtException[0] != null) {
                    throw uncaughtException[0];
                }
            } catch (Throwable e) {
                e.printStackTrace();
                Log.e(TAG, "Refresher thread has failed, this run is skipped");
                // TODO: replace by a more conservative approach
                tradingApiHelper.tryNextDomain();
                return;
            }

            synchronized (this) {
                if (reloadFragment != this.fragmentIdSwitchTarget) {
                    Log.i(TAG, "Ignoring finished loader threads, as the switch target fragment has changed meanwhile");
                    return;
                }
                // Check the success and switch to the target fragment
                boolean switched = tryFragmentSwitch();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Unexpected fatal error within content refresher, exiting.");
            System.exit(1);
        }
    }

    private boolean tryFragmentSwitch() {
        if (
            // Exchange
            fragmentIdSwitchTarget == FRAGMENT_EXCHANGE
                && getContentProvider().isPrivateExchangeLoaded()
                //&& getContentProvider().isExchangeCacheNotExpired()

                // Wallet
                || fragmentIdSwitchTarget == FRAGMENT_WALLET
                && getContentProvider().isWalletLoaded()
                //&& getContentProvider().isWalletCacheNotExpired()

                // Settings
                || fragmentIdSwitchTarget == FRAGMENT_SETTINGS
        ) {
            this.context.hideProgressDialog();
            this.context.switchToFragmentAndClear(fragmentIdSwitchTarget, null);
            return true;
        } else {
            Log.i(TAG, "Waiting before fragment switch, because the data is not loade (or cached data is expired)");
            return false;
        }
    }

    private void loadBalance() {
        Log.d(TAG, "loadBalance called");
        List<WalletDetails> walletDetailsAccounts;
        try {
            walletDetailsAccounts = tradingApiHelper.accountBalance();
        } catch (TradingException e) {
            context.runOnUiThread(() -> {
                Toast.makeText(context, "Chyba pri čítani zostatkov.", Toast.LENGTH_SHORT).show();
            });
            throw new TradingException("Loading Balance received error: " + e.getMessage());
        }
        List<Coin> coins = new ArrayList<>();
        for (WalletDetails walletDetails : walletDetailsAccounts) {
            coins.add(new Coin(
                walletDetails.getCoinSymbol(),
                walletDetails.getWalletPublicKey(),
                walletDetails.getBalance()
            ));
        }
        getContentProvider().setCoinsBalance(coins);
    }

    private void loadInstruments() {
        Log.d(TAG, "loadInstruments called");
        List<Instrument> instruments;
        try {
            instruments = tradingApiHelper.instruments(getContentProvider().getUser());
        } catch (TradingException e) {
            context.runOnUiThread(() -> {
                Toast.makeText(context, "Chyba pri ziskavaní menových dvojíc.", Toast.LENGTH_SHORT).show();
            });
            throw new TradingException("Loading Instruments received error: " + e.getMessage());
        }
        List<String> pairs = new ArrayList<>();
        if (instruments != null) {
            for (Instrument instrument : instruments) {
                pairs.add(instrument.getName());
            }
        }
        getContentProvider().setInstruments(pairs);
    }

    private void loadMarketDepth() {
        Log.d(TAG, "loadMarketDepth called");
        final String pair = getContentProvider().getCurrentCurrencyPair();
        Depth depthData;
        try {
            depthData = tradingApiHelper.marketDepthForPair(pair);
        } catch (TradingException e) {
            context.runOnUiThread(() -> {
                Toast.makeText(context, "Chyba pri čítaní dostupných ponúk: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
            throw new TradingException("Loading Market Depth for pair " + pair + " received error: " + e.getMessage());
        }
        List<Order> orders = new ArrayList<>();
        if (depthData == null || depthData.getAsks() == null || depthData.getBids() == null) {
            throw new TradingException("Loading Market Depth for pair " + pair + " received null data");
        }
        for (List<Double> ask : depthData.getAsks()) {
            orders.add(new Order(
                ask.get(0),
                ask.get(0),
                pair.split("_")[0],
                ask.get(1),
                pair.split("_")[1],
                OrderSide.BUY,
                OrderType.LIMIT
            ));
        }
        for (List<Double> bid : depthData.getBids()) {
            orders.add(new Order(
                bid.get(0),
                bid.get(0),
                pair.split("_")[0],
                bid.get(1),
                pair.split("_")[1],
                OrderSide.SELL,
                OrderType.LIMIT
            ));
        }
        getContentProvider().setMarketDepthOrders(pair, orders);
    }

    private void loadAccountOrders() {
        Log.d(TAG, "loadAccountOrders called");
        List<Order> offers = new ArrayList<>();
        for (io.swagger.client.model.Order order : tradingApiHelper.accountOrders(getContentProvider().getUser())) {
            double price = 0;
            if (order.getType() == io.swagger.client.model.Order.TypeEnum.market) {
                // continue;
            }
            switch (order.getType()) {
                case limit:
                    price = order.getLimitPrice().doubleValue();
                    break;
                case stop:
                    price = order.getStopPrice().doubleValue();
                    break;
                case stoplimit:
                    price = order.getStopPrice().doubleValue();
            }
            String base = order.getInstrument().split("_")[0];
            String quote;
            try {
                quote = order.getInstrument().split("_")[1];
            } catch (Exception e) {
                quote = base;
            }
            offers.add(new Order(
                price,
                price,
                base,
                order.getQty().doubleValue(),
                quote,
                OrderSide.fromString(order.getSide().toString()),
                OrderType.fromString(order.getType().toString())
            ));
        }
        getContentProvider().setAccountOrders(offers);
    }

    private void loadAccountOrdersHistory() {
        Log.d(TAG, "loadAccountOrdersHistory called");
        List<io.swagger.client.model.Order> orders;
        try {
            orders = tradingApiHelper.accountOrdersHistory(getContentProvider().getUser(), accountOrdersHistoryCount);
        } catch (TradingException e) {
            context.runOnUiThread(() -> {
                Toast.makeText(context, "Chyba pri ziskavaní histórie ponúk.", Toast.LENGTH_SHORT).show();
            });
            throw new TradingException("Account Order History received error: " + e.getMessage());
        }
        List<Order> orderHistory = new ArrayList<>();
        for (io.swagger.client.model.Order orderResponse : orders) {
            orderHistory.add(new Order(
                orderResponse.getLimitPrice().doubleValue(),
                orderResponse.getStopPrice().doubleValue(),
                orderResponse.getInstrument().split("_")[0],
                orderResponse.getQty().doubleValue(),
                orderResponse.getInstrument().split("_")[1],
                OrderSide.fromString(orderResponse.getSide().toString()),
                OrderType.fromString(orderResponse.getType().toString())
            ));
        }
        getContentProvider().setAccountOrderHistory(orderHistory);
    }

    private void loadAccountExecutions() {
        Log.d(TAG, "loadAccountExecutions called");
        final String pair = getContentProvider().getCurrentCurrencyPair();
        List<Execution> executions;
        try {
            executions = tradingApiHelper.accountExecutions(getContentProvider().getUser(), pair, accountExecutionsCount);
        } catch (TradingException e) {
            context.runOnUiThread(() -> {
                Toast.makeText(context, "Chyba pri ziskavaní histórie transakcií.", Toast.LENGTH_SHORT).show();
            });
            throw new TradingException("Loading Executions received error: " + e.getMessage());
        }
        List<MyTransaction> transactions = new ArrayList<>();
        if (executions != null) {
            for (Execution execution : executions) {
                transactions.add(new MyTransaction(
                    OrderSide.fromString(execution.getSide().toString()),
                    execution.getInstrument().split("_")[0],
                    execution.getInstrument().split("_")[1],
                    execution.getPrice(),
                    execution.getQty(),
                    new Date(execution.getTime().longValue())
                ));
            }
        }
        getContentProvider().setAccountTransactionHistory(pair, transactions);
    }

    public void sendTradingOffer(final Order offer) {
        Log.d(TAG, "sendTradingOffer called");
        context.showProgressDialog("Vytváram ponuku");
        tradingApiHelper.sendTradingOffer(offer);
    }

    public void deleteTradingOffer(final Order offer) {
        Log.d(TAG, "deleteTradingOffer called");
        context.showProgressDialog("Odstraňujem ponuku");
        tradingApiHelper.deleteTradingOffer(offer);
    }
}

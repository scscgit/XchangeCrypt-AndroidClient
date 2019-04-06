package bit.xchangecrypt.client.network;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.AsyncTask;
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
import bit.xchangecrypt.client.listeners.FragmentSwitcherInterface;
import bit.xchangecrypt.client.ui.MainActivity;
import bit.xchangecrypt.client.util.DialogHelper;
import com.annimon.stream.Stream;
import io.swagger.client.ApiInvoker;
import io.swagger.client.auth.ApiKeyAuth;
import io.swagger.client.model.*;

import java.util.ArrayList;
import java.util.Calendar;
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
    private Integer switchFragmentTarget;
    private Integer currentFragment;
    private int accountOrdersHistoryCount = 30;
    private int accountExecutionsCount = 30;
    private ScheduledFuture<?> periodicTask;
    private boolean continueOffline;
    private AlertDialog authenticationExpirationDialog;

    private ContentRefresher() {
        this.executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(NUMBER_OF_THREADS);
    }

    public static ContentRefresher getInstance(MainActivity context) {
        if (instance == null) {
            instance = new ContentRefresher().withContext(context);
            instance.tradingApiHelper = new TradingApiHelper(context);
            // Refresher is started by onResume
            //instance.startRefresher();
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
        continueOffline = false;
        loaded = false;
        loaded = getContentProvider().setUserAndLoadCache(user);
        if (switchFragmentTarget == null || switchFragmentTarget == FRAGMENT_LOGIN) {
            switchFragmentTarget = FRAGMENT_EXCHANGE;
        }
        switchFragment(switchFragmentTarget);
    }

    public synchronized void switchFragment(int fragmentId) {
        context.showProgressDialog("Načítavam dáta zmenárne");
        switchFragmentTarget = fragmentId;
        // Trigger an immediate refresh
        pauseRefresher();
        startRefresher();
        // The refresher now started to work, but we also try to immediately load the fragment using cached data
        trySwitchFragment(fragmentId);
    }

    private ScheduledFuture<?> runPeriodically(Runnable runnable) {
        // Schedules the task to run after a constant delay which starts after its previous run finishes
        return this.executor.scheduleWithFixedDelay(runnable, 0, DELAY_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    public ContentRefresher startRefresher() {
        if (this.periodicTask == null || this.periodicTask.isCancelled()) {
            this.periodicTask = runPeriodically(instance::reloadContent);
        }
        return this;
    }

    public ContentRefresher pauseRefresher() {
        this.periodicTask.cancel(true);
        return this;
    }

    private void reloadContent() {
        try {
            // Wait for initialization
            if (switchFragmentTarget == null) {
                Log.d(TAG, "Refresher is not ready yet, waiting...");
                return;
            }
            // Choose loaders
            Runnable[] loaders;
            int reloadFragment;
            synchronized (this) {
                reloadFragment = switchFragmentTarget;
                switch (switchFragmentTarget) {
                    case FRAGMENT_EXCHANGE:
                        if (getContentProvider().getUser() != null) {
                            loaders = new Runnable[]{
                                this::loadInstruments,
                                this::loadMarketDepth,
                                this::loadHistoryBars,
                                this::loadAccountOrders,
                                this::loadAccountOrdersHistory,
                                this::loadAccountExecutions,
                                this::loadBalances,
                            };
                        } else {
                            loaders = new Runnable[]{
                                this::loadInstruments,
                                this::loadMarketDepth,
                                this::loadHistoryBars,
                            };
                        }
                        break;
                    case FRAGMENT_WALLET:
                        if (getContentProvider().getUser() != null) {
                            loaders = new Runnable[]{
                                this::loadAccountExecutions,
                                this::loadBalances,
                            };
                        } else {
                            loaders = new Runnable[]{
                            };
                        }
                        break;
                    case FRAGMENT_SETTINGS:
                        loaders = new Runnable[]{
                        };
                        break;
                    case FRAGMENT_LOGIN:
                        loaders = new Runnable[]{
                        };
                        break;
                    default:
                        throw new IllegalArgumentException("switchFragmentTarget");
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
                context.setProgressDialogPostfix(" (" + 1 + "/" + threads.size() + ")");
                for (int i = 0; i < threads.size(); i++) {
                    Thread thread = threads.get(i);
                    thread.join();
                    // Display progress
                    context.setProgressDialogPostfix(" (" + (i + 2) + "/" + threads.size() + ")");
                }
                // Finished progress
                context.hideProgressDialog();
                if (uncaughtException[0] != null) {
                    throw uncaughtException[0];
                }
            } catch (InterruptedException e) {
                // If we interrupted the loader e.g. by a fragment switch, then it's okay and we can ignore the run
                Log.d(TAG, "Old run has been interrupted");
                return;
            } catch (Throwable e) {
                e.printStackTrace();
                Log.e(TAG, "Refresher thread has failed, this run is skipped");
                // TODO: replace by a more conservative approach
                tradingApiHelper.tryNextDomain();
                return;
            }

            synchronized (this) {
                if (reloadFragment != this.switchFragmentTarget) {
                    Log.i(TAG, "Ignoring finished loader threads, as the switch target fragment has changed meanwhile");
                    return;
                }
                // Check the success and switch to the target fragment
                boolean switched = trySwitchFragment(switchFragmentTarget);
            }
        } catch (
            Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Unexpected fatal error within content refresher, exiting.");
            System.exit(1);
        }
    }

    private boolean trySwitchFragment(int switchTargetFragment) {
        if (
            // Exchange
            switchTargetFragment == FRAGMENT_EXCHANGE
                &&
                (
                    getContentProvider().getUser() == null && getContentProvider().isPublicExchangeLoaded()
                        || getContentProvider().getUser() != null && getContentProvider().isPrivateExchangeLoaded()
                )
                //&& getContentProvider().isExchangeCacheNotExpired()

                // Wallet
                || switchTargetFragment == FRAGMENT_WALLET
                && getContentProvider().isWalletLoaded()
                //&& getContentProvider().isWalletCacheNotExpired()

                // Settings
                || switchTargetFragment == FRAGMENT_SETTINGS
                // Login
                || switchTargetFragment == FRAGMENT_LOGIN
        ) {
            // Data is loaded, so hide the dialog that could have been displayed before fragment switch
            this.context.hideProgressDialog();

            if (currentFragment == null || currentFragment != switchTargetFragment) {
                // Full fragment switch
                Log.i(TAG, "Full fragment switch to fragment id " + switchTargetFragment);
                this.context.switchToFragmentAndClear(switchTargetFragment, null);
                this.currentFragment = switchTargetFragment;
            } else {
                // Only re-load the data in a current fragment
                context.runOnUiThread(() -> {
                    ((FragmentSwitcherInterface)
                        this.context
                            .getFragmentsManager()
                            .getSupportFragmentManager()
                            .getFragments()
                            .get(0)
                    ).refreshFragment();
                });
            }
            return true;
        } else {
            Log.i(TAG, "Waiting before fragment loads, because the data was not loaded (or cached data has expired)");
            return false;
        }
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

    private void loadHistoryBars() {
        Log.d(TAG, "loadHistoryBars called");
        final String pair = getContentProvider().getCurrentCurrencyPair();
        BarsArrays historyBars;
        try {
            // API 26 alternative example
            //LocalDateTime.now().minusWeeks(1).toEpochSecond(ZoneOffset.UTC),
            Calendar from = Calendar.getInstance();
            from.add(Calendar.WEEK_OF_YEAR, -1);
            historyBars = tradingApiHelper.historyBars(
                pair,
                getContentProvider().getGraphResolution(),
                from.getTimeInMillis(),
                Calendar.getInstance().getTimeInMillis(),
                null
            );
        } catch (TradingException e) {
            context.runOnUiThread(() -> {
                Toast.makeText(context, "Chyba pri čítaní histórie: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
            throw new TradingException("Loading History Bars for pair " + pair + " received error: " + e.getMessage());
        }
        if (historyBars == null
            || historyBars.getO() == null
            || historyBars.getH() == null
            || historyBars.getL() == null
            || historyBars.getC() == null
            || historyBars.getV() == null
            || historyBars.getT() == null) {
            throw new TradingException("Loading History Bar for pair " + pair + " received null data");
        }
        int referenceSize = historyBars.getT().size();
        if (historyBars.getO().size() != referenceSize
            || historyBars.getH().size() != referenceSize
            || historyBars.getL().size() != referenceSize
            || historyBars.getC().size() != referenceSize
            || historyBars.getV().size() != referenceSize
            || historyBars.getT().size() != referenceSize) {
            throw new TradingException("Loading History Bar for pair " + pair + " received uneven list sizes");
        }
        getContentProvider().setHistoryBars(pair, historyBars);
    }

    private void loadAccountOrders() {
        Log.d(TAG, "loadAccountOrders called");
        List<Order> offers = new ArrayList<>();
        List<io.swagger.client.model.Order> ordersResponse;
        try {
            ordersResponse = tradingApiHelper.accountOrders(getContentProvider().getUser());
        } catch (TradingException e) {
            if (handleAuthenticationExpiration(e)) {
                return;
            }
            context.runOnUiThread(() -> {
                Toast.makeText(context, "Chyba pri ziskavaní vlastných ponúk.", Toast.LENGTH_SHORT).show();
            });
            throw new TradingException("Account Orders received error: " + e.getMessage());
        }
        for (io.swagger.client.model.Order order : ordersResponse) {
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
            if (handleAuthenticationExpiration(e)) {
                return;
            }
            context.runOnUiThread(() -> {
                Toast.makeText(context, "Chyba pri ziskavaní histórie ponúk.", Toast.LENGTH_SHORT).show();
            });
            throw new TradingException("Account Orders History received error: " + e.getMessage());
        }
        List<Order> orderHistory = new ArrayList<>();
        for (io.swagger.client.model.Order orderResponse : orders) {
            orderHistory.add(new Order(
                orderResponse.getLimitPrice() == null ? null : orderResponse.getLimitPrice().doubleValue(),
                orderResponse.getStopPrice() == null ? null : orderResponse.getStopPrice().doubleValue(),
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
            if (handleAuthenticationExpiration(e)) {
                return;
            }
            context.runOnUiThread(() -> {
                Toast.makeText(context, "Chyba pri ziskavaní histórie transakcií.", Toast.LENGTH_SHORT).show();
            });
            throw new TradingException("Account Executions received error: " + e.getMessage());
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

    private void loadBalances() {
        Log.d(TAG, "loadBalances called");
        List<WalletDetails> walletDetailsAccounts;
        try {
            walletDetailsAccounts = tradingApiHelper.accountBalance(getContentProvider().getUser());
        } catch (TradingException e) {
            if (handleAuthenticationExpiration(e)) {
                return;
            }
            context.runOnUiThread(() -> {
                Toast.makeText(context, "Chyba pri čítani zostatkov mien.", Toast.LENGTH_SHORT).show();
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

    private boolean handleAuthenticationExpiration(TradingException e) {
        if ("java.lang.IllegalStateException: Expected BEGIN_OBJECT but was STRING at line 1 column 1 path $".equals(e.getCause().getMessage())) {
            if (continueOffline) {
                return true;
            }
            context.runOnUiThread(() -> {
                if (ContentRefresher.this.authenticationExpirationDialog != null
                    && ContentRefresher.this.authenticationExpirationDialog.isShowing()) {
                    return;
                }
                ContentRefresher.this.authenticationExpirationDialog = DialogHelper.confirmationDialog(
                    context,
                    "Overenie používateľa vypršalo",
                    "Je potrebné prihlásiť sa znovu",
                    () -> switchFragment(FRAGMENT_LOGIN),
                    () -> continueOffline = true,
                    "Prihlásiť sa",
                    "Pokračovať offline"
                );
            });
            return true;
        }
        return false;
    }

    @SuppressLint("StaticFieldLeak")
    public void sendTradingOffer(final Order offer) {
        Log.d(TAG, "sendTradingOffer called");
        // We pause the refresher so that it doesn't display a conflicting progress dialog
        pauseRefresher();
        context.showProgressDialog("Vytváram ponuku");
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    tradingApiHelper.sendTradingOffer(offer);
                } catch (TradingException e) {
                    e.printStackTrace();
                    DialogHelper.alertDialog(context, "Chyba", "Pokus o vytvorenie ponuky skončil s chybou: " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                startRefresher();
                context.hideProgressDialog();
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void deleteTradingOffer(final String orderId) {
        Log.d(TAG, "deleteTradingOffer called");
        // We pause the refresher so that it doesn't display a conflicting progress dialog
        pauseRefresher();
        context.showProgressDialog("Odstraňujem ponuku");
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    tradingApiHelper.deleteTradingOffer(orderId);
                } catch (TradingException e) {
                    e.printStackTrace();
                    DialogHelper.alertDialog(context, "Chyba", "Pokus o odstránenie ponuky skončil s chybou: " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                startRefresher();
                context.hideProgressDialog();
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void generateWallet(final String coinSymbol) {
        Log.d(TAG, "generateWallet called");
        // We pause the refresher so that it doesn't display a conflicting progress dialog
        pauseRefresher();
        context.showProgressDialog("Generujem peňaženku");
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    tradingApiHelper.generateWallet(coinSymbol);
                } catch (TradingException e) {
                    e.printStackTrace();
                    DialogHelper.alertDialog(context, "Chyba", "Pokus o generovanie peňaženky skončil s chybou: " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                startRefresher();
                context.hideProgressDialog();
            }
        }.execute();
    }
}

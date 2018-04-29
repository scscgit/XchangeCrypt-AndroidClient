package cloud.coders.sk.xchangecrypt.ui;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cloud.coders.sk.xchangecrypt.core.Constants;
import cloud.coders.sk.xchangecrypt.datamodel.Coin;
import cloud.coders.sk.xchangecrypt.datamodel.CurrencyPair;
import cloud.coders.sk.xchangecrypt.datamodel.MyTransaction;
import cloud.coders.sk.xchangecrypt.datamodel.Order;
import cloud.coders.sk.xchangecrypt.datamodel.OrderSide;
import cloud.coders.sk.xchangecrypt.datamodel.OrderType;
import cloud.coders.sk.xchangecrypt.datamodel.User;
import cloud.coders.sk.xchangecrypt.listeners.ConnectionListener;
import cloud.coders.sk.xchangecrypt.listeners.FragmentSwitcherInterface;
import cloud.coders.sk.xchangecrypt.network.RestHelper;
import cloud.coders.sk.xchangecrypt.network.TradingApiHelper;
import cloud.coders.sk.xchangecrypt.ui.fragments.ExchangeFragment;
import cloud.coders.sk.xchangecrypt.ui.fragments.LoginFragment;
import cloud.coders.sk.xchangecrypt.ui.fragments.SettingFragment;
import cloud.coders.sk.xchangecrypt.ui.fragments.SplashFragment;
import cloud.coders.sk.xchangecrypt.ui.fragments.WalletFragment;
import cloud.coders.sk.xchangecrypt.utils.Logger;
import cloud.coders.sk.xchangecrypt.utils.Utility;
import cloud.coders.sk.R;
import io.swagger.client.api.TradingPanelBridgeBrokerDataOrdersApi;
import io.swagger.client.model.DepthItem;
import io.swagger.client.model.InlineResponse200;
import io.swagger.client.model.InlineResponse20013;
import io.swagger.client.model.InlineResponse2004;


public class MainActivity extends BaseActivity implements FragmentSwitcherInterface, Constants {

    public static int asyncTaskId = 0;

    private static final String TAG = "[MainActivity]";
    private TextView toolbarTitle;

    private BroadcastReceiver accountOfferDataReceiver;
    private BroadcastReceiver depthDataReceiver;
    private BroadcastReceiver accountHistoryOfferReceiver;
    private BroadcastReceiver sendOfferReceiver;
    private BroadcastReceiver removeOfferReceiver;
    private BroadcastReceiver authorizationReceiver;

    public TradingApiHelper getTradingApiHelper() {
        return tradingApiHelper;
    }

    private TradingApiHelper tradingApiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.initialize(true);
        prepareCache();
        setContentView(R.layout.activity_main);
        setToolbarAndDrawer(savedInstanceState);
        setBottomNavigationView();
        createNetworkReceiver();
        getFragmentsManager().clearAndInit(this, true);
        switchToFragmentAndClear(FRAGMENT_SPLASH, null);
//        tradingApiHelper = new TradingApiHelper(this, getApplicationContext());
        createDumbData();

    }

    private void createDumbData() {

        MyTransaction transaction0 = new MyTransaction(OrderSide.buy, "QBC", "BTC", (float)0.00000311, (float)258.00058265);
        MyTransaction transaction1 = new MyTransaction(OrderSide.buy, "BTC", "QBC", (float)0.02000311, (float)0.058265);
        List<MyTransaction> transactionList = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            transactionList.add(transaction0);
            transactionList.add(transaction1);
        }
        getContentProvider().setAccountTransactionHistory(transactionList);

        Coin coin1 = new Coin("BTC", 0.00025638);
        Coin coin2 = new Coin("QBC", 0.90025211);
        List<Coin> coins = new ArrayList<>();
        coins.add(coin1);
        coins.add(coin2);
        getContentProvider().setCoinsBalance(coins);

        Order offer1 = new Order(0.00000268, "QBC",1252.1965919,"BTC",0.034565856, OrderSide.buy,OrderType.limit );
        Order offer2 = new Order(0.00000270, "QBC",3000.0000000,"BTC",0.008100000, OrderSide.sell,OrderType.limit );

        List<Order> offerList = new ArrayList<>();
        for(int i = 0; i < 10; i++){
         offerList.add(offer1);
         offerList.add(offer2);
        }
        getContentProvider().addToOrders("QBC_BTC", offerList);
        getContentProvider().addToOrders("BTC_QBC", new ArrayList<Order>());

        List<Order> myoffers = new ArrayList<Order>();
        myoffers.add(offer1);
        myoffers.add(offer2);
        getContentProvider().setAccountOrders(myoffers);
        getContentProvider().setUser(new User("0"));

        getContentProvider().setActualCurrencyPair(CurrencyPair.QBC_BTC);
        getContentProvider().setCurrentOrderSide(OrderSide.buy);


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }



    private void setBottomNavigationView(){
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationLayout = (LinearLayout) findViewById(R.id.bottom_navigation_layout);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home_item:
                                switchToFragmentAndClear(FRAGMENT_EXCHANGE, null);
                                break;
                            case R.id.wallet_item:
                                switchToFragmentAndClear(FRAGMENT_WALLET, null);
                                break;
                            case R.id.settings_item:
                                switchToFragmentAndClear(FRAGMENT_SETTINGS, null);
                                break;
                        }
                        return true;
                    }
                });
    }


    private void setToolbarAndDrawer(Bundle savedInstanceState) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        content = (RelativeLayout) findViewById(R.id.content);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        //setContentMarginOn();

    }


//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        mDrawerToggle.syncState();
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        mDrawerToggle.onConfigurationChanged(newConfig);
//    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        if (networkStateReceiver != null)
            registerReceiver(networkStateReceiver, intentFilter);

        if (accountOfferDataReceiver != null) {
            accountOfferDataReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String accountID = intent.getExtras().getString("accountId");
                    int taskID = intent.getExtras().getInt("taskId");
                    if (accountID.equals(getContentProvider().getUser().getAccountId())) {
                        List<Order> offers = new ArrayList<>();
                        for (io.swagger.client.model.Order order : tradingApiHelper.getAccountOrders(accountID).getD()) {
                            //boolean sell, double price, String baseCurrency,
                            // double baseCurrencyAmount, String quoteCurrency, double quoteCurrencyAmount) {
                            offers.add(new Order(
                                    order.getLimitPrice().doubleValue(),
                                    order.getInstrument(),
                                    order.getQty().doubleValue(),
                                    order.getInstrument(),
                                    order.getQty().doubleValue() * order.getLimitPrice().doubleValue(),
                                    OrderSide.valueOf(order.getSide().toString()),
                                    OrderType.valueOf(order.getType().toString())
                            ));
                        }
                        getContentProvider().setAccountOrders(offers);
                        tradingApiHelper.deleteFromPendingTask(taskID);
                        if (tradingApiHelper.noPendingTask()) {
                            hideProgressDialog();
                        }
                    }
                }
            };
        }
        registerReceiver(accountOfferDataReceiver, new IntentFilter("account_offer_update"));

        if (depthDataReceiver != null) {
            depthDataReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String pair = intent.getExtras().getString("pair");
                    int taskID = intent.getExtras().getInt("taskId");
                    List<Order> offers = new ArrayList<>();
                    for (DepthItem order : tradingApiHelper.getDepthData(pair).getD().getAsks()) {
                        //boolean sell, double price, String baseCurrency,
                        // double baseCurrencyAmount, String quoteCurrency, double quoteCurrencyAmount) {
                        offers.add(new Order(
                                0.5,
                                "1",
                                order.get(0).doubleValue(),
                                "2",
                                order.get(0).doubleValue(),
                                OrderSide.buy,
                                OrderType.limit
                        ));
                    }
                    for (DepthItem order : tradingApiHelper.getDepthData(pair).getD().getBids()) {
                        //boolean sell, double price, String baseCurrency,
                        // double baseCurrencyAmount, String quoteCurrency, double quoteCurrencyAmount) {
                        offers.add(new Order(
                                0.5,
                                "1",
                                order.get(0).doubleValue(),
                                "2",
                                order.get(0).doubleValue(),
                                OrderSide.sell,
                                OrderType.limit
                        ));
                    }
                    getContentProvider().addToOrders(pair, offers);
                    tradingApiHelper.deleteFromPendingTask(taskID);
                    if (tradingApiHelper.noPendingTask()) {
                        hideProgressDialog();
                    }

                }
            };
            registerReceiver(depthDataReceiver, new IntentFilter("depth_update"));
        }


        accountHistoryOfferReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int taskID = intent.getExtras().getInt("taskId");
                InlineResponse2004 response = tradingApiHelper.getHistoryAccountOrders(getContentProvider().getUser().getAccountId());
                List<MyTransaction> transactions = new ArrayList<>();
                for (io.swagger.client.model.Order order : response.getD()) {
                    transactions.add(new MyTransaction(
                            OrderSide.valueOf(order.getSide().toString()),
                            order.getInstrument(),
                            order.getInstrument(),
                            order.getLimitPrice().doubleValue(),
                            order.getQty().doubleValue()
                    ));
                }
                getContentProvider().setAccountTransactionHistory(transactions);
                tradingApiHelper.deleteFromPendingTask(taskID);
                if (tradingApiHelper.noPendingTask()) {
                    hideProgressDialog();
                }
            }
        };
        registerReceiver(accountHistoryOfferReceiver, new IntentFilter("account_history_update"));

        sendOfferReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int taskID = intent.getExtras().getInt("taskId");
                tradingApiHelper.deleteFromPendingTask(taskID);
                if (tradingApiHelper.noPendingTask()) {
                    hideProgressDialog();
                }
            }
        };
        registerReceiver(sendOfferReceiver, new IntentFilter("order_send"));

        removeOfferReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int taskID = intent.getExtras().getInt("taskId");
                String orderId = intent.getExtras().getString("orderId");
                getContentProvider().removeOrderById(orderId);
                tradingApiHelper.deleteFromPendingTask(taskID);
                if (tradingApiHelper.noPendingTask()) {
                    hideProgressDialog();
                }
            }
        };
        registerReceiver(removeOfferReceiver, new IntentFilter("order_delete"));

        authorizationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int taskID = intent.getExtras().getInt("taskId");
                tradingApiHelper.deleteFromPendingTask(taskID);
                if (tradingApiHelper.noPendingTask()) {
                    hideProgressDialog();
                }
            }
        };
        registerReceiver(authorizationReceiver, new IntentFilter("auth_update"));

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        RestHelper.getInstance(this).cancelAllRequests();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        getContentProvider().releaseLoadedData();
        super.onDestroy();
        unregisterReceiver(networkStateReceiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void switchToFragment(int fragmentID, Bundle args) {
        switchToFragment(fragmentID, args, false);
    }

    @Override
    public void switchToFragmentAndClear(int fragmentID, Bundle args) {
        switchToFragment(fragmentID, args, true);
    }

    private void switchToFragment(int fragmentID, Bundle args, boolean clear) {
        switch (fragmentID) {
            case FRAGMENT_SPLASH:
                getFragmentsManager().switchFragmentInMainBackStack(SplashFragment.newInstance(args), clear, true);
                break;
            case FRAGMENT_EXCHANGE:
                 //tradingApiHelper.tradingOffersForCurrencyPair(asyncTaskId++,getContentProvider().getActualCurrencyPair().toString());
                 //tradingApiHelper.tradingOffersPerAccount(asyncTaskId++, getContentProvider().getUser());
                 //showProgressDialog("Načítavám dáta");
                getFragmentsManager().switchFragmentInMainBackStack(ExchangeFragment.newInstance(args), clear, true);
                break;
            case FRAGMENT_LOGIN:
                getFragmentsManager().switchFragmentInMainBackStack(LoginFragment.newInstance(args), clear, true);
                break;
            case FRAGMENT_WALLET:
                //tradingApiHelper.getHistoryAccountOrders(getContentProvider().getUser().getAccountId());
                //showProgressDialog("Načítavám dáta");
                getFragmentsManager().switchFragmentInMainBackStack(WalletFragment.newInstance(args), clear, true);
                break;
            case FRAGMENT_SETTINGS:
                getFragmentsManager().switchFragmentInMainBackStack(SettingFragment.newInstance(args), clear, true);
                break;
        }
    }

    protected void setHamburgerFunction() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public void toggleDrawerButtonArrow() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        setBackButtonFunction();
        toggleDrawerButton(0, 1);
        mDrawerToggle.syncState();
    }

    public void toggleDrawerButtonHamburger() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        setHamburgerFunction();
        toggleDrawerButton(1, 0);
        mDrawerToggle.syncState();
    }

    protected void setBackButtonFunction() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTopFragmentInMainBS();
                toggleDrawerButtonHamburger();
            }
        });
    }

    private void toggleDrawerButton(float start, float end) {
        ValueAnimator anim = ValueAnimator.ofFloat(start, end);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float slideOffset = (Float) valueAnimator.getAnimatedValue();
                mDrawerToggle.onDrawerSlide(mDrawerLayout, slideOffset);
            }
        });
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(500);
        anim.start();
    }


    public void changeBottomNavigationVisibility(int visibility) {
        bottomNavigationView.setVisibility(visibility);
    }

    public void changeBottomNavigationColor(int color) {
        bottomNavigationView.setBackgroundColor(color);
    }

    public void changeToolbarColor(int color) {
        toolbar.setBackgroundColor(color);
    }

    public void setToolbarTitle(String text) {
        toolbarTitle.setText(text);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void changeStatusBarColor(int color) {
        Window window = getWindow();
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        color = Color.HSVToColor(hsv);
        window.setStatusBarColor(color);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentsManager().getMainBackStackSize() == 1) {
            if (getFragmentsManager().getTopFragmentInMainBackstack().customOnBackPressed())
                finish();
        } else {
            getFragmentsManager().getTopFragmentInMainBackstack().customOnBackPressed();
            showTopFragmentInMainBS();
        }
    }


    private void createNetworkReceiver() {
        setIsOnline(Utility.isOnline(this));
        networkStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, Intent intent) {
                if (intent.getExtras() != null) {
                    final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
                    ConnectionListener listener = getConnectionListener();
                    if (ni != null && ni.isConnectedOrConnecting()) { //offline > online
                        Logger.e(TAG,"isConnecting");
                        setIsOnline(true);

                        if (isOnline()) {
                            Snackbar.make(MainActivity.this.content,"Pripojené",Snackbar.LENGTH_SHORT).show();
                            if (listener != null)
                                listener.onReconnection();
                        }

                    } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) { //online > offline
                        Logger.e(TAG,"isDisconnecting");
                        setIsOnline(false);
                        if (!isOnline()) {
                            Snackbar.make(MainActivity.this.content,"Žiadne pripojenie na internet",Snackbar.LENGTH_SHORT).show();
                            if (listener != null)
                                listener.onConnectionLost();
                        }

                    }
                }
            }
        };
    }
}


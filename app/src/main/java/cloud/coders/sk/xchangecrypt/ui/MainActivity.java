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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import cloud.coders.sk.xchangecrypt.core.Constants;
import cloud.coders.sk.xchangecrypt.datamodel.Coin;
import cloud.coders.sk.xchangecrypt.datamodel.MyTransaction;
import cloud.coders.sk.xchangecrypt.datamodel.Offer;
import cloud.coders.sk.xchangecrypt.listeners.ConnectionListener;
import cloud.coders.sk.xchangecrypt.listeners.FragmentSwitcherInterface;
import cloud.coders.sk.xchangecrypt.network.RestHelper;
import cloud.coders.sk.xchangecrypt.ui.fragments.ExchangeFragment;
import cloud.coders.sk.xchangecrypt.ui.fragments.LoginFragment;
import cloud.coders.sk.xchangecrypt.ui.fragments.SettingFragment;
import cloud.coders.sk.xchangecrypt.ui.fragments.SplashFragment;
import cloud.coders.sk.xchangecrypt.ui.fragments.WalletFragment;
import cloud.coders.sk.xchangecrypt.utils.Logger;
import cloud.coders.sk.xchangecrypt.utils.Utility;
import cloud.coders.sk.R;
import io.swagger.client.ApiException;
import io.swagger.client.api.TradingPanelBridgeBrokerDataOrdersApi;
import io.swagger.client.api.TradingTerminalIntegrationApi;
import io.swagger.client.model.InlineResponse2004;
import io.swagger.client.model.Order;


public class MainActivity extends BaseActivity implements FragmentSwitcherInterface, Constants {
    private static final String TAG = "[MainActivity]";
    private TextView toolbarTitle;


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
        createDumbData();

    }

    private void createDumbData() {
        /*
        InlineResponse2004 inlineResponse2004;
        try {
            Log.d(MainActivity.class.getSimpleName(),"b4 download test");
//            TradingTerminalIntegrationApi tradingTerminalIntegrationApi = new TradingTerminalIntegrationApi();
            TradingPanelBridgeBrokerDataOrdersApi tradingPanelBridgeBrokerDataOrdersApi = new TradingPanelBridgeBrokerDataOrdersApi();
            tradingPanelBridgeBrokerDataOrdersApi.authorizePost("test","test");
            inlineResponse2004 =              tradingPanelBridgeBrokerDataOrdersApi       .accountsAccountIdOrdersGet("0");
            Log.d(MainActivity.class.getSimpleName(),"after download");
        } catch (TimeoutException | ExecutionException | InterruptedException | ApiException e) {
            e.printStackTrace();
            throw new RuntimeException("2004 error");
        }*/

        MyTransaction transaction0 = new MyTransaction(new Date(), true, "QBC", "BTC", (float)0.00000311, (float)258.00058265);
        MyTransaction transaction1 = new MyTransaction(new Date(), false, "LTC", "BTC", (float)0.02000311, (float)0.058265);
        List<MyTransaction> transactionList = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            transactionList.add(transaction0);
            transactionList.add(transaction1);
        }
        getContentProvider().setTransactionHistory(transactionList);

        Coin coin1 = new Coin("BTC", 0.00025638);
        Coin coin2 = new Coin("QBC", 0.90025211);
        List<Coin> coins = new ArrayList<>();
        coins.add(coin1);
        coins.add(coin2);
        getContentProvider().setCoins(coins);

        Offer offer1 = new Offer(true,0.00000268, "QBC",1252.1965919,"BTC",0.034565856 );
        Offer offer2 = new Offer(true,0.00000270, "QBC",3000.0000000,"BTC",0.008100000 );

        List<Offer> offerList = new ArrayList<>();
        /*
        for (Order order : inlineResponse2004.getD()) {
            //boolean sell, double price, String baseCurrency,
            // double baseCurrencyAmount, String quoteCurrency, double quoteCurrencyAmount) {
            offerList.add(new Offer(
                    order.getSide() != Order.SideEnum.buy,
                    order.getLimitPrice().doubleValue(),
                    order.getInstrument(),
                    order.getQty().doubleValue(),
                    order.getInstrument(),
                    order.getQty().doubleValue() * order.getLimitPrice().doubleValue()
            ));
        }
        */
        getContentProvider().setOffers(offerList);


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
            registerReceiver(networkStateReceiver,intentFilter);
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
                getFragmentsManager().switchFragmentInMainBackStack(ExchangeFragment.newInstance(args), clear, true);
                break;
            case FRAGMENT_LOGIN:
                getFragmentsManager().switchFragmentInMainBackStack(LoginFragment.newInstance(args), clear, true);
                break;
            case FRAGMENT_WALLET:
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


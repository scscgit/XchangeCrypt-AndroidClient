package cloud.coders.sk.xchangecrypt.ui;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.Toast;


import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.JsonObject;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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



public class MainActivity extends BaseActivity implements FragmentSwitcherInterface, Constants, GoogleApiClient.OnConnectionFailedListener {

    public static int asyncTaskId = 0;

    private static final String TAG = "[MainActivity]";
    private TextView toolbarTitle;

    private BroadcastReceiver accountOfferDataReceiver;
    private BroadcastReceiver depthDataReceiver;
    private BroadcastReceiver accountHistoryOfferReceiver;
    private BroadcastReceiver sendOfferReceiver;
    private BroadcastReceiver removeOfferReceiver;
    private BroadcastReceiver authorizationReceiver;

    private Context context;



    public TradingApiHelper getTradingApiHelper() {
        return tradingApiHelper;
    }

    private TradingApiHelper tradingApiHelper;


    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInAccount googleAccount;
    private MobileServiceClient mobileServiceClient;
    private Scope[] scopes;
    private MobileServiceUser mobileServiceUser;


    private MobileServiceClient mClient;

    public class TodoItem {
        public String Id;
        public String Text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.initialize(true);
        this.context = this;
        prepareCache();
        setContentView(R.layout.activity_main);
        setToolbarAndDrawer(savedInstanceState);
        setBottomNavigationView();
        createNetworkReceiver();
        getFragmentsManager().clearAndInit(this, true);
        switchToFragmentAndClear(FRAGMENT_SPLASH, null);
        tradingApiHelper = new TradingApiHelper(this,this);
        createDumbData();

//        GoogleSignInOptions gso1 = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .requestIdToken(getString(R.string.server_client_id))
//                .requestServerAuthCode(getString(R.string.server_client_id))
//                .build();

        GoogleSignInOptions gso1 = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(getString(R.string.google_sign_client_id))
                .requestIdToken(getString(R.string.server_client_id))
                .requestServerAuthCode(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        scopes = gso1.getScopeArray();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso1)
                .build();


        try {
            mobileServiceClient = new MobileServiceClient("https://xchangecrypttest-convergencebackend.azurewebsites.net",this);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void signIn() {
        if (isOnline()) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Nie ste pripojený na internet, prihlásenie nie je možné.", Toast.LENGTH_SHORT);
            toast.show();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    System.exit(1);
                }
            }, 2000);
        }
    }

    public void signOut(View v) {
        Auth.GoogleSignInApi.signOut(googleApiClient);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                googleAccount = task.getResult(ApiException.class);
//            } catch (ApiException e) {
//                Toast.makeText(this, "Prihlásenie cez google neúspešné.",
//                        Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//                //    System.exit(1);
//            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("", "handleSignInResult: " + result.isSuccess());

        if(result.isSuccess()) {
            final GoogleSignInAccount account = result.getSignInAccount();

            final String idToken = account.getIdToken();
            String serverAuthCode = account.getServerAuthCode();

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit().putString("idToken", idToken).apply();
            prefs.edit().putString("serverAuthCode", serverAuthCode).apply();

            new AsyncTask<Void, Void, String>() {

                @Override
                protected String doInBackground(Void... params) {
                    try {

                        StringBuilder scopesBuilder = new StringBuilder("oauth2:");
                        for(Scope scope : scopes) {
                            scopesBuilder//.append("https://www.googleapis.com/auth/")
                                    .append(scope.toString())
                                    .append(" ");
                        }

                        String token = GoogleAuthUtil.getToken(context,
                                account.getEmail(), scopesBuilder.toString());

                        return token;
                    } catch (IOException | GoogleAuthException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String result) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    prefs.edit().putString("accessToken", result).apply();
                    authenticateWithAzure();
                }
            }.execute();
        } else {

        }
    }

    private void authenticateWithAzure() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String idToken = prefs.getString("idToken", null);
        String serverAuthCode = prefs.getString("serverAuthCode", null);
        String accessToken = prefs.getString("accessToken", null);

        JsonObject json = new JsonObject();
        json.addProperty("access_token", accessToken);
        json.addProperty("id_token", idToken);
        json.addProperty("authorization_code", serverAuthCode);

        ListenableFuture<MobileServiceUser> loginFuture =
                mobileServiceClient.login(MobileServiceAuthenticationProvider.Google, json);

        Futures.addCallback(loginFuture, new FutureCallback<MobileServiceUser>() {
            @Override
            public void onSuccess(MobileServiceUser result) {
               mobileServiceUser = result;
               Toast.makeText(context,"Login succesfull.",Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.getMessage(), t);
            }

        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Chyba pri komunikácii " + connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

        private void createDumbData() {
        getContentProvider().setUser(new User("0"));
        MyTransaction transaction0 = new MyTransaction(OrderSide.buy, "QBC", "BTC", (float)0.00000311, (float)258.00058265);
        MyTransaction transaction1 = new MyTransaction(OrderSide.buy, "BTC", "QBC", (float)0.02000311, (float)0.058265);
        List<MyTransaction> transactionList = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            transactionList.add(transaction0);
            transactionList.add(transaction1);
        }
       // getContentProvider().setAccountTransactionHistory(transactionList);

        Coin coin1 = new Coin("BTC", 0.00025638);
        Coin coin2 = new Coin("QBC", 0.90025211);
        List<Coin> coins = new ArrayList<>();
        coins.add(coin1);
        coins.add(coin2);
      //  getContentProvider().setCoinsBalance(coins);

        Order offer1 = new Order(0.00000268,0.00000268, "QBC",1252.1965919,"BTC",0.034565856, OrderSide.buy,OrderType.limit,"1" );
        Order offer2 = new Order(0.00000270,0.00000268, "QBC",3000.0000000,"BTC",0.008100000, OrderSide.sell,OrderType.limit,"2" );

        List<Order> offerList = new ArrayList<>();
        for(int i = 0; i < 10; i++){
         offerList.add(offer1);
         offerList.add(offer2);
        }
      //  getContentProvider().addToOrders("QBC_BTC", offerList);
      //  getContentProvider().addToOrders("BTC_QBC", new ArrayList<Order>());

        List<Order> myoffers = new ArrayList<Order>();
        myoffers.add(offer1);
        myoffers.add(offer2);
        getContentProvider().setAccountOrders(myoffers);
        getContentProvider().setUser(new User("0"));

        getContentProvider().setActualCurrencyPair(CurrencyPair.QBC_BTC);
        getContentProvider().setCurrentOrderSide(OrderSide.buy);

        getContentProvider().addMarketPrice("QBC_BTC",0.00000311);
        getContentProvider().addMarketPrice("BTC_QBC",0.90000311);


    }

    @Override
    protected void onStart() {
        super.onStart();

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
                                getDataBeforeSwitch(FRAGMENT_EXCHANGE,null);
                                break;
                            case R.id.wallet_item:
                                getDataBeforeSwitch(FRAGMENT_WALLET,  null);
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

        if (accountOfferDataReceiver == null) {
            accountOfferDataReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String accountID = intent.getExtras().getString("accountId");
                    int taskID = intent.getExtras().getInt("taskId");
                    String error = intent.getExtras().getString("error");
                    if (error == null) {
                        if (accountID.equals(getContentProvider().getUser().getAccountId())) {
                            List<Order> offers = new ArrayList<>();
                            for (io.swagger.client.model.Order order : tradingApiHelper.getAccountOrders(accountID).getD()) {
                                double price = 0;

                                if (order.getType() == io.swagger.client.model.Order.TypeEnum.market) {
                                    continue;
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
                                offers.add(new Order(
                                        price,
                                        price,
                                        order.getInstrument().split("_")[0],
                                        order.getQty().doubleValue(),
                                        order.getInstrument().split("_")[1],
                                        order.getQty().doubleValue() * price,
                                        OrderSide.valueOf(order.getSide().toString()),
                                        OrderType.valueOf(order.getType().toString())
                                ));
                            }
                            getContentProvider().setAccountOrders(offers);
                        } else {
                            Toast.makeText(context, "Chyba pri ziskavaní dát.", Toast.LENGTH_SHORT).show();
                        }
                        tradingApiHelper.deleteFromPendingTask(taskID);
                        if (tradingApiHelper.noPendingTask()) {
                            hideProgressDialog();
                            switchToFragmentAndClear(FRAGMENT_EXCHANGE, null);
                        }
                    }

                }
            };
        }
        registerReceiver(accountOfferDataReceiver, new IntentFilter("account_offer_update"));

        if (depthDataReceiver == null) {
            depthDataReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String pair = intent.getExtras().getString("pair");
                    int taskID = intent.getExtras().getInt("taskId");
                    String error = intent.getExtras().getString("error");
                    if (error == null) {
                        HashMap<Double, Double> priceAndVolumes = new HashMap<>();
                    List<Order> orders= new ArrayList<>();
                    if (tradingApiHelper.getDepthData(pair).getD() != null) {
                        List<DepthItem> asks = tradingApiHelper.getDepthData(pair).getD().getAsks();
                        if (asks != null) {
                            for (DepthItem offer : asks) {
                                if (offer != null && offer.get(0) != null && offer.get(1) != null) {
                                    if (priceAndVolumes.get(offer.get(0).doubleValue()) == null) {
                                        priceAndVolumes.put(offer.get(0).doubleValue(), offer.get(1).doubleValue());
                                    } else {
                                        priceAndVolumes.put(offer.get(0).doubleValue(), priceAndVolumes.get(offer.get(0).doubleValue()) + offer.get(1).doubleValue());
                                    }
                                }

                            }
                            for (HashMap.Entry<Double, Double> entry : priceAndVolumes.entrySet()) {
                                orders.add(new Order(
                                        entry.getKey(),
                                        entry.getKey(),
                                        pair.split("_")[0],
                                        entry.getValue(),
                                        pair.split("_")[1],
                                        entry.getKey() * entry.getValue(),
                                        OrderSide.buy,
                                        OrderType.unknown
                                ));
                            }
                        }
                        List<DepthItem> bids = tradingApiHelper.getDepthData(pair).getD().getBids();
                        if (bids != null) {
                            priceAndVolumes = new HashMap<>();
                            for (DepthItem offer : tradingApiHelper.getDepthData(pair).getD().getBids()) {
                                if (offer != null && offer.get(0) != null && offer.get(1) != null) {
                                    if (priceAndVolumes.get(offer.get(0).doubleValue()) == null) {
                                        priceAndVolumes.put(offer.get(0).doubleValue(), offer.get(1).doubleValue());
                                    } else {
                                        priceAndVolumes.put(offer.get(0).doubleValue(), priceAndVolumes.get(offer.get(0).doubleValue()) + offer.get(1).doubleValue());
                                    }
                                }
                            }
                            for (HashMap.Entry<Double, Double> entry : priceAndVolumes.entrySet()) {
                                orders.add(new Order(
                                        entry.getKey(),
                                        entry.getKey(),
                                        pair.split("_")[0],
                                        entry.getValue(),
                                        pair.split("_")[1],
                                        entry.getKey() * entry.getValue(),
                                        OrderSide.sell,
                                        OrderType.unknown
                                ));
                            }
                        }
                    }
                    getContentProvider().addToOrders(pair, orders);
                    } else {
                        Toast.makeText(context, "Chyba pri čítaní ponúk.", Toast.LENGTH_SHORT).show();
                    }
                    tradingApiHelper.deleteFromPendingTask(taskID);
                    if (tradingApiHelper.noPendingTask()) {
                        hideProgressDialog();
                        switchToFragmentAndClear(FRAGMENT_EXCHANGE,null);
                    }

                }
            };
        }
        registerReceiver(depthDataReceiver, new IntentFilter("depth_update"));

        if (accountHistoryOfferReceiver == null) {
            accountHistoryOfferReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    int taskID = intent.getExtras().getInt("taskId");
                    String error = intent.getExtras().getString("error");
                    if (error == null) {
                    InlineResponse2004 response = tradingApiHelper.getHistoryAccountOrders(getContentProvider().getUser().getAccountId());
                    List<MyTransaction> transactions = new ArrayList<>();

                    for (io.swagger.client.model.Order order : response.getD()) {
                        double price = 0;
                        switch(order.getType()){
                            case limit:
                                price = order.getLimitPrice().doubleValue();
                                break;
                            case stop:
                                price  = order.getStopPrice().doubleValue();
                                break;
                            case stoplimit:
                                price = order.getStopPrice().doubleValue();
                        }
                        transactions.add(new MyTransaction(
                                OrderSide.valueOf(order.getSide().toString()),
                                order.getInstrument().split("_")[0],
                                order.getInstrument().split("_")[1],
                                price,
                                order.getQty().doubleValue()
                        ));
                    }
                    getContentProvider().setAccountTransactionHistory(transactions);
                    } else {
                        Toast.makeText(context, "Chyba pri ziskavaní histórie.", Toast.LENGTH_SHORT).show();
                    }
                    tradingApiHelper.deleteFromPendingTask(taskID);
                    if (tradingApiHelper.noPendingTask()) {
                        hideProgressDialog();
                        switchToFragmentAndClear(FRAGMENT_WALLET, null);
                    }
                }
            };
        }
        registerReceiver(accountHistoryOfferReceiver, new IntentFilter("account_history_update"));

        if (sendOfferReceiver == null) {
            sendOfferReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    int taskID = intent.getExtras().getInt("taskId");
                    String error = intent.getExtras().getString("error");
                    if (error != null) {
                        Toast.makeText(context, "Chyba pri posielaní ponuky.", Toast.LENGTH_SHORT).show();
                    }
                    tradingApiHelper.deleteFromPendingTask(taskID);
                    if (tradingApiHelper.noPendingTask()) {
                        hideProgressDialog();
                    }
                }
            };
        }
        registerReceiver(sendOfferReceiver, new IntentFilter("order_send"));

        if (removeOfferReceiver == null) {
            removeOfferReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    int taskID = intent.getExtras().getInt("taskId");
                    String error = intent.getExtras().getString("error");
                    if (error == null) {
                    String orderId = intent.getExtras().getString("orderId");
                    getContentProvider().removeOrderById(orderId);
                    } else {
                        Toast.makeText(context, "Chyba pri zmazaní ponuky.", Toast.LENGTH_SHORT).show();
                    }
                    tradingApiHelper.deleteFromPendingTask(taskID);
                    if (tradingApiHelper.noPendingTask()) {
                        hideProgressDialog();
                    }
                }
            };
        }
        registerReceiver(removeOfferReceiver, new IntentFilter("order_delete"));

        if (authorizationReceiver == null) {
            authorizationReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    int taskID = intent.getExtras().getInt("taskId");
                    String error = intent.getExtras().getString("error");
                    if (error != null) {
                        Toast.makeText(context, "Chyba autorizácie.", Toast.LENGTH_SHORT).show();
                    }
                    tradingApiHelper.deleteFromPendingTask(taskID);
                    if (tradingApiHelper.noPendingTask()) {
                        hideProgressDialog();
                    }
                }
            };
        }
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
        unregisterReceiver(removeOfferReceiver);
        unregisterReceiver(sendOfferReceiver);
        unregisterReceiver(authorizationReceiver);
        unregisterReceiver(accountHistoryOfferReceiver);
        unregisterReceiver(depthDataReceiver);
        unregisterReceiver(accountOfferDataReceiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void switchToFragment(int fragmentID, Bundle args) {
        switchToFragment(fragmentID, args, false,false);
    }

    @Override
    public void switchToFragmentAndClear(int fragmentID, Bundle args) {
        switchToFragment(fragmentID, args, true,false);
    }
    @Override
    public void getDataBeforeSwitch(int fragmentID, Bundle args){
        switch (fragmentID) {
            case FRAGMENT_EXCHANGE:
                showProgressDialog("Načítavám dáta");
                tradingApiHelper.tradingOffersForCurrencyPair(asyncTaskId++,getContentProvider().getActualCurrencyPair().toString());
                //tradingApiHelper.tradingOffersPerAccount(asyncTaskId++, getContentProvider().getUser());
                break;
            case FRAGMENT_WALLET:
                //tradingApiHelper.tradingOffersPerAccount(asyncTaskId++, getContentProvider().getUser());
                switchToFragmentAndClear(FRAGMENT_WALLET,null);
                break;
        }
    }

    private void switchToFragment(int fragmentID, Bundle args, boolean clear, boolean getData) {
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

    public void sendOrder(Order order){
        showProgressDialog("Spracovanie ponuky");
        tradingApiHelper.sendTradingOffer(asyncTaskId++, order);
    }

    public void deleteOrder(Order order){
        showProgressDialog("Vymázavanie ponuky");
        tradingApiHelper.deleteTradingOffer(asyncTaskId++,order,getContentProvider().getUser());
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


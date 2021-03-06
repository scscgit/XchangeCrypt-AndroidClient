package bit.xchangecrypt.client.ui;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import bit.xchangecrypt.client.BuildConfig;
import bit.xchangecrypt.client.R;
import bit.xchangecrypt.client.core.Constants;
import bit.xchangecrypt.client.core.ContentProvider;
import bit.xchangecrypt.client.datamodel.Coin;
import bit.xchangecrypt.client.datamodel.MyTransaction;
import bit.xchangecrypt.client.datamodel.Order;
import bit.xchangecrypt.client.datamodel.User;
import bit.xchangecrypt.client.datamodel.enums.OrderSide;
import bit.xchangecrypt.client.datamodel.enums.OrderType;
import bit.xchangecrypt.client.listeners.ConnectionListener;
import bit.xchangecrypt.client.listeners.FragmentSwitcherInterface;
import bit.xchangecrypt.client.network.ContentRefresher;
import bit.xchangecrypt.client.ui.fragments.*;
import bit.xchangecrypt.client.util.*;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.auth0.android.jwt.DecodeException;
import com.auth0.android.jwt.JWT;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.microsoft.identity.client.*;
import com.microsoft.identity.client.exception.MsalClientException;
import com.microsoft.identity.client.exception.MsalException;
import com.microsoft.identity.client.exception.MsalServiceException;
import com.microsoft.identity.client.exception.MsalUiRequiredException;
import com.microsoft.identity.common.internal.providers.oauth2.AuthorizationStrategy;
import io.swagger.client.ApiInvoker;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class MainActivity extends BaseActivity implements FragmentSwitcherInterface, Constants {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String CHANNEL_ID = "1";

    private BroadcastReceiver authorizationReceiver;

    private TextView toolbarTitle;
    private ImageButton helpButton;

    private int notificationId;

    // Azure AD MSAL context variables
    private static PublicClientApplication activeDirectoryApp;

    public void loginDialog() {
        DialogHelper.yesNoConfirmationDialog(
            this,
            getString(R.string.user_is_offline),
            getString(R.string.question_login),
            new Runnable() {
                @Override
                public void run() {
                    // Make sure Exchange is the selected item after returning after logging in
                    bottomNavigationView.setSelectedItemId(R.id.home_item);

                    getContentRefresher().switchFragment(FRAGMENT_LOGIN);
                }
            }
        );
    }

    public ContentRefresher getContentRefresher() {
        return ContentRefresher.getInstance(this);
    }

    public ContentProvider getContentProvider() {
        return ContentProvider.getInstance(this);
    }

    public ImageButton getHelpButton() {
        if (helpButton == null) {
            helpButton = findViewById(R.id.help_button);
        }
        return helpButton;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbarAndDrawer(savedInstanceState);
        setBottomNavigationView();
        createNotificationChannel();

        if (BuildConfig.DEBUG) {
            HttpsTrustHelper.allowAllSSL();
        }

        createNetworkReceiver();
        getFragmentsManager().clearAndInit(this, true);
        switchToFragmentAndClear(FRAGMENT_SPLASH, null);

        // Initialize the MSAL App context
        if (activeDirectoryApp == null) {
            Log.d(TAG, "Initialized the Azure AD MSAL App context");
            if (!"".equals(Constants.AUTHORITY)) {
                activeDirectoryApp = new PublicClientApplication(
                    this,
                    Constants.CLIENT_ID,
                    String.format(Constants.AUTHORITY, Constants.TENANT, Constants.SISU_POLICY)
                );
            } else {
                activeDirectoryApp = new PublicClientApplication(this, Constants.CLIENT_ID);
            }
            try {
                com.microsoft.identity.client.Logger.getInstance().setExternalLogger(new ILoggerCallback() {
                    @Override
                    public void log(String tag, com.microsoft.identity.client.Logger.LogLevel logLevel, String message, boolean containsPII) {
                        //Log.d(tag, "MSAL_LOG: " + message);
                    }
                });
            } catch (IllegalStateException e) {
                // Already set
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // null intent has been observed to happen request prematurely during browser loading on low Android API auth
        if (requestCode == AuthorizationStrategy.BROWSER_FLOW && data == null) {
            Toast.makeText(this,
                "Authentication using browser has failed probably due to low Android API",
                Toast.LENGTH_LONG
            ).show();
        }
        activeDirectoryApp.handleInteractiveRequestRedirect(requestCode, resultCode, data);
    }

    /**
     * Use MSAL to acquireToken for the end-user
     * Pass UserInfo response data to AuthenticatedActivity
     */
    public void onClickedActiveDirectorySignIn() {
        showProgressDialog(getString(R.string.authentication_attempting));
        // Attempt to get a user and acquireTokenSilently
        // If this fails we will do an interactive request
        Log.d(TAG, "onClickedActiveDirectorySignIn");
        IAccount currentAccount =
            MicrosoftIdentityHelper.getUserByPolicy(activeDirectoryApp.getAccounts(), Constants.SISU_POLICY);
        final String[] scopes = Constants.SCOPES.split("\\s+");
        if (currentAccount != null) {
            // We have 1 user
            activeDirectoryApp.acquireTokenSilentAsync(
                scopes,
                currentAccount,
                getAuthSilentCallback());
        } else {
            // We have no user (for required policy)
            activeDirectoryApp.acquireToken(MainActivity.this, scopes, getAuthInteractiveCallback());
        }
    }

    /**
     * Callback used in for silent acquireToken calls.
     * Looks if tokens are in the cache (refreshes if necessary and if we don't forceRefresh)
     * else errors that we need to do an interactive request.
     */
    private AuthenticationCallback getAuthSilentCallback() {
        return new AuthenticationCallback() {
            @Override
            public void onSuccess(AuthenticationResult authenticationResult) {
                activeDirectoryOnSignInSuccess(authenticationResult);
            }

            @Override
            public void onError(MsalException exception) {
                // Failed to acquireToken
                Log.d(TAG, "Silent authentication failed: " + exception.toString());
                final String[] scopes = Constants.SCOPES.split("\\s+");
                if (exception instanceof MsalUiRequiredException) {
                    // Tokens expired or no session, retry with interactive
                    activeDirectoryApp.acquireToken(MainActivity.this, scopes, getAuthInteractiveCallback());
                    return;
                }
                activeDirectoryOnSignInError(exception);
            }

            @Override
            public void onCancel() {
                activeDirectoryOnSignInCancel();
            }
        };
    }

    /**
     * Callback used for interactive request.  If succeeds we use the access
     * token to call the api. Does not check cache.
     */
    private AuthenticationCallback getAuthInteractiveCallback() {
        return new AuthenticationCallback() {
            @Override
            public void onSuccess(AuthenticationResult authenticationResult) {
                activeDirectoryOnSignInSuccess(authenticationResult);
            }

            @Override
            public void onError(MsalException exception) {
                // Failed to acquireToken
                Log.d(TAG, "Interactive authentication failed: " + exception.toString());
                activeDirectoryOnSignInError(exception);
            }

            @Override
            public void onCancel() {
                activeDirectoryOnSignInCancel();
            }
        };
    }

    private void activeDirectoryOnSignInSuccess(AuthenticationResult authenticationResult) {
        Log.d(TAG, "Successfully authenticated");
        String idToken = authenticationResult.getIdToken();
        String accessToken = authenticationResult.getAccessToken();
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Authentication ID Token: " + idToken);
            Log.d(TAG, "Authentication Access Token: " + accessToken);
        }
        getContentRefresher().getApiAuthentication().setApiKeyPrefix("Bearer");
        getContentRefresher().getApiAuthentication().setApiKey(accessToken);
        //callActiveDirectoryToPrepareUser(accessToken);
        decodeIdTokenToPrepareUser(accessToken);
    }

    private void decodeIdTokenToPrepareUser(String idToken) {
        try {
            // No need for hideProgressDialog(), as we switch fragment
            JWT jwt = new JWT(idToken);
            getContentRefresher().setUser(new User(
                jwt.getClaim("sub").asString(),
                "",
                jwt.getClaim("emails").asArray(String.class)[0],
                jwt.getClaim("given_name").asString()
            ));
            Log.d(TAG, "Active Directory successfully configured user");
        } catch (DecodeException e) {
            Toast.makeText(MainActivity.this,
                getString(R.string.login_failed_verification), Toast.LENGTH_LONG
            ).show();
            Log.e(TAG, "DecodeException" + e.getClass().getSimpleName() + " during ActiveDirectory user property configuration: " + e.getMessage());
            activeDirectorySignOutClearCache();
        }
    }

    private void activeDirectoryOnSignInError(MsalException exception) {
        hideProgressDialog();
        Toast.makeText(
            MainActivity.this,
            getString(R.string.authentication_failed, exception.getMessage()),
            Toast.LENGTH_LONG
        ).show();
        if (exception instanceof MsalClientException) {
            // Exception inside MSAL, more info inside MsalError.java
            exception.printStackTrace();
        } else if (exception instanceof MsalServiceException) {
            // Exception when communicating with the STS, likely config issue
            exception.printStackTrace();
        }
    }

    private void activeDirectoryOnSignInCancel() {
        hideProgressDialog();
        // User canceled the authentication
        Log.d(TAG, "User canceled login");
        Toast.makeText(MainActivity.this, getString(R.string.login_canceled), Toast.LENGTH_SHORT).show();
    }

    public void activeDirectorySignOutClearCache() {
        // No need for hideProgressDialog(), as we switch fragment to login
        Log.d(TAG, "AD signing out/clearing app cache");
        List<IAccount> accounts = activeDirectoryApp.getAccounts();
        int accountsCount = accounts.size();
        for (int i = 0; i < accountsCount; i++) {
            activeDirectoryApp.removeAccount(accounts.get(i));
        }
        String message;
        switch (accountsCount) {
            case 0:
                message = "Failed to Sign Out, no user";
                break;
            case 1:
                message = "Signed Out";
                break;
            default:
                message = String.format("Signed Out for %d users", accountsCount);
                break;
        }
        Log.i(TAG, message);
        getContentProvider().setUserAndLoadCache(null);
        getContentRefresher().switchFragment(FRAGMENT_LOGIN);
    }

    /**
     * Use Volley to request the /me endpoint from API.
     * Sets the User properties to what we get back.
     */
    private void callActiveDirectoryToPrepareUser(final String bearerToken) {
        Log.d(TAG, "Starting volley request to Active Directory API");
        showProgressDialog(getString(R.string.dialog_verifying_identity));
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(
            Request.Method.GET,
            Constants.API_URL,
            null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // Successfully called API
                    Log.d(TAG, "Active Directory user properties response: " + response);
                    try {
                        getContentRefresher().setUser(new User(
                            response.getString("sub"),
                            "mockLogin",
                            "mock@user",
                            response.getString("name")
                        ));
                        Log.d(TAG, "Active Directory successfully configured user");
                    } catch (JSONException e) {
                        hideProgressDialog();
                        Toast.makeText(MainActivity.this,
                            getString(R.string.login_failed_verification), Toast.LENGTH_LONG
                        ).show();
                        Log.e(TAG, "JSONException" + e.getClass().getSimpleName() + " during ActiveDirectory user property configuration: " + e.getMessage());
                        activeDirectorySignOutClearCache();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {
                    hideProgressDialog();
                    Toast.makeText(MainActivity.this,
                        getString(R.string.login_failed_verification), Toast.LENGTH_LONG
                    ).show();
                    Log.e(TAG, "VolleyError " + e.getClass().getSimpleName() + " during ActiveDirectory API call: " + e.getMessage());
                    activeDirectorySignOutClearCache();
                }
            }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + bearerToken);
                return headers;
            }
        };
        queue.add(request);
        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                queue.stop();
            }
        });
    }

    @Deprecated
    private void createDumbData() {
        getContentProvider().setUserAndLoadCache(new User("1", "dumbUser", "dumb@email", "dumb name"));
        getContentProvider().setCurrentCurrencyPair("QBC_BTC");
        getContentProvider().setCurrentOrderSide(OrderSide.BUY);
        MyTransaction transaction0 = new MyTransaction(OrderSide.BUY, "QBC", "BTC", (float) 0.00000311, (float) 258.00058265, new Date());
        //MyTransaction transaction1 = new MyTransaction(OrderSide.BUY, "BTC", "QBC", (float)0.02000311, (float)0.058265, new Date());
        MyTransaction transaction1 = new MyTransaction(OrderSide.BUY, "LTC", "BTC", (float) 0.02000235, (float) 0.158265, new Date());
        MyTransaction transaction2 = new MyTransaction(OrderSide.SELL, "LTC", "BTC", (float) 0.02100235, (float) 0.101515, new Date());
        MyTransaction transaction3 = new MyTransaction(OrderSide.SELL, "QBC", "BTC", (float) 0.00000299, (float) 21.00021215, new Date());
        List<MyTransaction> transactionList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            transactionList.add(transaction0);
            transactionList.add(transaction1);
            transactionList.add(transaction2);
            transactionList.add(transaction3);
        }
        getContentProvider().setAccountTransactionHistory(getContentProvider().getCurrentCurrencyPair(), transactionList);
        //getContentProvider().setLastUpdateTime(ContentCacheType.history, new Date());

        Coin coin1 = new Coin("BTC", "testPubKeyBtc", 0.00025638, 0.00025638);
        Coin coin2 = new Coin("QBC", "testPubKeyQbc", 1.90025211, 1.90025211);
        Coin coin3 = new Coin("LTC", "testPubKeyLtc", 5.10015111, 5.10015111);
        List<Coin> coins = new ArrayList<>();
        coins.add(coin1);
        coins.add(coin2);
        coins.add(coin3);
        getContentProvider().setCoinsBalance(coins);

        Order offer1 = new Order(0.00000268, 0.00000268, "LTC", 1252.1965919, 0, "BTC", OrderSide.BUY, OrderType.LIMIT).setOrderId("1");
        Order offer2 = new Order(0.00000270, 0.00000268, "LTC", 3000.0000000, 0, "BTC", OrderSide.BUY, OrderType.LIMIT).setOrderId("2");
        Order offer3 = new Order(0.00000272, 0.00000268, "LTC", 3000.0000000, 0, "BTC", OrderSide.BUY, OrderType.LIMIT).setOrderId("2");
        Order offer4 = new Order(0.00000274, 0.00000268, "LTC", 3000.0000000, 0, "BTC", OrderSide.BUY, OrderType.LIMIT).setOrderId("2");
        Order offer5 = new Order(0.00000282, 0.00000268, "LTC", 3000.0000000, 0, "BTC", OrderSide.BUY, OrderType.LIMIT).setOrderId("2");
        Order offer6 = new Order(0.00000284, 0.00000268, "LTC", 3000.0000000, 0, "BTC", OrderSide.BUY, OrderType.LIMIT).setOrderId("2");
        Order offer7 = new Order(0.00000295, 0.00000268, "LTC", 3000.0000000, 0, "BTC", OrderSide.BUY, OrderType.LIMIT).setOrderId("2");
        Order offer8 = new Order(0.00000296, 0.00000268, "LTC", 3000.0000000, 0, "BTC", OrderSide.BUY, OrderType.LIMIT).setOrderId("2");
        Order offer9 = new Order(0.00000299, 0.00000268, "LTC", 3000.0000000, 0, "BTC", OrderSide.BUY, OrderType.LIMIT).setOrderId("2");

        Order offer11 = new Order(0.00000268, 0.00000268, "QBC", 1252.1965919, 0, "BTC", OrderSide.BUY, OrderType.LIMIT).setOrderId("1");
        Order offer21 = new Order(0.00000270, 0.00000268, "QBC", 3000.0000000, 0, "BTC", OrderSide.BUY, OrderType.LIMIT).setOrderId("2");
        Order offer31 = new Order(0.00000272, 0.00000268, "QBC", 3000.0000000, 0, "BTC", OrderSide.BUY, OrderType.LIMIT).setOrderId("2");
        Order offer41 = new Order(0.00000274, 0.00000268, "QBC", 3000.0000000, 0, "BTC", OrderSide.BUY, OrderType.LIMIT).setOrderId("2");
        Order offer51 = new Order(0.00000282, 0.00000268, "QBC", 3000.0000000, 0, "BTC", OrderSide.BUY, OrderType.LIMIT).setOrderId("2");
        Order offer61 = new Order(0.00000284, 0.00000268, "QBC", 3000.0000000, 0, "BTC", OrderSide.BUY, OrderType.LIMIT).setOrderId("2");
        Order offer71 = new Order(0.00000295, 0.00000268, "QBC", 3000.0000000, 0, "BTC", OrderSide.BUY, OrderType.LIMIT).setOrderId("2");
        Order offer81 = new Order(0.00000296, 0.00000268, "QBC", 3000.0000000, 0, "BTC", OrderSide.BUY, OrderType.LIMIT).setOrderId("2");
        Order offer91 = new Order(0.00000299, 0.00000268, "QBC", 3000.0000000, 0, "BTC", OrderSide.BUY, OrderType.LIMIT).setOrderId("2");

        List<Order> offerList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            offerList.add(offer1);
            offerList.add(offer2);
            offerList.add(offer3);
            offerList.add(offer4);
            offerList.add(offer5);
            offerList.add(offer6);
            offerList.add(offer7);
            offerList.add(offer8);
            offerList.add(offer9);

        }
        List<Order> offerList1 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            offerList1.add(offer11);
            offerList1.add(offer21);
            offerList1.add(offer31);
            offerList1.add(offer41);
            offerList1.add(offer51);
            offerList1.add(offer61);
            offerList1.add(offer71);
            offerList1.add(offer81);
            offerList1.add(offer91);
        }

        getContentProvider().setMarketDepthOrders("QBC_BTC", offerList1);
        getContentProvider().setMarketDepthOrders("LTC_QBC", offerList);

        List<Order> myoffers = new ArrayList<Order>();
        myoffers.add(offer1);
        myoffers.add(offer2);
        getContentProvider().setAccountOrders(myoffers);

//        getContentProvider().setMarketPrice("QBC_BTC", 0.00000311, OrderSide.BUY);
//        getContentProvider().setMarketPrice("QBC_BTC", 0.00000301, OrderSide.SELL);
//        getContentProvider().setMarketPrice("BTC_QBC", 0.901311, OrderSide.BUY);
//        getContentProvider().setMarketPrice("BTC_QBC", 0.91000311, OrderSide.SELL);
//        getContentProvider().setMarketPrice("LTC_BTC", 0.92000311, OrderSide.BUY);
//        getContentProvider().setMarketPrice("LTC_BTC", 0.90001311, OrderSide.SELL);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setBottomNavigationView() {
        bottomNavigationView = findViewById(R.id.main_bottom_navigation);
        bottomNavigationLayout = findViewById(R.id.main_bottom_navigation_layout);
        toolbarTitle = findViewById(R.id.main_toolbar_title);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_item:
                        getContentRefresher().switchFragment(FRAGMENT_EXCHANGE);
                        break;
                    case R.id.wallet_item:
                        if (getContentProvider().getUser() == null) {
                            loginDialog();
                            return true;
                        }
                        getContentRefresher().switchFragment(FRAGMENT_WALLET);
                        break;
                    case R.id.settings_item:
                        if (getContentProvider().getUser() == null) {
                            loginDialog();
                            return true;
                        }
                        getContentRefresher().switchFragment(FRAGMENT_SETTINGS);
                        break;
                }
                return true;
            }
        });
    }

    private void setToolbarAndDrawer(Bundle savedInstanceState) {
        toolbar = findViewById(R.id.main_toolbar);
        content = findViewById(R.id.main_content);
        mDrawerLayout = findViewById(R.id.activity_main);
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
        getContentRefresher().startRefresher();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkStateReceiver, intentFilter);

        if (authorizationReceiver == null) {
            authorizationReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    int taskID = intent.getExtras().getInt("taskId");
                    String error = intent.getExtras().getString("error");
                    if (error != null) {
                        Toast.makeText(context, "Chyba autorizácie.", Toast.LENGTH_SHORT).show();
                    }
//                    tradingApiHelper.deleteFromPendingTask(taskID);
//                    if (tradingApiHelper.noPendingTask()) {
//                        hideProgressDialog();
//                    }
                }
            };
        }
        registerReceiver(authorizationReceiver, new IntentFilter("auth_update"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getContentProvider().isNotifications()) {
            getContentRefresher().pauseExceptNotifications();
        } else {
            getContentRefresher().pauseRefresher();
        }
    }

    @Override
    protected void onStop() {
        ApiInvoker.getInstance().cancelAllRequests();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentProvider().destroy();
        // Content refresher needs to be destroyed in order to reset currentFragment to login
        ContentRefresher.destroy();
        unregisterReceiver(networkStateReceiver);
        unregisterReceiver(authorizationReceiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void switchToFragment(int fragmentID, Bundle args) {
        switchToFragment(fragmentID, args, false, false);
    }

    @Override
    public void switchToFragmentAndClear(int fragmentID, Bundle args) {
        switchToFragment(fragmentID, args, true, false);
    }

    @Deprecated
    public void getDataBeforeSwitch(int fragmentID, Bundle args) {
        getDataBeforeSwitch(fragmentID, args, false);
    }

    @Deprecated
    @Override
    public void getDataBeforeSwitch(int fragmentID, Bundle args, boolean force) {
        switch (fragmentID) {
            case FRAGMENT_EXCHANGE:
                if (!isOnline()) {
                    switchToFragmentAndClear(FRAGMENT_EXCHANGE, null);
                    return;
                }
                if (!force) {
                    if (getContentProvider().isExchangeCacheNotExpired()) {
                        switchToFragmentAndClear(FRAGMENT_EXCHANGE, null);
                        return;
                    }
                }
                // Queue asynchronous downloads
                // Replaced by ContentRefresher
//                tradingApiHelper.marketDepthForPair(asyncTaskId++, getContentProvider().getCurrentCurrencyPair());
//                tradingApiHelper.instruments(asyncTaskId++, getContentProvider().getUser());
//                tradingApiHelper.accountOrders(asyncTaskId++, getContentProvider().getUser());
//                tradingApiHelper.accountExecutions(asyncTaskId++, getContentProvider().getUser(), getContentProvider().getCurrentCurrencyPair(), 30);

                // Exchange fragment also displays available balance
//                tradingApiHelper.accountBalance(asyncTaskId++);

                getContentRefresher().switchFragment(FRAGMENT_EXCHANGE);

                // Switches fragment, synchronously or asynchronously
//                if (getContentProvider().isPrivateExchangeLoaded()) {
//                    switchToFragmentAndClear(FRAGMENT_EXCHANGE, null);
//                } else {
//                    showProgressDialog("Načítavám dáta");
//                    fragmentIdSwitchTarget = FRAGMENT_EXCHANGE;
//                }
                break;
            case FRAGMENT_WALLET:
                if (!isOnline()) {
                    switchToFragmentAndClear(FRAGMENT_WALLET, null);
                    return;
                }
                if (!force) {
                    if (getContentProvider().isWalletCacheNotExpired()) {
                        switchToFragmentAndClear(FRAGMENT_WALLET, null);
                        return;
                    }
                }
                // Queue asynchronous downloads
//                tradingApiHelper.accountOrdersHistory(asyncTaskId++, getContentProvider().getUser(), 30);
//                tradingApiHelper.accountBalance(asyncTaskId++);

                getContentRefresher().switchFragment(FRAGMENT_WALLET);

                // Switches fragment, synchronously or asynchronously
//                if (getContentProvider().isWalletLoaded()) {
//                    switchToFragmentAndClear(FRAGMENT_WALLET, null);
//                } else {
//                    showProgressDialog("Načítavám dáta");
//                    fragmentIdSwitchTarget = FRAGMENT_WALLET;
//                }
                break;
        }
    }

    @Override
    public void refreshFragment() {
        // This is only used by fragments
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

    public void sendOrder(Order order) {
        if (isOnline()) {
            getContentRefresher().sendTradingOffer(order);
        } else {
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
        }
    }

    public void deleteOrder(Order order) {
        if (isOnline()) {
            getContentRefresher().deleteTradingOffer(order.getOrderId());
        } else {
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
        }
    }

    protected void setHamburgerFunction() {
        mDrawerToggle = new ActionBarDrawerToggle(
            this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // creates call to onPrepareOptionsMenu()
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // creates call to onPrepareOptionsMenu()
                invalidateOptionsMenu();
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
        Log.d(TAG, "Back pressed with fragment stack: " + getFragmentsManager().getMainBackStackSize());
        if (getFragmentsManager().getMainBackStackSize() == 1) {
            if (getFragmentsManager().getTopFragmentInMainBackstack().customOnBackPressed())
                finish();
        } else {
            // TODO: cleanup, always returns false
            getFragmentsManager().getTopFragmentInMainBackstack().customOnBackPressed();
            showTopFragmentInMainBS();
        }
    }

    private void createNetworkReceiver() {
        setIsOnline(ConnectionHelper.isOnline(this));
        networkStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, Intent intent) {
                if (intent.getExtras() != null) {
                    final ConnectivityManager connectivityManager = (ConnectivityManager)
                        context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                    ConnectionListener listener = getConnectionListener();
                    if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                        // offline > online
                        Log.d(TAG, "isConnecting");
                        setIsOnline(true);
                        Snackbar.make(MainActivity.this.content, "Spojenie obnovené", Snackbar.LENGTH_SHORT).show();
                        if (listener != null) {
                            listener.onReconnection();
                        }
                    } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                        // online > offline
                        Log.d(TAG, "isDisconnecting");
                        setIsOnline(false);
                        Snackbar.make(MainActivity.this.content, "Žiadne spojenie cez internet", Snackbar.LENGTH_SHORT).show();
                        if (listener != null) {
                            listener.onConnectionLost();
                        }
                    }
                }
            }
        };
    }

    public static String formatNumber(double number) {
        return String.format("%.8f", number).replaceAll("0+$", "0").replaceAll("([^.,])0$", "$1");
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = "";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void createNotification(String coinSymbol, String title, String text) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(CoinHelper.getResourceIdCoin(coinSymbol))
            .setContentTitle(title)
            .setStyle(new NotificationCompat.BigTextStyle()
                .bigText(text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification
        notificationManager.notify(notificationId++, builder.build());
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig) {
        super.onMultiWindowModeChanged(isInMultiWindowMode, newConfig);
        Toast.makeText(this,
            "in multi window: " + isInMultiWindowMode
                + " height " + newConfig.screenHeightDp
                + " width " + newConfig.screenWidthDp
                + " (smallest " + newConfig.smallestScreenWidthDp
                + ")",
            Toast.LENGTH_LONG
        ).show();
    }
}

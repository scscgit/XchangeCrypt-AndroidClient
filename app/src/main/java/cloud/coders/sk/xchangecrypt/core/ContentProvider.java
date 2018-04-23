package cloud.coders.sk.xchangecrypt.core;

import android.content.Context;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cloud.coders.sk.xchangecrypt.datamodel.Coin;
import cloud.coders.sk.xchangecrypt.datamodel.MyTransaction;
import cloud.coders.sk.xchangecrypt.datamodel.Offer;


/**
 * Created by V3502484 on 16. 9. 2016.
 */
public class ContentProvider {
    private static ContentProvider instance;
    private static Context context;


    List<MyTransaction> transactionHistory;
    List<Coin> coins;
    List<Offer> offers;

    private ContentProvider(Context context) {
        ContentProvider.context = context;
        transactionHistory = new ArrayList<>();
        coins = new ArrayList<>();
        offers = new ArrayList<>();
    }

    public static ContentProvider getInstance(Context context) {
        if (instance == null)
            instance = new ContentProvider(context);
        return instance;
    }

    public void destroy() {
        instance = null;
    }

    public void releaseLoadedData() {
      transactionHistory = null;
      coins = null;
      offers = null;
    }

    public List<MyTransaction> getTransactionHistory() {
        return transactionHistory;
    }

    public void setTransactionHistory(List<MyTransaction> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    public List<Coin> getCoins() {
        return coins;
    }

    public void setCoins(List<Coin> coins) {
        this.coins = coins;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }
}

package bit.xchangecrypt.client.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import bit.xchangecrypt.client.R;
import bit.xchangecrypt.client.adapters.WalletListViewAdapter;
import bit.xchangecrypt.client.adapters.WalletRecyclerViewAdapter;
import bit.xchangecrypt.client.datamodel.Coin;
import bit.xchangecrypt.client.datamodel.ContentCacheType;
import bit.xchangecrypt.client.util.DateFormatter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

/**
 * Created by Peter on 21.04.2018.
 */
public class WalletFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private WalletRecyclerViewAdapter adapter;
    private ListView balanceListView;
    private FloatingActionButton floatingActionButton;
    private AppBarLayout appBarLayout;
    private TextView datetextView;

    public static WalletFragment newInstance(Bundle args) {
        WalletFragment fragment = new WalletFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_wallet, container, false);
        setActionBar();
        setViews();
        setViewContents();
        return rootView;
    }

    @Override
    protected void setActionBar() {
        setToolbarTitle("Peňaženka");
    }

    @Override
    protected void setViews() {
        appBarLayout = rootView.findViewById(R.id.wallet_coins_balance);
        floatingActionButton = rootView.findViewById(R.id.wallet_coins_balance_close_fab);
        recyclerView = rootView.findViewById(R.id.recyclerView_wallet);
        balanceListView = rootView.findViewById(R.id.wallet_coins_balance_coin_list);
        datetextView = rootView.findViewById(R.id.wallet_last_updated);
        balanceListView.setClickable(false);
        balanceListView.setScrollContainer(false);

        adapter = new WalletRecyclerViewAdapter(getContentProvider().getAccountTransactionHistory(getContentProvider().getCurrentCurrencyPair()), getContext());
    }

    private boolean loading = true;

    private int pastVisiblesItems, visibleItemCount, totalItemCount;

    @Override
    protected void setViewContents() {
        Date date = getContentProvider().getLastUpdateTime(ContentCacheType.ACCOUNT_TRANSACTION_HISTORY);
        if (date != null) {
            datetextView.setText("Naposledy aktualizované" + "\n" + DateFormatter.getStringFromDate(date, DateFormatter.FORMAT_DD_MM_YYYY_HH_MM_SS));
        }
        List<Coin> coinList = getContentProvider().getCoinsBalance();
        balanceListView.setAdapter(new WalletListViewAdapter(getContext(), coinList));
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appBarLayout.setExpanded(false);
            }
        });

        final float inPixelsDate = getResources().getDimension(R.dimen.date_item);
        final float inPixels = getResources().getDimension(R.dimen.list_item);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        lp.height = (int) ((inPixelsDate) + (inPixels * coinList.size()));

        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            Toast.makeText(getContext(), "End", Toast.LENGTH_SHORT);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void refreshFragment() {
        setViews();
        setViewContents();
        Log.d(WalletFragment.class.getSimpleName(), "Refreshed fragment");
    }
}

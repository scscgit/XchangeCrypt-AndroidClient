package cloud.coders.sk.xchangecrypt.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cloud.coders.sk.R;
import cloud.coders.sk.xchangecrypt.adapters.WalletListViewAdapter;
import cloud.coders.sk.xchangecrypt.adapters.WalletRecyclerViewAdapter;
import cloud.coders.sk.xchangecrypt.datamodel.Coin;
import cloud.coders.sk.xchangecrypt.ui.MainActivity;

/**
 * Created by Peter on 21.04.2018.
 */

public class WalletFragment extends BaseFragment {

    public static WalletFragment newInstance(Bundle args){
        WalletFragment fragment = new WalletFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView recyclerView;
    private WalletRecyclerViewAdapter adapter;
    private ListView balanceListView;
    private FloatingActionButton floatingActionButton;
    private AppBarLayout appBarLayout;

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
        appBarLayout = (AppBarLayout) rootView.findViewById(R.id.app_bar_layout);
        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_wallet);
        balanceListView = (ListView) rootView.findViewById(R.id.listwiew_wallet);
        balanceListView.setClickable(false);
        balanceListView.setScrollContainer(false);

        adapter = new WalletRecyclerViewAdapter(getContentProvider().getAccountTransactionHistory(), getContext(), (MainActivity) getActivity());

    }


    private boolean loading = true;

    private int pastVisiblesItems, visibleItemCount, totalItemCount;

    @Override
    protected void setViewContents() {
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


        final float inPixels= getResources().getDimension(R.dimen.list_item);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)appBarLayout.getLayoutParams();
        lp.height = (int)(inPixels*coinList.size());

        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            Toast.makeText(getContext(),"End",Toast.LENGTH_SHORT);
                        }
                    }
                }
            }
        });


    }
}

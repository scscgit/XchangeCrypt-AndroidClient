package cloud.coders.sk.xchangecrypt.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import cloud.coders.sk.R;
import cloud.coders.sk.xchangecrypt.adapters.WalletListViewAdapter;
import cloud.coders.sk.xchangecrypt.adapters.WalletRecyclerViewAdapter;
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
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_wallet);
        balanceListView = (ListView) rootView.findViewById(R.id.listwiew_wallet);
        balanceListView.setClickable(false);
        balanceListView.setScrollContainer(false);

        adapter = new WalletRecyclerViewAdapter(getContentProvider().getTransactionHistory(), getContext(), (MainActivity) getActivity());

    }

    @Override
    protected void setViewContents() {

        balanceListView.setAdapter(new WalletListViewAdapter(getContext(), getContentProvider().getCoins()));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

    }
}

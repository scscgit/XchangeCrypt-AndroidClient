package cloud.coders.sk.xchangecrypt.ui.fragments;

import android.media.Image;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


import cloud.coders.sk.R;
import cloud.coders.sk.xchangecrypt.adapters.ExchangeOrderListViewAdapter;
import cloud.coders.sk.xchangecrypt.ui.MainActivity;

/**
 * Created by V3502505 on 20/09/2016.
 */
public class ExchangeFragment extends BaseFragment {

    private TextView firstCurrencyText;
    private ImageView firstCurrencyLogo;
    private TextView secondCurrencyText;
    private ImageView secondCurrencyLogo;

    private TextView ballanceText;

    private ListView listViewOrders;

    private EditText amountEdit;
    private TextView amountCoin;

    private EditText priceEdit;
    private TextView priceCoin;

    private EditText feeEdit;
    private TextView feeCoin;

    private EditText sumEdit;
    private TextView sumtCoin;

    private Button buttonOrder;



    private RecyclerView recyclerView;

    public static ExchangeFragment newInstance(Bundle args){
        ExchangeFragment fragment = new ExchangeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_exchange,container,false);


        setActionBar();
        setViews();
        setViewContents();

        return rootView;
    }

    @Override
    protected void setActionBar() {
        showActionBar();
        setToolbarTitle("Zmenáreň");
        ((MainActivity)getActivity()).changeBottomNavigationVisibility(View.VISIBLE);
    }

    @Override
    protected void setViews() {
        firstCurrencyText = (TextView) rootView.findViewById(R.id.first_coin_logo_text);
        firstCurrencyLogo = (ImageView) rootView.findViewById(R.id.first_coin_logo_image);
        secondCurrencyText = (TextView) rootView.findViewById(R.id.second_coin_logo_text);
        secondCurrencyLogo = (ImageView) rootView.findViewById(R.id.second_coin_logo_image);
        ballanceText = (TextView) rootView.findViewById(R.id.exchange_balance_text);
        listViewOrders = (ListView) rootView.findViewById(R.id.listview_orders);
        amountEdit = (EditText) rootView.findViewById(R.id.exchange_amount_edit);
        amountCoin = (TextView) rootView.findViewById(R.id.exchange_amount_coin_text);

        priceEdit = (EditText) rootView.findViewById(R.id.exchange_price_edit);
        priceCoin = (TextView) rootView.findViewById(R.id.exchange_price_coin_text);

        feeEdit = (EditText) rootView.findViewById(R.id.exchange_fee_edit);
        feeCoin = (TextView) rootView.findViewById(R.id.exchange_fee_coin_text);

        sumEdit = (EditText) rootView.findViewById(R.id.exchange_sum_edit);
        sumtCoin = (TextView) rootView.findViewById(R.id.exchange_sum_coin_text);

        buttonOrder = (Button) rootView.findViewById(R.id.button_order);
    }

    @Override
    protected void setViewContents() {
        listViewOrders.setAdapter(new ExchangeOrderListViewAdapter(getContext(), getContentProvider().getOffers()));
        listViewOrders.setClickable(false);
        ViewGroup header = (ViewGroup) getLayoutInflater().inflate(R.layout.listview_order_header, listViewOrders, false);
        listViewOrders.addHeaderView(header);

        ballanceText.setText(String.format("%.8f", getContentProvider().getCoins().get(0).getAmount()) + " " + getContentProvider().getCoins().get(0).getName());
    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


}

package cloud.coders.sk.xchangecrypt.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import cloud.coders.sk.R;
import cloud.coders.sk.xchangecrypt.datamodel.Order;

/**
 * Created by Peter on 23.04.2018.
 */
public class ExchangeOrderListViewAdapter extends ArrayAdapter {
    private Context context;
    private List<Order> offers;
    private boolean marketOrders;

    public ExchangeOrderListViewAdapter(Context context, @NonNull List<Order> offers, boolean marketOrders) {
        super(context, R.layout.listview_order_item, offers);
        this.context = context;
        this.offers = offers;
        this.marketOrders = marketOrders;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView;
        if (marketOrders) {
            rowView = inflater.inflate(R.layout.listview_order_item_notype, parent, false);
        } else {
            rowView = inflater.inflate(R.layout.listview_order_item, parent, false);
        }

        Order offer = offers.get(position);
        TextView type = rowView.findViewById(R.id.listview_orders_item_type);
        TextView price = rowView.findViewById(R.id.listview_orders_item_price);
        TextView coin1 = rowView.findViewById(R.id.listview_orders_item_coin1);
        TextView coin2 = rowView.findViewById(R.id.listview_orders_item_coin2);

        price.setText(String.format("%.8f", offer.getPrice()));
        coin1.setText(String.format("%.8f", offer.getBaseCurrencyAmount()));
        coin2.setText(String.format("%.8f", offer.getQuoteCurrencyAmount()));

        if (!marketOrders) {
            switch (offer.getType()) {
                case limit:
                    type.setText("L");
                    break;
                case stop:
                    type.setText("S");
                    break;
                case market:
                    type.setText("M");
            }
        }
        return rowView;
    }
}

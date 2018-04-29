package cloud.coders.sk.xchangecrypt.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cloud.coders.sk.R;
import cloud.coders.sk.xchangecrypt.datamodel.Order;

/**
 * Created by Peter on 23.04.2018.
 */

public class ExchangeOrderListViewAdapter extends ArrayAdapter {

    private Context context;
    private List<Order> offers;

    public ExchangeOrderListViewAdapter(Context context, List<Order> offers) {
        super(context, R.layout.listview_order_item, offers);
        this.context = context;
        this.offers = offers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listview_order_item, parent, false);

        Order offer = offers.get(position);
        TextView price = (TextView) rowView.findViewById(R.id.listview_orders_item_price);
        TextView coin1 = (TextView) rowView.findViewById(R.id.listview_orders_item_coin1);
        TextView coin2 = (TextView) rowView.findViewById(R.id.listview_orders_item_coin2);

        price.setText(String.format("%.8f", offer.getPrice()));
        coin1.setText(String.format("%.8f", offer.getBaseCurrencyAmount()));
        coin2.setText(String.format("%.8f", offer.getQuoteCurrencyAmount()));

        return rowView;
    }

}

package bit.xchangecrypt.client.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import bit.xchangecrypt.client.R;
import bit.xchangecrypt.client.datamodel.Order;
import bit.xchangecrypt.client.datamodel.enums.OrderType;
import bit.xchangecrypt.client.util.CompositeUnmodifiableList;

import java.util.List;

/**
 * Created by Peter on 23.04.2018.
 */
public class ExchangeOrderListViewAdapter extends ArrayAdapter<Order> {
    private Context context;
    private List<Order> openOrders;
    private List<Order> closedOrders;
    private boolean marketOrders;

    public ExchangeOrderListViewAdapter(Context context, @NonNull List<Order> openOrders, @NonNull List<Order> closedOrders, boolean marketOrders) {
        super(context, R.layout.item_order_user, new CompositeUnmodifiableList<>(openOrders, closedOrders));
        this.context = context;
        this.openOrders = openOrders;
        this.closedOrders = closedOrders;
        this.marketOrders = marketOrders;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout rowView;
        if (marketOrders) {
            rowView = (LinearLayout) inflater.inflate(R.layout.item_order_depth, parent, false);
        } else {
            rowView = (LinearLayout) inflater.inflate(R.layout.item_order_user, parent, false);
        }
        boolean openOrder = position < openOrders.size();
        Order offer;
        if (openOrder) {
            offer = openOrders.get(position);
        } else {
            offer = closedOrders.get(position - openOrders.size());
            rowView.setBackgroundColor(context.getResources().getColor(R.color.gray));
        }

        TextView type = rowView.findViewById(R.id.listview_orders_item_type);
        TextView price = rowView.findViewById(R.id.listview_orders_item_price);
        TextView coin1 = rowView.findViewById(R.id.listview_orders_item_coin1);
        TextView coin2 = rowView.findViewById(R.id.listview_orders_item_coin2);
        double offerPrice = offer.getType() == OrderType.STOP ? offer.getStopPrice() : offer.getLimitPrice();
        price.setText(String.format("%.8f", offerPrice));
        coin1.setText(String.format("%.8f", offer.getBaseCurrencyAmount()));
        coin2.setText(String.format("%.8f", offerPrice * offer.getBaseCurrencyAmount()));
        if (!marketOrders) {
            switch (offer.getType()) {
                case LIMIT:
                    type.setText(openOrder ? "L" : "C:L");
                    break;
                case STOP:
                    type.setText(openOrder ? "S" : "C:S");
                    break;
                case MARKET:
                    type.setText(openOrder ? "M" : "C:M");
            }
        }
        return rowView;
    }
}

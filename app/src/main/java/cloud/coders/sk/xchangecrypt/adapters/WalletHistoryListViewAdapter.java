package cloud.coders.sk.xchangecrypt.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cloud.coders.sk.R;
import cloud.coders.sk.xchangecrypt.datamodel.Coin;
import cloud.coders.sk.xchangecrypt.datamodel.MyTransaction;
import cloud.coders.sk.xchangecrypt.datamodel.OrderSide;

/**
 * Created by Peter on 05.05.2018.
 */

public class WalletHistoryListViewAdapter extends ArrayAdapter {

    private Context context;
    private List<MyTransaction> transactions;

    public WalletHistoryListViewAdapter(Context context, List<MyTransaction> transactions) {
        super(context, R.layout.listview_wallet_item, transactions);
        this.context = context;
        this.transactions = transactions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.recyclerview_wallet_item, parent, false);

        MyTransaction transaction = transactions.get(position);

        TextView title = (TextView) rowView .findViewById(R.id.title_text);
        TextView price = (TextView) rowView .findViewById(R.id.price_text);
        TextView amount = (TextView) rowView .findViewById(R.id.amount_text);
        //  date = (TextView) view.findViewById(R.id.date_text);
        ImageView logo = (ImageView) rowView .findViewById(R.id.coin_image);


        StringBuilder builder = new StringBuilder();
        if (transaction.getSide() == OrderSide.sell){
            builder.append("Predaj ");
        }else {
            builder.append("Nákup ");
        }



        builder.append(transaction.getBaseCurrency()).append("/").append(transaction.getQuoteCurrency());
        title.setText(builder.toString());

        price.setText(String.format("%.6f", transaction.getPrice()));
        amount.setText(String.format("%.6f", transaction.getAmount()));

        //date.setText(DateFormatter.getStringFromDate(transaction.getDate(), DateFormatter.FORMAT_DD_MM_YYYY));


        if (transaction.getSide() == OrderSide.buy) {
            switch (transaction.getQuoteCurrency()) {
                case "BTC":
                    logo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.btc_icon));
                    break;
                case "QBC":
                    logo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.qbc_icon));
                    break;
                case "LTC":
                    logo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ltc_icon));
            }
        }else {
            switch (transaction.getBaseCurrency()) {
                case "BTC":
                    logo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.btc_icon));
                    break;
                case "QBC":
                    logo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.qbc_icon));
                    break;
                case "LTC":
                    logo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ltc_icon));
                    break;
            }
        }




        return rowView;
    }
}
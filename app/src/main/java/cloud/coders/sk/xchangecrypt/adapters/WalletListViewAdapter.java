package cloud.coders.sk.xchangecrypt.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import cloud.coders.sk.R;
import cloud.coders.sk.xchangecrypt.datamodel.Coin;

/**
 * Created by Peter on 22.04.2018.
 */
public class WalletListViewAdapter extends ArrayAdapter {
    private Context context;
    private List<Coin> coins;

    public WalletListViewAdapter(Context context, @NonNull List<Coin> coins) {
        super(context, R.layout.listview_wallet_item, coins);
        Objects.requireNonNull(coins);
        this.context = context;
        this.coins = coins;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listview_wallet_item, parent, false);

        Coin coin = coins.get(position);
        TextView coinTitle = (TextView) rowView.findViewById(R.id.listview_coin_text);
        TextView amount = (TextView) rowView.findViewById(R.id.listview_coin_amount);
        ImageView logo = (ImageView) rowView.findViewById(R.id.listview_coin_logo);
        coinTitle.setText(coin.getSymbolName());
        amount.setText(String.format("%.6f", coin.getAmount()));
        switch (coin.getSymbolName()) {
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
        return rowView;
    }
}

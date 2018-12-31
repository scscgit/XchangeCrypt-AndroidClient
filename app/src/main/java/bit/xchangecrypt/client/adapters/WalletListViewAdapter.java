package bit.xchangecrypt.client.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import bit.xchangecrypt.client.R;
import bit.xchangecrypt.client.datamodel.Coin;

import java.util.List;

/**
 * Created by Peter on 22.04.2018.
 */
public class WalletListViewAdapter extends ArrayAdapter<Coin> {
    private Context context;
    private List<Coin> coins;

    public WalletListViewAdapter(Context context, @NonNull List<Coin> coins) {
        super(context, R.layout.item_wallet_coin_balance, coins);
        this.context = context;
        this.coins = coins;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_wallet_coin_balance, parent, false);

        Coin coin = coins.get(position);
        TextView coinTitle = rowView.findViewById(R.id.listview_coin_text);
        TextView amount = rowView.findViewById(R.id.listview_coin_amount);
        ImageView logo = rowView.findViewById(R.id.listview_coin_logo);
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

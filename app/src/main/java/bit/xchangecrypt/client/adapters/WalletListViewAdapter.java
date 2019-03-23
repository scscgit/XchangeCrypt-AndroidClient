package bit.xchangecrypt.client.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import bit.xchangecrypt.client.R;
import bit.xchangecrypt.client.datamodel.Coin;
import bit.xchangecrypt.client.util.CoinHelper;

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
        ImageView logo = rowView.findViewById(R.id.listview_coin_logo);
        TextView coinTitle = rowView.findViewById(R.id.listview_coin_text);
        TextView coinAmount = rowView.findViewById(R.id.listview_coin_amount);
        TextView coinPublicKey = rowView.findViewById(R.id.listview_coin_public_key);
        Button coinGenerateButton = rowView.findViewById(R.id.listview_coin_generate_button);
        coinTitle.setText(coin.getSymbolName());
        coinAmount.setText(String.format("%.6f", coin.getAmount()));
        coinPublicKey.setText(coin.getPublicKey());
        logo.setImageDrawable((CoinHelper.getDrawableForCoin(context, coin.getSymbolName())));
        return rowView;
    }
}

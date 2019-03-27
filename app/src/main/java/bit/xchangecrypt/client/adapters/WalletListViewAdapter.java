package bit.xchangecrypt.client.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import bit.xchangecrypt.client.R;
import bit.xchangecrypt.client.datamodel.Coin;
import bit.xchangecrypt.client.ui.fragments.WalletFragment;
import bit.xchangecrypt.client.util.CoinHelper;
import bit.xchangecrypt.client.util.DialogHelper;

import java.util.List;

/**
 * Created by Peter on 22.04.2018.
 */
public class WalletListViewAdapter extends ArrayAdapter<Coin> {
    private WalletFragment walletFragment;
    private List<Coin> coins;

    public WalletListViewAdapter(WalletFragment walletFragment, @NonNull List<Coin> coins) {
        super(walletFragment.getContext(), R.layout.item_wallet_coin_balance, coins);
        this.walletFragment = walletFragment;
        this.coins = coins;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_wallet_coin_balance, parent, false);

        Coin coin = coins.get(position);
        ImageView logo = rowView.findViewById(R.id.listview_coin_logo);
        TextView coinTitle = rowView.findViewById(R.id.listview_coin_text);
        TextView coinAmount = rowView.findViewById(R.id.listview_coin_amount);
        TextView coinPublicKey = rowView.findViewById(R.id.listview_coin_public_key);
        Button coinGenerateButton = rowView.findViewById(R.id.listview_coin_generate_button);

        logo.setImageDrawable((CoinHelper.getDrawableForCoin(getContext(), coin.getSymbolName())));
        coinTitle.setText(coin.getSymbolName());
        coinAmount.setText(String.format("%.8f", coin.getAmount()).replaceAll("0+$", "0") + " " + coin.getSymbolName());
        coinPublicKey.setText(coin.getPublicKey());
        coinPublicKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(coin.getSymbolName() + " public key", coin.getPublicKey());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Skopírované do schránky: " + coin.getPublicKey(), Toast.LENGTH_LONG).show();
            }
        });
        coinGenerateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelper.confirmationDialog(
                    getContext(),
                    "Generovanie adresy",
                    "Prajete si vygenerovať novú adresu?",
                    () -> walletFragment.getMainActivity().getContentRefresher().generateWallet(coin.getSymbolName())
                );
            }
        });
        return rowView;
    }
}

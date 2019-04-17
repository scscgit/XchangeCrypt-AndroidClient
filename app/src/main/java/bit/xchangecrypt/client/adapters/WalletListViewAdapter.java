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
import bit.xchangecrypt.client.ui.MainActivity;
import bit.xchangecrypt.client.ui.fragments.ExchangeFragment;
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
        Button coinActionButton = rowView.findViewById(R.id.listview_coin_action);

        logo.setImageDrawable((CoinHelper.getDrawableForCoin(getContext(), coin.getSymbolName())));
        coinTitle.setText(coin.getSymbolName());
        coinAmount.setText(String.format(
            "%s/%s %s",
            MainActivity.formatNumber(coin.getAvailableAmount()),
            MainActivity.formatNumber(coin.getAmount()),
            coin.getSymbolName()
        ));
        coinPublicKey.setText(coin.getPublicKey());
        coinPublicKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyAddress(coin);
            }
        });
        coinActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelper.choiceDialogBuilder(
                    getContext(),
                    getContext().getString(R.string.wallet_action),
                    getContext().getString(R.string.wallet_choose_action, coin.getSymbolName()),
                    () -> {
                        View withdrawalDialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_wallet_withdrawal, null);
                        TextView amountText = withdrawalDialogView.findViewById(R.id.withdrawal_amount_text);
                        EditText amountEdit = withdrawalDialogView.findViewById(R.id.withdrawal_amount);
                        EditText addressEdit = withdrawalDialogView.findViewById(R.id.withdrawal_address);
                        amountText.setText(getContext().getString(R.string.wallet_withdrawal_amount, coin.getSymbolName()));
                        DialogHelper.choiceDialogBuilder(
                            getContext(),
                            getContext().getString(R.string.wallet_withdrawal),
                            null,
                            () -> {
                                double amount;
                                if (amountEdit.getText().toString().trim().length() <= 0) {
                                    Toast.makeText(getContext(), getContext().getString(R.string.missing_amount), Toast.LENGTH_LONG).show();
                                    return;
                                }
                                try {
                                    amount = ExchangeFragment.parseValue(amountEdit);
                                } catch (NumberFormatException e) {
                                    DialogHelper.alertDialog(walletFragment.getMainActivity(), getContext().getString(R.string.invalid_value), getContext().getString(R.string.please_fix_amount));
                                    return;
                                }
                                if (addressEdit.getText().toString().trim().length() <= 0) {
                                    Toast.makeText(getContext(), getContext().getString(R.string.missing_address), Toast.LENGTH_LONG).show();
                                    return;
                                }
                                walletFragment.getMainActivity().getContentRefresher().withdraw(coin, addressEdit.getText().toString().trim(), amount);
                            },
                            null,
                            getContext().getString(R.string.ok_btn),
                            getContext().getString(R.string.cancel_btn),
                            false
                        ).setView(withdrawalDialogView)
                            .show();
                    },
                    () -> DialogHelper.yesNoConfirmationDialog(
                        getContext(),
                        getContext().getString(R.string.wallet_generate_address),
                        getContext().getString(R.string.wallet_generate_address_question, coin.getSymbolName()),
                        () -> walletFragment.getMainActivity().getContentRefresher().generateWallet(coin.getSymbolName())
                    ),
                    getContext().getString(R.string.wallet_withdrawal),
                    getContext().getString(R.string.wallet_generate_address),
                    false
                ).setNeutralButton(
                    getContext().getString(R.string.wallet_copy_address),
                    (dialog, which) -> copyAddress(coin)
                ).show();
            }
        });
        return rowView;
    }

    private void copyAddress(Coin coin) {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(coin.getSymbolName() + " public key", coin.getPublicKey());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(), "Skopírované do schránky: " + coin.getPublicKey(), Toast.LENGTH_LONG).show();
    }
}

package bit.xchangecrypt.client.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import bit.xchangecrypt.client.R;
import bit.xchangecrypt.client.datamodel.MyTransaction;
import bit.xchangecrypt.client.datamodel.enums.OrderSide;
import bit.xchangecrypt.client.ui.fragments.WalletFragment;
import bit.xchangecrypt.client.util.CoinHelper;
import bit.xchangecrypt.client.util.DateFormatter;

import java.util.List;

/**
 * Created by Peter on 22.04.2018.
 */
public class WalletRecyclerViewAdapter extends RecyclerView.Adapter<WalletRecyclerViewAdapter.MyViewHolder> {
    private List<MyTransaction> transactions;
    private WalletFragment context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, price, amount, date;
        ImageView logo;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title_text);
            price = view.findViewById(R.id.price_text);
            amount = view.findViewById(R.id.amount_text);
            date = view.findViewById(R.id.date_text);
            logo = view.findViewById(R.id.coin_image);
        }
    }

    public WalletRecyclerViewAdapter(@NonNull List<MyTransaction> transactions, WalletFragment context) {
        this.transactions = transactions;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_wallet_transaction, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyTransaction transaction = transactions.get(position);
        StringBuilder builder = new StringBuilder();
        if (transaction.getSide() == OrderSide.SELL) {
            builder.append("Predaj ");
        } else {
            builder.append("Nákup ");
        }
        builder.append(transaction.getBaseCurrency()).append("/").append(transaction.getQuoteCurrency());
        holder.title.setText(builder.toString());
        holder.price.setText(context.formatNumber(transaction.getPrice()));
        holder.amount.setText(context.formatNumber(transaction.getAmount()));
        holder.date.setText(DateFormatter.getStringFromDate(transaction.getDate(), DateFormatter.FORMAT_DD_MM_YYYY_HH_MM_SS));
        if (transaction.getSide() == OrderSide.BUY) {
            holder.logo.setImageDrawable(CoinHelper.getDrawableForCoin(context.getContext(), transaction.getQuoteCurrency()));
        } else {
            holder.logo.setImageDrawable(CoinHelper.getDrawableForCoin(context.getContext(), transaction.getBaseCurrency()));
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
}

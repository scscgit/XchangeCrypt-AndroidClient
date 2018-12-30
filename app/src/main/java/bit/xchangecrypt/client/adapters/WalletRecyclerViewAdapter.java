package bit.xchangecrypt.client.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import bit.xchangecrypt.client.R;
import bit.xchangecrypt.client.datamodel.MyTransaction;
import bit.xchangecrypt.client.datamodel.enums.OrderSide;
import bit.xchangecrypt.client.util.DateFormatter;

/**
 * Created by Peter on 22.04.2018.
 */
public class WalletRecyclerViewAdapter extends RecyclerView.Adapter<WalletRecyclerViewAdapter.MyViewHolder> {
    private List<MyTransaction> transactions;
    private Context context;

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

    public WalletRecyclerViewAdapter(@NonNull List<MyTransaction> transactions, Context context) {
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
        holder.price.setText(String.format("%.6f", transaction.getPrice()));
        holder.amount.setText(String.format("%.6f", transaction.getAmount()));
        holder.date.setText(DateFormatter.getStringFromDate(transaction.getDate(), DateFormatter.FORMAT_DD_MM_YYYY_HH_MM_SS));
        if (transaction.getSide() == OrderSide.BUY) {
            switch (transaction.getQuoteCurrency()) {
                case "BTC":
                    holder.logo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.btc_icon));
                    break;
                case "QBC":
                    holder.logo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.qbc_icon));
                    break;
                case "LTC":
                    holder.logo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ltc_icon));
            }
        } else {
            switch (transaction.getBaseCurrency()) {
                case "BTC":
                    holder.logo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.btc_icon));
                    break;
                case "QBC":
                    holder.logo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.qbc_icon));
                    break;
                case "LTC":
                    holder.logo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ltc_icon));
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
}

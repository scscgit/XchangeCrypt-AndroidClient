package cloud.coders.sk.xchangecrypt.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cloud.coders.sk.R;
import cloud.coders.sk.xchangecrypt.datamodel.MyTransaction;
import cloud.coders.sk.xchangecrypt.ui.MainActivity;
import cloud.coders.sk.xchangecrypt.utils.DateFormatter;

/**
 * Created by Peter on 22.04.2018.
 */

public class WalletRecyclerViewAdapter extends RecyclerView.Adapter<WalletRecyclerViewAdapter.MyViewHolder>
{
    private List<MyTransaction> transactions;
    private Context context;
    private MainActivity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, price, amount, date;
        public ImageView logo;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title_text);
            price = (TextView) view.findViewById(R.id.price_text);
            amount = (TextView) view.findViewById(R.id.amount_text);
            date = (TextView) view.findViewById(R.id.date_text);
            logo = (ImageView) view.findViewById(R.id.coin_image);
        }
    }

    public WalletRecyclerViewAdapter (List<MyTransaction> transactions, Context context, MainActivity activity) {
        this.transactions = transactions;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_wallet_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyTransaction transaction = transactions.get(position);
        StringBuilder builder = new StringBuilder();
        if (transaction.isSell()){
           builder.append("Predaj ");
        }else {
            builder.append("Nákup ");
        }
        builder.append(transaction.getBaseCurrency()).append("/").append(transaction.getQuoteCurrency());
        holder.title.setText(builder.toString());

        holder.price.setText(String.format("%.8f", transaction.getPrice()));
        holder.amount.setText(String.format("%.8f", transaction.getAmount()));

        holder.date.setText(DateFormatter.getStringFromDate(transaction.getDate(), DateFormatter.FORMAT_DD_MM_YYYY));


        holder.logo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.btc_icon));


    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
}

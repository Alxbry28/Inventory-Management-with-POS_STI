package com.example.inventorymanagementsystem.adapters;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.Product;
import com.example.inventorymanagementsystem.models.Sales;
import com.example.inventorymanagementsystem.models.Transaction;

import java.util.List;

public class TransactionRCVAdapter extends RecyclerView.Adapter<TransactionRCVAdapter.TransactionViewHolder> {

    private List<Transaction> transactionsList;
    private Context context;
    private Activity activity;
    public class TransactionViewHolder extends RecyclerView.ViewHolder{

        //initialize components
        TextView tvTransactionName, tvTransactionPrice, tvTransactionQuantity, tvCreatedAt;
        View view;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            // Component with layout
            tvTransactionName = itemView.findViewById(R.id.tvTransactionName);
            tvTransactionPrice = itemView.findViewById(R.id.tvTransactionPrice);
            tvTransactionQuantity = itemView.findViewById(R.id.tvTransactionQuantity);
        }
    }

    @NonNull
    @Override
    public TransactionRCVAdapter.TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerview_transaction_item, parent, false);
        return new TransactionRCVAdapter.TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionRCVAdapter.TransactionViewHolder holder, int position) {
        Transaction transaction = transactionsList.get(position);
        holder.tvTransactionName.setText(transaction.getCreatedAt());
        holder.tvTransactionPrice.setText("P" + String.valueOf(transaction.getTotal_price()));
        holder.tvTransactionQuantity.setText("Qty: " + String.valueOf(transaction.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

    public List<Transaction> getTransactionsList() {
        return transactionsList;
    }

    public void setTransactionsList(List<Transaction> transactionsList) {
        this.transactionsList = transactionsList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}

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

    private List<Sales> salesList;
    private Context context;
    private Activity activity;
    public class TransactionViewHolder extends RecyclerView.ViewHolder{

        //initialize components
        TextView tvTransReceiptNo, tvTransDate, tvTransQuantity, tvAmountPayable;
        View view;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            // Component with layout
            tvTransReceiptNo = itemView.findViewById(R.id.tvTransReceiptNo);
            tvTransDate = itemView.findViewById(R.id.tvTransDate);
            tvTransQuantity = itemView.findViewById(R.id.tvTransQuantity);
            tvAmountPayable = itemView.findViewById(R.id.tvAmountPayable);
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
        Sales transaction = salesList.get(position);
        holder.tvTransReceiptNo.setText(transaction.getReceiptNo());
        holder.tvTransDate.setText(transaction.getCreated_at());
        holder.tvAmountPayable.setText("P" + String.valueOf(transaction.getAmountPayable()));
        holder.tvTransQuantity.setText("Qty: " + String.valueOf(transaction.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return salesList.size();
    }

    public List<Sales> getSalesList() {
        return salesList;
    }

    public void setSalesList(List<Sales> salesList) {
        this.salesList = salesList;
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

package com.example.inventorymanagementsystem.adapters;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;
import android.widget.ImageButton;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.inventorymanagementsystem.models.Product;
import com.example.inventorymanagementsystem.R;
import java.util.List;

public class POSItemsDialogRCVAdapter extends RecyclerView.Adapter<POSItemsDialogRCVAdapter.POSItemsDialogViewHolder> {

    private List<Product> productsList;
    private Context context;
    private Activity activity;

    @NonNull
    @Override
    public POSItemsDialogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerview_pos_product_item_dialog, parent, false);

        return new POSItemsDialogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull POSItemsDialogViewHolder holder, int position) {
        Product product = productsList.get(position);
        holder.tvProductName.setText(product.getName());
        holder.tvProductPrice.setText("P" + String.valueOf(product.getPrice()));
        holder.tvProductQuantity.setText("Qty: " + String.valueOf(product.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class POSItemsDialogViewHolder extends RecyclerView.ViewHolder{

        TextView tvProductName, tvProductPrice, tvProductQuantity;
        ImageButton btnAddProduct;
        View view;

        public POSItemsDialogViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice= itemView.findViewById(R.id.tvProductPrice);
            tvProductQuantity = itemView.findViewById(R.id.tvProductQuantity);
            btnAddProduct = itemView.findViewById(R.id.btnAddProduct);

        }
    }

    public List<Product> getProductsList() {
        return productsList;
    }

    public void setProductsList(List<Product> productsList) {
        this.productsList = productsList;
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

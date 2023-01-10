package com.example.inventorymanagementsystem.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.enums.AppConstant;
import com.example.inventorymanagementsystem.interfaces.POSSelectedItemListener;
import com.example.inventorymanagementsystem.libraries.MoneyLibrary;
import com.example.inventorymanagementsystem.models.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class POSRCVAdapter extends RecyclerView.Adapter<POSRCVAdapter.POSViewHolder> {

    private ArrayList<Product> productList;
    private POSSelectedItemListener posSelectedItemListener;
    private Context context;
    private Activity activity;

    @NonNull
    @Override
    public POSViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerview_pos_product_item, parent, false);
        return new POSRCVAdapter.POSViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull POSViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvProductName.setText(product.getName());
        holder.tvCategory.setText(product.getCategory());
        String imageUrl = (product.getImageUrl() == null) ? AppConstant.IMAGE.Value : product.getImageUrl();

        Picasso.get().load(imageUrl).into(holder.ivProductImage);
        String qtyText = (product.getQuantity() > 0) ? "Qty: " + String.valueOf(product.getQuantity())
                : "Out of Stock";
        int qtyColor = (product.stockStatus() == 0) ? Color.RED : ((product.stockStatus() == 1) ? Color.parseColor("#FFCF9603") : Color.parseColor("#FF499306"));
        holder.tvQuantity.setTextColor(qtyColor);
        holder.tvQuantity.setText(qtyText);

        holder.tvPrice.setText("P" + MoneyLibrary.toTwoDecimalPlaces(product.getPrice()) + "_" + product.stockStatus());
        holder.cvProductItem.setOnClickListener(v -> {
            posSelectedItemListener.getSelectedItem(product);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class POSViewHolder extends RecyclerView.ViewHolder {

        CardView cvProductItem;
        TextView tvProductName, tvCategory, tvPrice, tvQuantity;
        View view;
        ImageView ivProductImage;

        public POSViewHolder(@NonNull View itemView) {
            super(itemView);
            // Component with layout id
            cvProductItem = itemView.findViewById(R.id.cvProductItem);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            view = itemView;
            ivProductImage = itemView.findViewById(R.id.ivProductImage);

        }
    }

    public ArrayList<Product> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }

    public POSSelectedItemListener getPosSelectedItemListener() {
        return posSelectedItemListener;
    }

    public void setPosSelectedItemListener(POSSelectedItemListener posSelectedItemListener) {
        this.posSelectedItemListener = posSelectedItemListener;
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

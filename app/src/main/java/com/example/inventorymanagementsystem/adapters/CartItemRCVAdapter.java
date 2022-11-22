package com.example.inventorymanagementsystem.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.models.CartItem;
import com.example.inventorymanagementsystem.models.Product;

import java.util.List;

public class CartItemRCVAdapter extends RecyclerView.Adapter<CartItemRCVAdapter.CartItemViewHolder>{

    private List<CartItem> cartItemList;
    private Context context;
    private Activity activity;

    public class CartItemViewHolder extends RecyclerView.ViewHolder{

        CardView cvProductItem;
        TextView tvProductName, tvCategory, tvPrice, tvQuantity;
        View view;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            // Component with layout id
            cvProductItem = itemView.findViewById(R.id.cvProductItem);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            view = itemView;
        }
    }

    @NonNull
    @Override
    public CartItemRCVAdapter.CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerview_pos_product_item, parent, false);
        return new CartItemRCVAdapter.CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemRCVAdapter.CartItemViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        holder.tvProductName.setText(cartItem.getName());
        holder.tvCategory.setText(cartItem.getCategory());
        holder.tvQuantity.setText("Qty: " + String.valueOf(cartItem.getQuantity()));
        holder.tvPrice.setText("P" + String.valueOf(cartItem.GetComputedTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
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

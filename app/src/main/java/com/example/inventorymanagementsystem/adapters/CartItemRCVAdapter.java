package com.example.inventorymanagementsystem.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.database.SQLiteDB;
import com.example.inventorymanagementsystem.interfaces.CartItemsListener;
import com.example.inventorymanagementsystem.interfaces.ProductModelListener;
import com.example.inventorymanagementsystem.libraries.MoneyLibrary;
import com.example.inventorymanagementsystem.models.CartItem;
import com.example.inventorymanagementsystem.models.Product;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CartItemRCVAdapter extends RecyclerView.Adapter<CartItemRCVAdapter.CartItemViewHolder>{

    private ArrayList<CartItem> cartItemList;
    private Context context;
    private Activity activity;
    private CartItemsListener cartItemsListener;
//    private Product tempProduct;
    public class CartItemViewHolder extends RecyclerView.ViewHolder{

        ImageButton iBtnDeleteItem;
        Button btnMinusQty, btnPlusQty;
        EditText etQuantity;
        TextView tvProductName, tvCategory, tvPrice, tvProductPrice;
        View view;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            // Component with layout id
            iBtnDeleteItem = itemView.findViewById(R.id.iBtnDeleteItem);
            etQuantity = itemView.findViewById(R.id.etQuantity);
            btnMinusQty = itemView.findViewById(R.id.btnMinusQty);
            btnPlusQty = itemView.findViewById(R.id.btnPlusQty);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            view = itemView;
        }
    }

    @NonNull
    @Override
    public CartItemRCVAdapter.CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerview_cart_item, parent, false);
        return new CartItemRCVAdapter.CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemRCVAdapter.CartItemViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        cartItem.setSqLiteDB(new SQLiteDB(context));
        holder.tvProductName.setText(cartItem.getName());
        holder.tvCategory.setText(cartItem.getCategory());
        holder.tvPrice.setText("P" + MoneyLibrary.toTwoDecimalPlaces(cartItem.GetComputedTotalPrice()));
        holder.tvProductPrice.setText("P" + MoneyLibrary.toTwoDecimalPlaces(cartItem.getPrice()));
        holder.etQuantity.setText(String.valueOf(cartItem.getQuantity()));
        holder.etQuantity.setFocusable(false);

        holder.iBtnDeleteItem.setOnClickListener(v -> {
            cartItem.DeleteById();
            cartItemList.remove(cartItem);
            notifyItemRangeChanged(position,1);
            notifyDataSetChanged();
            cartItemsListener.retrieveCartItemList(cartItemList);
            Toast.makeText(context, "Delete Item", Toast.LENGTH_SHORT).show();
        });
        holder.btnMinusQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cartItem.getQuantity() == 1){
                    return;
                }

                cartItem.setQuantity(cartItem.getQuantity() - 1);
                holder.etQuantity.setText(String.valueOf(cartItem.getQuantity()));
                cartItemList.set(cartItemList.indexOf(cartItem), cartItem);
                holder.tvPrice.setText("P" + MoneyLibrary.toTwoDecimalPlaces(cartItem.GetComputedTotalPrice()));
                cartItemsListener.retrieveCartItemList(cartItemList);

            }
        });

        holder.btnPlusQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = new Product();
                product.setId(cartItem.getProductId());
                product.GetById(new ProductModelListener() {
                    @Override
                    public void retrieveProduct(Product product1) {
                        product.setQuantity(product1.getQuantity());

                        if(cartItem.getQuantity() ==  product.getQuantity()){
                            Toast.makeText(context, "Cannot exceed item", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        cartItem.setQuantity(cartItem.getQuantity() + 1);
                        holder.etQuantity.setText(String.valueOf(cartItem.getQuantity()));
                        cartItemList.set(cartItemList.indexOf(cartItem), cartItem);
                        holder.tvPrice.setText("P" + MoneyLibrary.toTwoDecimalPlaces(cartItem.GetComputedTotalPrice()));
                        cartItemsListener.retrieveCartItemList(cartItemList);

                    }

                    @Override
                    public void getProductList(ArrayList<Product> productArrayList) {

                    }

                });

            }
        });

    }

    public void setOnQuantityChange(CartItemsListener cartItemsListener){

        this.cartItemsListener = cartItemsListener;

    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public ArrayList<CartItem> getCartItemList() {
        return cartItemList;
    }

    public void setCartItemList(ArrayList<CartItem> cartItemList) {
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

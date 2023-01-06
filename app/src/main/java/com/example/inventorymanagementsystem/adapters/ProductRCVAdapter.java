package com.example.inventorymanagementsystem.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventorymanagementsystem.views.staff.*;
import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.libraries.MoneyLibrary;
import com.example.inventorymanagementsystem.models.Product;

import java.util.ArrayList;

public class ProductRCVAdapter extends RecyclerView.Adapter<ProductRCVAdapter.ProductsViewHolder>  {

    private ArrayList<Product> productList;
    private Context context;
    private Activity activity;

    public class ProductsViewHolder extends RecyclerView.ViewHolder{

        TextView tvProductName, tvCategory, tvPrice, tvQuantity;
        ImageButton btnEditProduct, btnDeleteProduct;
        View view;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            // Component with layout id
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnDeleteProduct = itemView.findViewById(R.id.btnDeleteProduct);
            btnEditProduct = itemView.findViewById(R.id.btnEditProduct);
        }

    }

    @NonNull
    @Override
    public ProductRCVAdapter.ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerview_product_item, parent, false);
        return new ProductRCVAdapter.ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvProductName.setText(product.getName());
        holder.tvCategory.setText("Category: " + product.getCategory());

        String qtyText = (product.getQuantity() > 0) ? "Qty: " + String.valueOf(product.getQuantity()) : "Out of Stock";
        int qtyColor = (product.getQuantity() <= 0) ? Color.RED : Color.GRAY;
        holder.tvQuantity.setTextColor(qtyColor);
        holder.tvQuantity.setText(qtyText);

        holder.tvPrice.setText("P" + MoneyLibrary.toTwoDecimalPlaces(product.getPrice()));
        holder.btnEditProduct.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddItemForm.class);
            intent.putExtra("isEditProduct", true);
            intent.putExtra("productId", product.getId());
            context.startActivity(intent);

            //Toast.makeText(context, "Test Edit Button " + product.getName() , Toast.LENGTH_SHORT).show();
        });
        holder.btnDeleteProduct.setOnClickListener(v -> {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            if(product != null){
                                product.Delete(status -> {
                                    if(status){
                                        notifyDataSetChanged();
                                        Toast.makeText(context, "Product Deleted. " , Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Are you sure you want to delete product?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("Cancel", dialogClickListener).show();
        });
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public ArrayList<Product> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
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

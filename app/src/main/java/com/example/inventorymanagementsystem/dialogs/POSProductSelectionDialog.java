package com.example.inventorymanagementsystem.dialogs;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.example.inventorymanagementsystem.R;
import com.example.inventorymanagementsystem.adapters.POSItemsDialogRCVAdapter;
import com.example.inventorymanagementsystem.models.Product;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Toast;

import java.util.ArrayList;

public class POSProductSelectionDialog extends AppCompatDialogFragment {

    private PointOfSaleProductDialogListener listener;
    private Context context;
    private ArrayList<Product> productArrayList;
    private RecyclerView rcPOSItemsDialog;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (PointOfSaleProductDialogListener) context;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_pos_layout_product_items,null);
        rcPOSItemsDialog = view.findViewById(R.id.rcPOSItemsDialog);

        productArrayList = new ArrayList<>();
        Product product1 = new Product();
        product1.setName("Buko");
        product1.setPrice(25);
        product1.setQuantity(20);
        product1.setId("1");

        Product product2 = new Product();
        product2.setName("Magic Sarap");
        product2.setPrice(200);
        product2.setQuantity(30);
        product2.setId("3");

        Product product3 = new Product();
        product3.setName("Potatos");
        product3.setPrice(250);
        product3.setQuantity(19);
        product3.setId("2");

        Product product4 = new Product();
        product4.setName("Carrots");
        product4.setPrice(100);
        product4.setQuantity(10);
        product4.setId("4");

        Product product5 = new Product();
        product5.setName("KangKong");
        product5.setPrice(12.5);
        product5.setQuantity(10);
        product5.setId("5");

        productArrayList.add(product1);
        productArrayList.add(product2);
        productArrayList.add(product3);
        productArrayList.add(product4);
        productArrayList.add(product5);

        POSItemsDialogRCVAdapter posItemsDialogRCVAdapter = new POSItemsDialogRCVAdapter();
        posItemsDialogRCVAdapter.setProductsList(productArrayList);
        rcPOSItemsDialog.setAdapter(posItemsDialogRCVAdapter);
        RecyclerView.LayoutManager rcvLayoutManager = new LinearLayoutManager(context);
        rcPOSItemsDialog.setLayoutManager(rcvLayoutManager);
        rcPOSItemsDialog.setItemAnimator(new DefaultItemAnimator());

        builder.setView(view).setTitle("Choose Products").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
            }
        });

        return builder.create();

    }

    public ArrayList<Product> getProductArrayList() {
        return productArrayList;
    }

    public void setProductArrayList(ArrayList<Product> productArrayList) {
        this.productArrayList = productArrayList;
    }

    //Event Listener
    public interface PointOfSaleProductDialogListener{
        void addProduct(String productId);
    }

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

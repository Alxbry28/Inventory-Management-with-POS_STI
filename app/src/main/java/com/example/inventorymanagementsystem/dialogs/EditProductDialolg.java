package com.example.inventorymanagementsystem.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.inventorymanagementsystem.R;

public class EditProductDialolg extends AppCompatDialogFragment {

    private Context context;
    private EditPProductDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (EditPProductDialogListener) context;
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

        return builder.create();
    }

    //Event Listener
    public interface EditPProductDialogListener{
        void addProduct(String productId);
    }

}

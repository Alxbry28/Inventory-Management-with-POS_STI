package com.example.inventorymanagementsystem.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.example.inventorymanagementsystem.R;

import java.util.Arrays;

public class PositionChoiceDialog extends AppCompatDialogFragment {

    private Context context;
    private String chosenPosition;
    private String[] typeOfPosition;
    private PositionChoiceListener listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        typeOfPosition = getResources().getStringArray(R.array.array_position_admin);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle("Choose Rolce");
        int index = Arrays.asList(typeOfPosition).indexOf(chosenPosition);
        int selectedIndex = (index >= 0) ? index : -1;
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.setPositionChoice(chosenPosition);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", null);
        dialogBuilder.setSingleChoiceItems(typeOfPosition, selectedIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                chosenPosition = typeOfPosition[i];
            }
        });

        return dialogBuilder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (PositionChoiceListener) context;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getPositionChoice(PositionChoiceListener listener){
        this.listener = listener;
    }

    public interface PositionChoiceListener{
        void setPositionChoice(String choice);
    }

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getChosenPosition() {
        return chosenPosition;
    }

    public void setChosenPosition(String chosenPosition) {
        this.chosenPosition = chosenPosition;
    }

    public String[] getTypeOfPosition() {
        return typeOfPosition;
    }

    public void setTypeOfPosition(String[] typeOfPosition) {
        this.typeOfPosition = typeOfPosition;
    }
}

package com.example.inventorymanagementsystem.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.inventorymanagementsystem.interfaces.IDurationChoiceDialogListener;
import com.example.inventorymanagementsystem.R;
import java.util.Arrays;

public class DurationChoiceDialog extends AppCompatDialogFragment {

    private Context context;
    private String chosenDuration;
    private String[] durationChoices;
    private IDurationChoiceDialogListener iDurationChoiceDialogListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        durationChoices = getResources().getStringArray(R.array.duration_choice);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Select Duration");
        int index = Arrays.asList(durationChoices).indexOf(chosenDuration);
        int selectedIndex = (index >= 0) ? index : -1;
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                iDurationChoiceDialogListener.setChosenDuration(chosenDuration);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.setSingleChoiceItems(durationChoices, selectedIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                chosenDuration = durationChoices[i];
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            iDurationChoiceDialogListener = (IDurationChoiceDialogListener) context;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getChosenDuration() {
        return chosenDuration;
    }

    public void setChosenDuration(String chosenDuration) {
        this.chosenDuration = chosenDuration;
    }

    public String[] getDurationChoices() {
        return durationChoices;
    }

    public void setDurationChoices(String[] durationChoices) {
        this.durationChoices = durationChoices;
    }

    public IDurationChoiceDialogListener getiDurationChoiceDialogListener() {
        return iDurationChoiceDialogListener;
    }

    public void setiDurationChoiceDialogListener(IDurationChoiceDialogListener iDurationChoiceDialogListener) {
        this.iDurationChoiceDialogListener = iDurationChoiceDialogListener;
    }

}

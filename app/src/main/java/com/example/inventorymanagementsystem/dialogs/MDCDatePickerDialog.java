package com.example.inventorymanagementsystem.dialogs;

import androidx.core.util.Pair;

import com.google.android.material.datepicker.MaterialDatePicker;

public class MDCDatePickerDialog {

    public static MaterialDatePicker openDatePicker(){
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select a Date");
        MaterialDatePicker materialDatePicker = builder.build();
        return materialDatePicker;
    }

    public static MaterialDatePicker openDateRangePicker(){
        MaterialDatePicker.Builder<Pair<Long,Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select a Date");
        MaterialDatePicker materialDatePicker = builder.build();
        return materialDatePicker;
    }

}

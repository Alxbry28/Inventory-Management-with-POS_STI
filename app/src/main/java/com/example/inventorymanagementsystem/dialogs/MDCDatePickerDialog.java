package com.example.inventorymanagementsystem.dialogs;

import androidx.core.util.Pair;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

public class MDCDatePickerDialog {

    public static MaterialDatePicker openDatePicker(){
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select a Date");
        MaterialDatePicker materialDatePicker = builder.build();
        return materialDatePicker;
    }

    public static MaterialDatePicker startDatePicker(){
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select Start Date");
        MaterialDatePicker materialDatePicker = builder.build();
        return materialDatePicker;
    }

    public static MaterialDatePicker endDatePicker(){
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select End Date");

        CalendarConstraints.DateValidator dateValidatorMin = DateValidatorPointForward.from(min.getTimeInMillis());
        CalendarConstraints.DateValidator dateValidatorMax = DateValidatorPointBackward.before(max.getTimeInMillis());

        ArrayList<CalendarConstraints.DateValidator> listValidators =
                new ArrayList<CalendarConstraints.DateValidator>();
        listValidators.add(dateValidatorMin);
        listValidators.add(dateValidatorMax);

        CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listValidators);
        constraintsBuilderRange.setValidator(validators);

        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
        builder.setCalendarConstraints(constraintsBuilderRange.build());


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

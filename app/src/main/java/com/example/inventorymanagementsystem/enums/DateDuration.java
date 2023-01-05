package com.example.inventorymanagementsystem.enums;

public enum DateDuration {
    TODAY( "Today"),
    LAST_DAY("Last Day"),
    THIS_WEEK( "This Week"),
    THIS_MONTH( "This Month"),
    LAST_MONTH( "Last Month"),
    ALL("All"),
    SINGLEDAY("Single Day"),
    DATE_RANGE("Date Range");

    String Duration;
    DateDuration(String duration) {
        Duration = duration;
    }

    public String getDuration() {
        return this.Duration;
    }

}

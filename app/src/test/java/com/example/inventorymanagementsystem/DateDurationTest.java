package com.example.inventorymanagementsystem;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.inventorymanagementsystem.libraries.Validation;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Locale;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class DateDurationTest {

    @Test
    public void validationTest() {

        String password = "qwerqwer123";
        String confirmPassword = "qwer123qwer";
        boolean isMatch = Validation.checkPasswordMatch(password, confirmPassword);
        assertFalse(isMatch);

    }

    @Test
    public void dateDurationTodayTest() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        String formattedDate = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
                .format(currentDateTime);
        assertEquals("2022-12-09", formattedDate);
    }

    @Test
    public void dateDurationLastDayTest() {
        LocalDate currentDateTime = LocalDate.now();
        LocalDate lastDay = currentDateTime.minusDays(1);
        String formattedDate = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
                .format(lastDay);
        assertEquals("2022-12-08", formattedDate);
    }

    @Test
    public void dateDurationLastWeekTest1() {
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime lastWeek = currentDate.minusWeeks(1);
        String formattedDate = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
                .format(lastWeek);
        assertEquals("2022-12-02", formattedDate);
    }

    @Test
    public void dateDurationLastWeekTest2() {
        String startDate, endDate, startDateShort, endDateShort;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        DateTimeFormatter dateTimeDateShort = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);

        LocalDate date = LocalDate.parse("2022-12-09");
        LocalDate firstDayWeek = date.minusWeeks(1).with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        LocalDate lastWeek = firstDayWeek.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));

        startDate = dateTimeFormatter.format(firstDayWeek);
        endDate = dateTimeFormatter.format(lastWeek);

        startDateShort = dateTimeDateShort.format(firstDayWeek);
        endDateShort = dateTimeDateShort.format(lastWeek);

        assertArrayEquals(new String[]{"2022-11-28", "2022-12-04", "28 Nov 2022", "04 Dec 2022"}, new String[]{startDate, endDate, startDateShort, endDateShort});
    }

    @Test
    public void dateDurationLastMonthTest1() {
        LocalDate date = LocalDate.parse("2022-12-09");
        LocalDate lastMonth = date.minusMonths(1);
        LocalDate firstDaylastMonth = lastMonth.with(TemporalAdjusters.firstDayOfMonth());
        String formattedDate = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
                .format(lastMonth);
        String formattedDateShort = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)
                .format(lastMonth);

//        assertEquals("2022-11-09", formattedDate);
        assertEquals("09 Nov 2022", formattedDateShort);
    }

    @Test
    public void dateDurationLastMonthTest2() {
        String startDate, endDate, startDateShort, endDateShort;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        DateTimeFormatter dateTimeDateShort = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);

        LocalDate date = LocalDate.parse("2022-12-09").with(TemporalAdjusters.lastDayOfMonth());
        LocalDate lastMonth = date.minusMonths(1);
        LocalDate firstDaylastMonth = lastMonth.with(TemporalAdjusters.firstDayOfMonth());

        startDate = dateTimeFormatter.format(firstDaylastMonth);
        endDate = dateTimeFormatter.format(lastMonth);

        startDateShort = dateTimeDateShort.format(firstDaylastMonth);
        endDateShort = dateTimeDateShort.format(lastMonth);

        assertArrayEquals(new String[]{"2022-11-01", "2022-11-30", "01 Nov 2022", "30 Nov 2022"}, new String[]{startDate, endDate, startDateShort, endDateShort});

    }

    @Test
    public void dateDurationThisMonthTest1() {
        String startDate, endDate, startDateShort, endDateShort;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        DateTimeFormatter dateTimeDateShort = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);

        LocalDate currentDate = LocalDate.now();
        LocalDate firstDaylastMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth());

        startDate = dateTimeFormatter.format(firstDaylastMonth);
        endDate = dateTimeFormatter.format(currentDate);

        startDateShort = dateTimeDateShort.format(firstDaylastMonth);
        endDateShort = dateTimeDateShort.format(currentDate);

        assertArrayEquals(new String[]{"2022-12-01", "2022-12-09", "01 Dec 2022", "09 Dec 2022"}, new String[]{startDate, endDate, startDateShort, endDateShort});
    }

    @Test
    public void dateDurationThisYearTest1() {
        String startDate, endDate, startDateShort, endDateShort;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        DateTimeFormatter dateTimeDateShort = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);

        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfYear = currentDate.with(TemporalAdjusters.firstDayOfYear());

        startDate = dateTimeFormatter.format(firstDayOfYear);
        endDate = dateTimeFormatter.format(currentDate);

        startDateShort = dateTimeDateShort.format(firstDayOfYear);
        endDateShort = dateTimeDateShort.format(currentDate);

        assertArrayEquals(new String[]{"2022-01-01", "2022-12-10", "01 Jan 2022", "10 Dec 2022"}, new String[]{startDate, endDate, startDateShort, endDateShort});

    }

}

package com.example.mareu.model;

public class Date {
    private int day, month, year;

    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public Date (){}

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String dateToString() {
        String day = String.valueOf(getDay());
        String month = String.valueOf(getMonth());
        String year = String.valueOf(getYear());

        String date = day + "." + month + "." + year;

        return date;
    }

    public boolean compareDate(Date date1, Date date2) {
        if (date2.year < date1.year)
            return false;
        else if (date2.month < date1.month)
            return false;
        else if (date2.day < date1.day)
            return false;
        return true;
    }
}

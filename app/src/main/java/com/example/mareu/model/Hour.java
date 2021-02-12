package com.example.mareu.model;

public class Hour {
    private int hour, minute;

    public Hour(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String hourToString() {
        String fHour = String.valueOf(getHour());
        String fMinute = String.valueOf(getMinute());
        if (this.hour < 10)
            fHour = "0" + fHour;
        if (this.minute < 10)
            fMinute = "0" + fMinute;
        String completeHour = fHour + ":" + fMinute;
        return completeHour;
    }
}

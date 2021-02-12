package com.example.mareu.model;

import java.util.ArrayList;
import java.util.List;

public class Meeting {

    private Hour hour;
    private Date meetingDate, userAsked;
    private String location, description;
    private List<String> listMail;
    private User user;

    public Meeting(Hour hour, Date meetingDate, Date userAsked, String location, List<String> listMail, User user, String description) {
        this.hour = hour;
        this.meetingDate = meetingDate;
        this.userAsked = userAsked;
        this.location = location;
        this.listMail = listMail;
        this.user = user;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Hour getHour() {
        return hour;
    }

    public void setHour(Hour hour) {
        this.hour = hour;
    }

    public Date getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Date meetingDate) {
        this.meetingDate = meetingDate;
    }

    public Date getUserAsked() {
        return userAsked;
    }

    public void setUserAsked(Date userAsked) {
        this.userAsked = userAsked;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getListMail() {
        return listMail;
    }

    public void setListMail(List<String> listMail) {
        this.listMail = listMail;
    }
}

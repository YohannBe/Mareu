package com.example.mareu.service;

import com.example.mareu.model.Date;
import com.example.mareu.model.Meeting;

import java.util.ArrayList;
import java.util.List;

public interface ListMeetingApiService {

    void addMeeting(Meeting meeting);
    void deleteMeeting(Meeting meeting);
    ArrayList<Meeting> getMeetingList();
    int getSizeMeetingList();
    void addMail(String mail);
    void deleteMail(String mail);
    ArrayList<String> getListMail();
    void clearListMail();

    List<Meeting> getFilteredMeetingList(String specific, Date date);

    List<Meeting> getFilteredMeetingListRange(Date date1, Date date2);
}

package com.example.mareu.service;

import com.example.mareu.model.Date;
import com.example.mareu.model.Meeting;
import com.example.mareu.utils.Utils;

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

    List<Meeting> getFilteredMeetingList(Utils.EnumDate enumDate, Date date);

    List<Meeting> getFilteredMeetingListRange(Date date1, Date date2);

    List<Meeting> getFilteredMeetingListLocation(ArrayList<Utils.EnumRoom> boxes);

    List<Meeting> getFilteredBothMeetingList(List<Meeting> filteredList1, List<Meeting> filteredList2);

    ArrayList<Meeting> getMeetingListFiltered2();
}

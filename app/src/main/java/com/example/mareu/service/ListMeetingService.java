package com.example.mareu.service;


import android.util.Log;

import com.example.mareu.model.Date;
import com.example.mareu.model.Meeting;
import com.example.mareu.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.example.mareu.service.DummyMeetingGenerator.generateList;
import static com.example.mareu.service.DummyMeetingGenerator.generateListMail;
import static com.example.mareu.utils.Utils.EnumDate.specific;

public class ListMeetingService implements ListMeetingApiService {
    private ArrayList<Meeting> meetingList = (ArrayList<Meeting>) generateList();
    public static ArrayList<String> listMail = (ArrayList<String>) generateListMail();
    private ArrayList<Meeting> meetingListFiltered = new ArrayList<>();


    public void addMeeting(Meeting meeting) {
        meetingList.add(meeting);
    }

    public void deleteMeeting(Meeting meeting) {
        meetingList.remove(meeting);
    }

    public ArrayList<Meeting> getMeetingList() {
        return meetingList;
    }

    public Meeting getMeeting(int index) {
        return meetingList.get(index);
    }

    public int getSizeMeetingList() {
        return meetingList.size();
    }

    public void addMail(String mail) {
        listMail.add(mail);
    }

    public void deleteMail(String mail) {
        listMail.remove(mail);
    }

    public ArrayList<String> getListMail() {
        return listMail;
    }

    public void clearListMail() {
        listMail.clear();
    }

    @Override
    public List<Meeting> getFilteredMeetingList(Utils.EnumDate range, Date date) {
        meetingListFiltered.clear();

        for (int i = 0; i < meetingList.size(); i++) {
            switch (range) {
                case specific:
                    if (Utils.getEqual(meetingList.get(i).getMeetingDate(), date))
                        meetingListFiltered.add(meetingList.get(i));
                    break;
                case before:
                    if (Utils.getBefore(date, meetingList.get(i).getMeetingDate()))
                        meetingListFiltered.add(meetingList.get(i));
                    break;
                case after:
                    if (Utils.getAfter(date, meetingList.get(i).getMeetingDate()))
                        meetingListFiltered.add(meetingList.get(i));
                    break;
                default:
                    break;
            }
        }
        return meetingListFiltered;
    }

    @Override
    public List<Meeting> getFilteredMeetingListRange(Date date1, Date date2) {
        meetingListFiltered.clear();

        for (int i = 0; i < meetingList.size(); i++) {
            if (Utils.getAfter(date1, meetingList.get(i).getMeetingDate()) && Utils.getBefore(date2, meetingList.get(i).getMeetingDate()))
                meetingListFiltered.add(meetingList.get(i));
        }

        return meetingListFiltered;
    }

    @Override
    public List<Meeting> getFilteredMeetingListLocation(ArrayList<Utils.EnumRoom> boxes) {

        meetingListFiltered.clear();
        for (int i = 0; i < meetingList.size(); i++) {
            for (int j = 0; j < boxes.size(); j++) {
                if (meetingList.get(i).getLocation().equals(String.valueOf(boxes.get(j).toString())))
                    meetingListFiltered.add(meetingList.get(i));
            }
        }
        return meetingListFiltered;
    }

    @Override
    public List<Meeting> getFilteredBothMeetingList(List<Meeting> filteredList1, List<Meeting> filteredList2) {
        meetingListFiltered.clear();
        for (int i = 0; i < filteredList1.size(); i++) {
            if (filteredList2.contains(filteredList1.get(i)))
                meetingListFiltered.add(filteredList1.get(i));
        }
        return meetingListFiltered;
    }
}

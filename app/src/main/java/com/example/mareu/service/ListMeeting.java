package com.example.mareu.service;


import com.example.mareu.model.Date;
import com.example.mareu.model.Meeting;

import java.util.ArrayList;
import java.util.List;

import static com.example.mareu.service.DummyMeetingGenerator.generateList;
import static com.example.mareu.service.DummyMeetingGenerator.generateListMail;

public class ListMeeting implements ListMeetingApiService {
    private ArrayList<Meeting> meetingList = (ArrayList<Meeting>) generateList();
    public static ArrayList<String> listMail = (ArrayList<String>) generateListMail();
    private ArrayList<Meeting> meetingListFilteredRadio;
    private ArrayList<Meeting> meetingListFilteredRange;
    private Date serviceDate = new Date();


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
    public List<Meeting> getFilteredMeetingList(String range, Date date) {
        meetingListFilteredRadio = new ArrayList<>();
        for (int i = 0; i < meetingList.size(); i++) {
            switch (range) {
                case "specific":
                    if (meetingList.get(i).getMeetingDate() == date)
                        meetingListFilteredRadio.add(meetingList.get(i));
                    break;
                case "before":
                    if (serviceDate.compareDate(meetingList.get(i).getMeetingDate(), date))
                        meetingListFilteredRadio.add(meetingList.get(i));
                    break;
                case "after":
                    if (serviceDate.compareDate(date, meetingList.get(i).getMeetingDate()))
                        meetingListFilteredRadio.add(meetingList.get(i));
                    break;
                default:
                    break;
            }
        }
        return meetingListFilteredRadio;
    }

    @Override
    public List<Meeting> getFilteredMeetingListRange(Date date1, Date date2) {
        meetingListFilteredRange = new ArrayList<>();

        for (int i = 0; i < meetingList.size(); i++) {
            if (serviceDate.compareDate(meetingList.get(i).getMeetingDate(), date1) && serviceDate.compareDate(date2,meetingList.get(i).getMeetingDate()))
                meetingListFilteredRange.add(meetingList.get(i));
        }

        return meetingListFilteredRange;
    }
}

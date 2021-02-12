package com.example.mareu.injection;

import com.example.mareu.service.ListMeeting;

public class Injection {

    static ListMeeting service = new ListMeeting();

    public static ListMeeting getListMeetingService() {
        return service;
    }


    public static ListMeeting getNewInstanceApiService() {
        return new ListMeeting();
    }
}

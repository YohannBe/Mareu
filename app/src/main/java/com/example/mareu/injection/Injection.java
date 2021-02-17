package com.example.mareu.injection;

import com.example.mareu.service.ListMeetingService;

public class Injection {

    static ListMeetingService service = new ListMeetingService();

    public static ListMeetingService getListMeetingService() {
        return service;
    }


    public static ListMeetingService getNewInstanceApiService() {
        return new ListMeetingService();
    }
}

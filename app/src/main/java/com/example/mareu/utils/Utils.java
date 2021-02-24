package com.example.mareu.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.mareu.AddMeeting;
import com.example.mareu.model.Date;
import com.example.mareu.model.Hour;
import com.example.mareu.model.Meeting;
import com.example.mareu.service.ListMeetingApiService;

import java.util.ArrayList;

public class Utils {

    /**
     * ------------------date-------------------
     */
    public static boolean compareDate(Date date1, Date date2) {
        if (date1.getYear() < date2.getYear()) {
            return true;
        } else {
            if (date1.getYear() == date2.getYear()) {
                if (date2.getMonth() < date1.getMonth()) {
                    return true;
                } else {
                    if (date1.getMonth() == date2.getMonth()) {
                        if (date1.getDay() <= date2.getDay())
                            return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean getBefore(Date date1, Date date2) {
        if (date2.getYear() < date1.getYear()) {
            return true;
        } else {
            if (date2.getYear() == date1.getYear()) {
                if (date2.getMonth() < date1.getMonth()) {
                    return true;
                } else {
                    if (date2.getMonth() == date1.getMonth()) {
                        if (date2.getDay() <= date1.getDay())
                            return true;
                    }
                }
            }
        }
        return false;
    }


    public static boolean getAfter(Date date1, Date date2) {

        if (date2.getYear() > date1.getYear()) {
            return true;
        } else {
            if (date2.getYear() == date1.getYear()) {
                if (date2.getMonth() > date1.getMonth()) {
                    return true;
                } else {
                    if (date2.getMonth() == date1.getMonth()) {
                        if (date2.getDay() >= date1.getDay())
                            return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean getEqual(Date date1, Date date2) {
        if (date1.getYear() == date2.getYear() && date1.getMonth() == date2.getMonth() && date1.getDay() == date2.getDay()) {
            return true;
        }

        return false;
    }

    public static String dateToString(Date date) {
        String day = String.valueOf(date.getDay());
        String month = String.valueOf(date.getMonth());
        String year = String.valueOf(date.getYear());

        String dateString = day + "." + month + "." + year;

        return dateString;
    }

    /**
     * ----------------- hour ------------------
     */

    public static String hourToString(Hour hour) {
        String fHour = String.valueOf(hour.getHour());
        String fMinute = String.valueOf(hour.getMinute());
        if (hour.getHour() < 10)
            fHour = "0" + fHour;
        if (hour.getMinute() < 10)
            fMinute = "0" + fMinute;
        String completeHour = fHour + ":" + fMinute;
        return completeHour;
    }

    private static boolean hourAfter(Hour hour1, Hour hour2) {

        if (hour2.getHour() > hour1.getHour())
            return true;
        else if (hour2.getHour() == hour1.getHour())
            if (hour2.getMinute() >= hour1.getMinute())
                return true;
        return false;
    }

    private static boolean hourBefore(Hour hour1, Hour hour2) {
        if (hour2.getHour() < hour1.getHour())
            return true;
        else if (hour2.getHour() == hour1.getHour())
            if (hour2.getMinute() <= hour1.getMinute())
                return true;
        return false;
    }

    /**
     * ----------------- filter -----------------
     */

    public static enum EnumRoom {
        A, B, C, D, E, F, G, H, I, J
    }

    public static enum EnumDate {
        before, after, specific
    }

    public static ArrayList<EnumRoom> chosenRooms = new ArrayList<>();

    public static ArrayList<EnumRoom> checkDialogBoxes(CheckBox a, CheckBox b, CheckBox c, CheckBox d, CheckBox e, CheckBox f, CheckBox g, CheckBox h, CheckBox i, CheckBox j) {
        chosenRooms.clear();
        if (a.isChecked())
            chosenRooms.add(Utils.EnumRoom.A);
        if (b.isChecked())
            chosenRooms.add(Utils.EnumRoom.B);
        if (c.isChecked())
            chosenRooms.add(Utils.EnumRoom.C);
        if (d.isChecked())
            chosenRooms.add(Utils.EnumRoom.D);
        if (e.isChecked())
            chosenRooms.add(Utils.EnumRoom.E);
        if (f.isChecked())
            chosenRooms.add(Utils.EnumRoom.F);
        if (g.isChecked())
            chosenRooms.add(Utils.EnumRoom.G);
        if (h.isChecked())
            chosenRooms.add(Utils.EnumRoom.H);
        if (i.isChecked())
            chosenRooms.add(Utils.EnumRoom.I);
        if (j.isChecked())
            chosenRooms.add(Utils.EnumRoom.J);

        return chosenRooms;
    }

    /** ----------- meeting dispo ---------------*/

    public static boolean checkMeetingDisponibility(ListMeetingApiService service, String location, Date dateObject, Hour hourObject, String hourDuration, String minuteDuration, Context applicationContext) {

        Hour startingHour;
        Hour finishedHour;
        Hour finishedHourObject;
        Hour durationObject = new Hour(Integer.parseInt(hourDuration), Integer.parseInt(minuteDuration));
        Meeting meeting;

        for (int i = 0; i < service.getSizeMeetingList(); i++) {
            meeting = service.getMeetingList().get(i);
            if (TextUtils.equals(meeting.getLocation(), location) && Utils.getEqual(meeting.getMeetingDate(), dateObject)) {
                startingHour = meeting.getHour();

                if ((startingHour.getMinute() + meeting.getDuration().getMinute()) >= 60)
                    finishedHour = new Hour(startingHour.getHour() + meeting.getDuration().getHour() + 1, startingHour.getMinute() + meeting.getDuration().getMinute() - 60);
                else
                    finishedHour = new Hour(startingHour.getHour() +  meeting.getDuration().getHour(), startingHour.getMinute() + meeting.getDuration().getMinute());


                if (hourObject.getMinute() + durationObject.getMinute() >= 60)
                    finishedHourObject = new Hour(hourObject.getHour() + durationObject.getHour() + 1, hourObject.getMinute() + durationObject.getMinute() - 60);
                else
                    finishedHourObject = new Hour(hourObject.getHour() + durationObject.getHour(), hourObject.getMinute() + durationObject.getMinute());

                if (Utils.hourAfter(startingHour, hourObject) && Utils.hourBefore(finishedHour, hourObject)) {
                    Toast.makeText(applicationContext, "There is already a meeting scheduled here, the start of your meeting is on another one", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (Utils.hourBefore(startingHour, hourObject) && Utils.hourAfter(startingHour, finishedHourObject)) {
                    Toast.makeText(applicationContext, "There is already a meeting scheduled here, the end of your meeting will be on the start of the other", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }
}

package com.example.mareu.utils;

import android.view.View;
import android.widget.CheckBox;

import com.example.mareu.model.Date;

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
}

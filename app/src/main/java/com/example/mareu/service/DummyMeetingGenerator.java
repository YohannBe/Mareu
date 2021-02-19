package com.example.mareu.service;

import com.example.mareu.R;
import com.example.mareu.model.Date;
import com.example.mareu.model.Hour;
import com.example.mareu.model.Meeting;
import com.example.mareu.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class DummyMeetingGenerator {


    public static List<String> DUMMY_MAIL = Arrays.asList(new String("jeanmoulin@gmail.com"),
            new String("louisde@gmail.com"));

    public static List<String> generateListMail() {
        return new ArrayList<>(DUMMY_MAIL);
    }

    public static List<Meeting> DUMMY_MEETING = Arrays.asList(
            new Meeting(new Hour(20, 35), new Date(21, 1, 2022),
                    new Date(8, 2, 2020), "B", DUMMY_MAIL, new User("jean", "jeanmoulin@gmail.com"), "Lorem ipsum ",
                    R.drawable.circle_view_orange),
            new Meeting(new Hour(15, 00), new Date(10, 5, 2021),
                    new Date(3, 5, 2019), "D", DUMMY_MAIL, new User("Louis", "louisde@gmail.com"), "Lorem ",
                    R.drawable.circle_view_purple),
            new Meeting(new Hour(12, 50), new Date(17, 3, 2023),
                    new Date(25, 9, 2020), "A", DUMMY_MAIL, new User("Marc", "louisde@gmail.com"), "Lorem ",
                    R.drawable.circle_view_blue),
            new Meeting(new Hour(18, 0), new Date(2, 12, 2012),
                    new Date(1, 1, 2017), "A", DUMMY_MAIL, new User("Charles", "louisde@gmail.com"), "Lorem ",
                    R.drawable.circle_view_orange),
            new Meeting(new Hour(10, 30), new Date(21, 4, 2022),
                    new Date(9, 2, 2020), "C", DUMMY_MAIL, new User("Harry", "louisde@gmail.com"), "Lorem ",
                    R.drawable.circle_view_orange),
            new Meeting(new Hour(11, 0), new Date(25, 6, 2019),
                    new Date(25, 2, 2020), "F", DUMMY_MAIL, new User("Hermione", "louisde@gmail.com"), "Lorem ",
                    R.drawable.circle_view_blue),
            new Meeting(new Hour(13, 15), new Date(3, 11, 2020),
                    new Date(10, 3, 2020), "D", DUMMY_MAIL, new User("Ron", "louisde@gmail.com"), "Lorem ",
                    R.drawable.circle_view_purple),
            new Meeting(new Hour(8, 30), new Date(9, 8, 2021),
                    new Date(16, 12, 2020), "D", DUMMY_MAIL, new User("Malfoy", "louisde@gmail.com"), "Lorem ",
                    R.drawable.circle_view_orange),
            new Meeting(new Hour(7, 0), new Date(12, 9, 2021),
                    new Date(24, 1, 2018), "A", DUMMY_MAIL, new User("Dumbledor", "louisde@gmail.com"), "Lorem ",
                    R.drawable.circle_view_red));

    public static List<Meeting> generateList() {
        return new ArrayList<>(DUMMY_MEETING);
    }
}

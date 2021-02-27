package com.example.mareu.service;

import com.example.mareu.R;
import com.example.mareu.injection.Injection;
import com.example.mareu.model.Date;
import com.example.mareu.model.Hour;
import com.example.mareu.model.Meeting;
import com.example.mareu.model.User;
import com.example.mareu.utils.Utils;

import junit.framework.TestCase;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

public class ListMeetingServiceTest extends TestCase {
    private ListMeetingService service = Injection.getListMeetingService();


    @Test
    public void testAddMeeting() {
        List<Meeting> meetingList = service.getMeetingList();
        int expectedSize = meetingList.size();

        List<String> mail = new ArrayList<>();
        mail.add("first@");
        mail.add("second@");
        Meeting meeting = new Meeting(new Hour(10, 50), new Date(20, 10, 2021), new Date(19, 2, 2021),
                "B", mail, new User("Dobby", "Dobby@"), "Phoenix order", R.drawable.circle_view_blue, new Hour(1, 30));
        meetingList.add(meeting);

        assertEquals(expectedSize + 1, meetingList.size());
        assertTrue(meetingList.contains(meeting));
    }

    @Test
    public void testDeleteMeeting() {
        List<Meeting> meetingList = service.getMeetingList();
        Meeting meeting = meetingList.get(0);
        int expectedSize = meetingList.size();

        service.deleteMeeting(meeting);

        meetingList = service.getMeetingList();
        assertEquals(expectedSize - 1, meetingList.size());
        assertFalse(meetingList.contains(meeting));

        service.addMeeting(meeting);
    }

    @Test
    public void testGetMeetingList() {
        List<Meeting> meetingList = service.getMeetingList();
        List<Meeting> expectedMeeting = DummyMeetingGenerator.DUMMY_MEETING;
        assertThat(expectedMeeting, IsIterableContainingInAnyOrder.containsInAnyOrder(meetingList.toArray()));
    }

    @Test
    public void testGetMeeting() {
        List<Meeting> expectedMeeting = DummyMeetingGenerator.DUMMY_MEETING;
        Meeting meeting = service.getMeeting(2);
        assertTrue(expectedMeeting.contains(meeting));
    }

    @Test
    public void testGetFilteredMeetingList() {
        List<Meeting> meetingList = service.getMeetingList();
        List<Meeting> expectedMeeting = DummyMeetingGenerator.DUMMY_MEETING;

        int expectedBefore = 3;
        assertNotSame(expectedBefore, meetingList.size());

        meetingList = service.getFilteredMeetingList(Utils.EnumDate.before, new Date(19, 2, 2021));
        assertEquals(expectedBefore, meetingList.size());

        int expectedAfter = 6;
        meetingList = service.getMeetingList();
        assertNotSame(expectedAfter, meetingList.size());

        meetingList = service.getFilteredMeetingList(Utils.EnumDate.after, new Date(19, 2, 2021));
        assertEquals(expectedAfter, meetingList.size());

        meetingList = service.getMeetingList();
        Meeting meeting = null;
        for (int i = 0; i< meetingList.size(); i++){
            if (Utils.getEqual(new Date(21, 1, 2022), meetingList.get(i).getMeetingDate())){
                meeting = meetingList.get(i);
            }
        }
        int expectedSpecific = 1;

        meetingList = service.getFilteredMeetingList(Utils.EnumDate.specific, new Date(21, 1, 2022));
        assertEquals(expectedSpecific, meetingList.size());
        assertTrue(meetingList.contains(meeting));
    }

    @Test
    public void testGetFilteredMeetingListRange() {

        List<Meeting> meetingList = service.getMeetingList();
        List<Meeting> expectedMeeting = DummyMeetingGenerator.DUMMY_MEETING;

        int expected = 4;
        assertNotSame(expected, meetingList.size());

        meetingList = service.getFilteredMeetingListRange(new Date(19, 2, 2021), new Date(19, 2, 2022));
        assertEquals(expected, meetingList.size());
    }

    @Test
    public void testGetFilteredMeetingListLocation() {
        List<Meeting> meetingList = service.getMeetingList();
        int expectedSize = 3;

        assertNotSame(expectedSize, meetingList.size());

        ArrayList<Utils.EnumRoom> boxes = new ArrayList<>();
        boxes.add(Utils.EnumRoom.A);

        meetingList = service.getFilteredMeetingListLocation(boxes);

        assertEquals(expectedSize, meetingList.size());
    }

    @Test
    public void testGetFilteredBothMeetingList() {
        List<Meeting> meetingList;
        ArrayList<Utils.EnumRoom> boxes = new ArrayList<>();
        boxes.add(Utils.EnumRoom.A);

        ListMeetingService parallelList1 = new ListMeetingService();
        List<Meeting> meetingListFiltered1 = parallelList1.getFilteredMeetingListLocation(boxes);
        int expectedSize = 3;
        assertEquals(expectedSize, meetingListFiltered1.size());

        int expected = 4;
        ListMeetingService parallelList2 = new ListMeetingService();
        List<Meeting> meetingListFiltered2 = parallelList2.getFilteredMeetingListRange(new Date(19, 2, 2021),
                new Date(19, 2, 2022));
        assertEquals(expected, meetingListFiltered2.size());

        int expectedFilteredBoth = 1;
        meetingList = service.getFilteredBothMeetingList(meetingListFiltered1, meetingListFiltered2);

        assertEquals(expectedFilteredBoth, meetingList.size());
    }
}
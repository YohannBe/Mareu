package com.example.mareu;

import android.view.View;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.mareu.Utils.ChildViewAction;
import com.example.mareu.Utils.DeleteViewAction;
import com.example.mareu.Utils.RecyclerViewItemCountAssertion;
import com.example.mareu.injection.Injection;
import com.example.mareu.model.Meeting;
import com.example.mareu.service.ListMeetingService;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)

public class MainActivityTest extends TestCase {
    private final ListMeetingService service = Injection.getListMeetingService();
    List<Meeting> meetingList = service.getMeetingList();

    @Rule
    public ActivityTestRule mActivityRule =
            new ActivityTestRule(MainActivity.class);


    @Test
    public void deleteMeetingWithSuccess() {

        onView(withId(R.id.mainActivity)).check(matches(isDisplayed()));

        meetingList = service.getMeetingList();
        int expectedItem = meetingList.size();

        onView(ViewMatchers.withId(R.id.recycleView_list_item))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));

        onView(withId(R.id.recycleView_list_item)).check(RecyclerViewItemCountAssertion.withItemCount(expectedItem - 1));
    }

    @Test
    public void navigateToAddMeetingWithSuccess(){
        onView(withId(R.id.mainActivity)).check(matches(isDisplayed()));

        onView(ViewMatchers.withId(R.id.add_meeting_floatting_button))
                .perform(click());

        onView(withId(R.id.add_meeting)).check(matches(isDisplayed()));
    }
}
package com.example.mareu;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.example.mareu.Utils.WithViewCount.withViewCount;

@RunWith(AndroidJUnit4.class)

public class AddMeetingTest extends TestCase {

    @Rule
    public ActivityTestRule mActivityRule =
            new ActivityTestRule(AddMeeting.class);

    @Test
    public void showCalendarWithSuccess(){
        onView(ViewMatchers.withId(R.id.add_meeting)).check(matches(isDisplayed()));
        onView(withId(R.id.date_collapse)).perform(click());
        onView(withId(R.id.date_calendar)).check(matches(isDisplayed()));
    }

    @Test
    public void showTimePickerWithSuccess(){
        onView(ViewMatchers.withId(R.id.add_meeting)).check(matches(isDisplayed()));
        onView(withId(R.id.hour_collapse)).perform(click());
        onView(withId(R.id.hour_timepicker)).check(matches(isDisplayed()));
    }

    @Test
    public void addParticipantWithSuccess(){
        onView(ViewMatchers.withId(R.id.add_meeting)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.add_meeting)).check(matches(withViewCount(withId(R.id.item_participant), 0)));
        onView(withId(R.id.add_participant_icon)).perform(scrollTo());
        onView(withId(R.id.add_participant_icon)).perform(click());
        onView(withId(R.id.add_participant_icon)).perform(scrollTo());
        onView(withId(R.id.add_participant_icon)).perform(click());


        onView(ViewMatchers.withId(R.id.add_meeting)).check(matches(withViewCount(withId(R.id.item_participant), 2)));
    }

}
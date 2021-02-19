package com.example.mareu.utils;

import com.example.mareu.model.Date;
import com.example.mareu.model.Hour;

import junit.framework.TestCase;

import org.junit.Test;

public class UtilsTest extends TestCase {

    Date date1, date2, date3;
    Hour hour, hour2;

    @Test
    public void testCompareDate() {
        date1 = new Date(21, 1, 1993);
        date2 = new Date(19, 02, 2021);
        assertTrue(Utils.compareDate(date1, date2));
    }

    @Test
    public void testGetBefore() {
        date1 = new Date(21, 1, 1993);
        date2 = new Date(19, 2, 2021);
        assertFalse(Utils.getBefore(date1, date2));
        assertTrue(Utils.getBefore(date2, date1));
    }

    @Test
    public void testGetAfter() {
        date1 = new Date(21, 1, 1993);
        date2 = new Date(19, 2, 2021);
        assertTrue(Utils.getAfter(date1, date2));
        assertFalse(Utils.getAfter(date2, date1));

    }

    @Test
    public void testGetEqual() {

        date1 = new Date(21, 1, 1993);
        date2 = new Date(19, 2, 2021);
        date3 = new Date(21, 1, 1993);

        assertTrue(Utils.getEqual(date1, date3));
        assertFalse(Utils.getEqual(date1, date2));
    }

    @Test
    public void testDateToString() {
        date1 = new Date(21, 1, 1993);
        String expected = "21.1.1993";
        assertEquals(expected, Utils.dateToString(date1));
    }

    @Test
    public void testHourToString() {
        hour = new Hour(10, 50);
        hour2 = new Hour(5, 9);
        String expected = "10:50";
        String expected2 = "05:09";

        assertEquals(expected, Utils.hourToString(hour));
        assertEquals(expected2, Utils.hourToString(hour2));
    }
}
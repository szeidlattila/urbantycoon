/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package UrbanTycoon;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author ati
 */
public class DateTest {
    
    /**
     * year, month, day, hour, minute won't change
     */
    @Test
    public void nMinutesElapsedTest1() {
        Date date1 = new Date(1000, 1, 1, 0, 0);
        date1.nMinutesElapsed(0);
        Date date2 = new Date(1000, 1, 1, 0, 0);
        assertEquals(date2, date1);
    }
    
    /**
     * year, month, day, hour won't change
     */
    @Test
    public void nMinutesElapsedTest2() {
        Date date1 = new Date(1000, 1, 1, 0, 0);
        date1.nMinutesElapsed(59);
        Date date2 = new Date(1000, 1, 1, 0, 59);
        assertEquals(date2, date1);
    }
    
    /**
     * year, month, day won't change
     */
    @Test
    public void nMinutesElapsedTest3() {
        Date date1 = new Date(1000, 1, 1, 0, 0);
        date1.nMinutesElapsed(60);
        Date date2 = new Date(1000, 1, 1, 1, 0);
        assertEquals(date2, date1);
    }
    
    /**
     * year, month won't change
     */
    @Test
    public void nMinutesElapsedTest4() {
        Date date1 = new Date(1000, 1, 1, 0, 0);
        date1.nMinutesElapsed(60*24);
        Date date2 = new Date(1000, 1, 2, 0, 0);
        assertEquals(date2, date1);
    }
    
    /**
     * year won't change
     */
    @Test
    public void nMinutesElapsedTest5() {
        Date date1 = new Date(1000, 1, 1, 0, 0);
        date1.nMinutesElapsed(60*24*31);
        Date date2 = new Date(1000, 2, 1, 0, 0);
        assertEquals(date2, date1);
    }
    
    /**
     * year will change
     */
    @Test
    public void nMinutesElapsedTest6() {
        Date date1 = new Date(1000, 1, 1, 0, 0);
        date1.nMinutesElapsed(60*24*365);
        Date date2 = new Date(1001, 1, 1, 0, 0);
        assertEquals(date2, date1);
    }
    
    /**
     * year, month, day, hour won't change
     */
    @Test
    public void nHoursElapsedTest1() {
        Date date1 = new Date(1000, 1, 1, 0, 0);
        date1.nHoursElapsed(0);
        Date date2 = new Date(1000, 1, 1, 0, 0);
        assertEquals(date2, date1);
    }
    
    /**
     * year, month, day won't change
     */
    @Test
    public void nHoursElapsedTest2() {
        Date date1 = new Date(1000, 1, 1, 0, 0);
        date1.nHoursElapsed(23);
        Date date2 = new Date(1000, 1, 1, 23, 0);
        assertEquals(date2, date1);
    }
    
    /**
     * year, month won't change
     */
    @Test
    public void nHoursElapsedTest3() {
        Date date1 = new Date(1000, 1, 1, 0, 0);
        date1.nHoursElapsed(24);
        Date date2 = new Date(1000, 1, 2, 0, 0);
        assertEquals(date2, date1);
    }
    
    /**
     * year won't change
     */
    @Test
    public void nHoursElapsedTest4() {
        Date date1 = new Date(1000, 1, 1, 0, 0);
        date1.nHoursElapsed(24*31);
        Date date2 = new Date(1000, 2, 1, 0, 0);
        assertEquals(date2, date1);
    }
    
    /**
     * year will change
     */
    @Test
    public void nHoursElapsedTest5() {
        Date date1 = new Date(1000, 1, 1, 0, 0);
        date1.nHoursElapsed(24*365);
        Date date2 = new Date(1001, 1, 1, 0, 0);
        assertEquals(date2, date1);
    }
    
    /**
     * year, month, day won't change
     */
    @Test
    public void nDaysElapsedTest1() {
        Date date1 = new Date(1000, 1, 1, 0, 0);
        date1.nDaysElapsed(0);
        Date date2 = new Date(1000, 1, 1, 0, 0);
        assertEquals(date2, date1);
    }
    
    /**
     * year, month won't change
     */
    @Test
    public void nDaysElapsedTest2() {
        Date date1 = new Date(1000, 1, 1, 0, 0);
        date1.nDaysElapsed(30);
        Date date2 = new Date(1000, 1, 31, 0, 0);
        assertEquals(date2, date1);
    }
    
    /**
     * year won't change
     */
    @Test
    public void nDaysElapsedTest3() {
        Date date1 = new Date(1000, 1, 1, 0, 0);
        date1.nDaysElapsed(31);
        Date date2 = new Date(1000, 2, 1, 0, 0);
        assertEquals(date2, date1);
    }
    
    /**
     * year will change
     */
    @Test
    public void nDaysElapsedTest4() {
        Date date1 = new Date(1000, 1, 1, 0, 0);
        date1.nDaysElapsed(365);
        Date date2 = new Date(1001, 1, 1, 0, 0);
        assertEquals(date2, date1);
    }
    
    /**
     * year, month won't change
     */
    @Test
    public void nMonthsElapsedTest1() {
        Date date1 = new Date(1000, 1, 1, 0, 0);
        date1.nMonthsElapsed(0);
        Date date2 = new Date(1000, 1, 1, 0, 0);
        assertEquals(date2, date1);
    }
    
    /**
     * year won't change
     */
    @Test
    public void nMonthsElapsedTest2() {
        Date date1 = new Date(1000, 1, 1, 0, 0);
        date1.nMonthsElapsed(11);
        Date date2 = new Date(1000, 12, 1, 0, 0);
        assertEquals(date2, date1);
    }
    
    /**
     * year will change
     */
    @Test
    public void nMonthsElapsedTest3() {
        Date date1 = new Date(1000, 1, 1, 0, 0);
        date1.nMonthsElapsed(12);
        Date date2 = new Date(1001, 1, 1, 0, 0);
        assertEquals(date2, date1);
    }
    
    /**
     * date1 > date2
     */
    @Test
    public void howManyDaysPassedTest1() {
        Date date1 = new Date(1000, 1, 10, 10, 10);
        Date date2 = new Date(1000, 1, 1, 5, 5);
        assertEquals(9, date1.howManyDaysPassed(date2));
    }
    
    /**
     * date1 < date2
     */
    @Test
    public void howManyDaysPassedTest2() {
        Date date1 = new Date(1000, 1, 1, 0, 0);
        Date date2 = new Date(1000, 1, 10, 10, 10);
        assertEquals(-9, date1.howManyDaysPassed(date2));
    }
    
    /**
     * date1 = date2
     */
    @Test
    public void howManyDaysPassedTest3() {
        Date date1 = new Date(1000, 1, 1, 1, 1);
        Date date2 = new Date(1000, 1, 1, 1, 1);
        assertEquals(0, date1.howManyDaysPassed(date2));
    }
    
    /**
     * 1 month passed
     */
    @Test
    public void howManyDaysPassedTest4() {
        Date date1 = new Date(1000, 2, 1, 0, 0);
        Date date2 = new Date(1000, 1, 1, 0, 0);
        assertEquals(31, date1.howManyDaysPassed(date2));
    }
    
    /**
     * 1 year passed
     */
    @Test
    public void howManyDaysPassedTest5() {
        Date date1 = new Date(1001, 1, 1, 0, 0);
        Date date2 = new Date(1000, 1, 1, 0, 0);
        assertEquals(365, date1.howManyDaysPassed(date2));
    }
    
    /**
     * 1 year 1 month 1 day passed
     */
    @Test
    public void howManyDaysPassedTest6() {
        Date date1 = new Date(1001, 2, 2, 0, 0);
        Date date2 = new Date(1000, 1, 1, 0, 0);
        assertEquals(365+31+1, date1.howManyDaysPassed(date2));
    }
    
    /**
     * date1 = date2
     */
    @Test
    public void hoursElapsedTest1() {
        Date date1 = new Date(1000, 1, 1, 0, 0);
        Date date2 = new Date(1000, 1, 1, 0, 0);
        assertEquals(0, date1.hoursElapsed(date2));
    }
    
    /**
     * 1 hour elapsed, date1 < date2
     */
    @Test
    public void hoursElapsedTest2() {
        Date date1 = new Date(1000, 1, 1, 0, 0);
        Date date2 = new Date(1000, 1, 1, 1, 0);
        assertEquals(1, date1.hoursElapsed(date2));
    }
    
    /**
     * 1 hour elapsed, date1 > date2
     */
    @Test
    public void hoursElapsedTest3() {
        Date date2 = new Date(1000, 1, 1, 0, 0);
        Date date1 = new Date(1000, 1, 1, 1, 0);
        assertEquals(1, date1.hoursElapsed(date2));
    }
    
    /**
     * 1 day elapsed
     */
    @Test
    public void hoursElapsedTest4() {
        Date date1 = new Date(1000, 1, 1, 0, 0);
        Date date2 = new Date(1000, 1, 2, 0, 0);
        assertEquals(24, date1.hoursElapsed(date2));
    }
    
    /**
     * 1 month elapsed
     */
    @Test
    public void hoursElapsedTest5() {
        Date date1 = new Date(1000, 1, 1, 0, 0);
        Date date2 = new Date(1000, 2, 1, 0, 0);
        assertEquals(24*31, date1.hoursElapsed(date2));
    }
    
    /**
     * 1 year elapsed
     */
    @Test
    public void hoursElapsedTest6() {
        Date date1 = new Date(1000, 1, 1, 0, 0);
        Date date2 = new Date(1001, 1, 1, 0, 0);
        assertEquals(24*365, date1.hoursElapsed(date2));
    }
    
    /**
     * 1 year 1 month 1 day 1 hour 1 minute elapsed
     */
    @Test
    public void hoursElapsedTest7() {
        Date date1 = new Date(1000, 1, 1, 0, 0);
        Date date2 = new Date(1001, 2, 2, 1, 1);
        assertEquals(24*365+24*31+24+1, date1.hoursElapsed(date2));
    }
}

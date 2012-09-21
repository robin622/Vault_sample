package com.redhat.tools.vault.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DateUtilTest {
    
    Calendar cal = null;

    @Before
    public void setUp() throws Exception {
        cal = Calendar.getInstance();
        //e.g.: 2012-9-26 9:30:00
        cal.set(2012, 8, 26, 9, 30, 0);
        cal.set(Calendar.MILLISECOND, 200);
    }

    @After
    public void tearDown() throws Exception {
        cal = null;
    }

    @Test
    public void testGetGMTTimeZone() {
        TimeZone tz = DateUtil.getGMTTimeZone();
        Assert.assertEquals("Greenwich Mean Time", tz.getDisplayName());
    }

    @Test
    public void testConvertTimeZone() {
        Calendar ccal = DateUtil.convertTimeZone(cal, TimeZone.getTimeZone("GMT-8"));
        Assert.assertEquals(9, ccal.get(Calendar.HOUR));
    }

    @Test
    public void testGetHourMinute() {
        Assert.assertEquals("09:30", DateUtil.getHourMinute(cal.getTime()));
    }

    @Test
    public void testAsHours() {
        Assert.assertEquals(24, DateUtil.asHours("1d"));
    }

    @Test
    public void testAsMinutes() {
        Assert.assertEquals(1440, DateUtil.asMinutes("1d"));
        Assert.assertEquals(60, DateUtil.asMinutes("1H"));
    }

    @Test
    public void testAsMills() {
        Assert.assertEquals(3000, DateUtil.asMills("3s"));
    }

    @Test
    public void testGetMiddleDateTime() {
        Assert.assertEquals("09-26 09:30", DateUtil.getMiddleDateTime(cal.getTime()));
    }

    @Test
    public void testRandomDateBetween() {
        Calendar cal1 = Calendar.getInstance(TimeZone.getTimeZone("GMT-8"));
        cal1.set(2010, 10, 10);
        
        Calendar cal2 = Calendar.getInstance(TimeZone.getTimeZone("GMT-8"));
        cal2.set(2020, 10, 10);
        
        Date rdate = DateUtil.randomDateBetween(cal1.getTime(), cal2.getTime());
        Calendar rcal = Calendar.getInstance(TimeZone.getTimeZone("GMT-8"));
        rcal.setTime(rdate);
        
        Assert.assertTrue(rcal.compareTo(cal1) > 0);
        Assert.assertTrue(rcal.compareTo(cal2) < 0);
    }

}

package com.redhat.tools.vault.util;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FormatManagerTest {
    
    private static final String DEFAULT_TIME_FORMAT_PATTERN = "HH:mm:ss";
    private static final String DEFAULT_TIMESTAMP_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String DEFAULT_NUMBER_FORMAT = "#,##0.00";
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
    public void testGetDefaultDateFormatPattern() {
        DateFormat df = FormatManager.getDefaultDateFormat();
        Assert.assertEquals("2012-09-26", df.format(cal.getTime()));
    }

    @Test
    public void testGetDefaultTimestampFormatPattern() {
        Assert.assertEquals(FormatManagerTest.DEFAULT_TIMESTAMP_FORMAT_PATTERN, FormatManager.getDefaultTimestampFormatPattern());
    }

    @Test
    public void testGetDefaultTimeFormatPattern() {
        Assert.assertEquals(FormatManagerTest.DEFAULT_TIME_FORMAT_PATTERN, FormatManager.getDefaultTimeFormatPattern());
    }

    @Test
    public void testGetDefaultTimestampFormat() {
        DateFormat df = FormatManager.getDefaultTimestampFormat();
        Assert.assertEquals("2012-09-26 09:30:00.200", df.format(cal.getTime()));
    }

    @Test
    public void testGetDefaultNumberFormatPattern() {
        Assert.assertEquals(FormatManagerTest.DEFAULT_NUMBER_FORMAT, FormatManager.getDefaultNumberFormatPattern());
    }

    @Test
    public void testGetDefaultDateFormat() {
        DateFormat df = FormatManager.getDefaultDateFormat();
        Assert.assertEquals("2012-09-26", df.format(cal.getTime()));
    }

    @Test
    public void testGetDateFormatString() {
        String formatString = "yyyy/MM/dd";
        DateFormat df = FormatManager.getDateFormat(formatString);
        Assert.assertEquals("2012/09/26", df.format(cal.getTime()));
    }

    @Test
    public void testGetDefaultNumberFormat() {
        NumberFormat nf = FormatManager.getDefaultNumberFormat();
        Assert.assertEquals("12.35",nf.format(12.34564d));
        Assert.assertEquals("100,001.35",nf.format(100001.34564d));
    }

    @Test
    public void testGetNumberFormat() {
        String formatString = "#,##0.000";
        NumberFormat nf = FormatManager.getNumberFormat(formatString);
        Assert.assertEquals("12.346",nf.format(12.34564d));
        Assert.assertEquals("100,001.346",nf.format(100001.34564d));
        
        formatString = "#,###";
        nf = FormatManager.getNumberFormat(formatString);
        Assert.assertEquals("12",nf.format(12.34564d));
        Assert.assertEquals("100,001",nf.format(100001.34564d));
    }

}

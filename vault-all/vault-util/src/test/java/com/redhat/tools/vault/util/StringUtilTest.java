package com.redhat.tools.vault.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StringUtilTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testRandomString() {
        Assert.assertEquals(6, (StringUtil.randomString(6)).length());
    }

    @Test
    public void testSplitStringToArray() {
        Assert.assertArrayEquals(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9" },
                StringUtil.splitStringToArray("1,2,3,4,5,6,7,8,9", ","));
    }

    @Test
    public void testStringToArray2String() {
        Assert.assertArrayEquals(new String[] { "util", "service", "test", "dao" },
                StringUtil.stringToArray2("util;service,test;dao"));
    }

    @Test
    public void testArrayToString() {
        Assert.assertEquals("util,service,test,dao",
                StringUtil.arrayToString(new String[] { "util", "service", "test", "dao" }, ",").toString());
    }

    @Test
    public void testListToString() {
        List<String> temp = new ArrayList<String>();
        temp.add("util");
        temp.add("service");
        temp.add("test");
        temp.add("dao");
        Assert.assertEquals("util,service,test,dao",StringUtil.listToString(temp, ","));
    }

    @Test
    public void testDuplicateString() {
        Assert.assertEquals("a,a,a,a,a",StringUtil.duplicateString("a", 5, ","));
    }

    @Test
    public void testLeftPadStringIntString() {
        Assert.assertEquals("0098", StringUtil.leftPad(98, 4));
    }

    @Test
    public void testRightPad() {
        Assert.assertEquals("9800", StringUtil.rightPad("98", 4, "0"));
    }

    @Test
    public void testIsIP() {
        Assert.assertEquals(Boolean.TRUE, StringUtil.isIP("10.66.15.153"));
        Assert.assertEquals(Boolean.FALSE, StringUtil.isIP("10.66.15:153"));
        Assert.assertEquals(Boolean.FALSE, StringUtil.isIP("10.66,15.153"));
        Assert.assertEquals(Boolean.FALSE, StringUtil.isIP("10.66.15"));
        Assert.assertEquals(Boolean.FALSE, StringUtil.isIP("10-66-15:153"));
    }

    @Test
    public void testNullOrBlank() {
        Assert.assertEquals(Boolean.TRUE,StringUtil.nullOrBlank(""));
        Assert.assertEquals(Boolean.TRUE,StringUtil.nullOrBlank(null));
        Assert.assertEquals(Boolean.TRUE,StringUtil.nullOrBlank("   "));
    }

    @Test
    public void testNotNull() {
        Assert.assertEquals("",StringUtil.notNull(""));
        Assert.assertEquals("",StringUtil.notNull(null));
        Assert.assertEquals("123",StringUtil.notNull("123"));
    }

    @Test
    public void testIsVaildEmail() {
        Assert.assertEquals(Boolean.TRUE,StringUtil.isVaildEmail("wguo@redhat.com"));
        Assert.assertEquals(Boolean.FALSE,StringUtil.isVaildEmail("aa.redhat.com"));
        Assert.assertEquals(Boolean.FALSE,StringUtil.isVaildEmail("aa@"));
        Assert.assertEquals(Boolean.FALSE,StringUtil.isVaildEmail("aa@redhat"));
    }

    @Test
    public void testFormartCCList() {
        Assert.assertEquals("abc@redhat.com,bcd@redhat.com,wabc@redhat.com",
                StringUtil.formartCCList("abc@redhat.com,bcd@redhat.com,wabc@redhat.com"));
        Assert.assertEquals(
                "abc@redhat.com,bcd@redhat.com,wabc@redhat.com,abc2@redhat.com,bcd2@redhat.com,wabc2@redhat.com,<br>abc@redhat.com,bcd@redhat.com,wabc@redhat.com,abc2@redhat.com,bcd2@redhat.com,wabc2@redhat.com",
                StringUtil
                        .formartCCList("abc@redhat.com,bcd@redhat.com,wabc@redhat.com,abc2@redhat.com,bcd2@redhat.com,wabc2@redhat.com,abc@redhat.com,bcd@redhat.com,wabc@redhat.com,abc2@redhat.com,bcd2@redhat.com,wabc2@redhat.com"));

    }

    @Test
    public void testDeFormartCCList() {
        Assert.assertEquals(
                "abc@redhat.com,bcd@redhat.com,wabc@redhat.com,abc2@redhat.com,bcd2@redhat.com,wabc2@redhat.com,abc@redhat.com,bcd@redhat.com,wabc@redhat.com,abc2@redhat.com,bcd2@redhat.com,wabc2@redhat.com",
                StringUtil
                        .deFormartCCList("abc@redhat.com,bcd@redhat.com,wabc@redhat.com,abc2@redhat.com,bcd2@redhat.com,wabc2@redhat.com,<br>abc@redhat.com,bcd@redhat.com,wabc@redhat.com,abc2@redhat.com,bcd2@redhat.com,wabc2@redhat.com"));
    }

}

package com.redhat.tools.vault.util;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArithmeticUtilTest {

    double[] ds = null;
    int[] is = null;
    
    @Before
    public void setUp() throws Exception {
        ds = new double[5];
        ds[0] = 1.2d;
        ds[1] = 2.3d;
        ds[2] = 1.5d;
        ds[3] = 1.6d;
        ds[4] = 4.3d;
        
        is = new int[5];
        is[0] = 2;
        is[1] = 5;
        is[2] = 9;
        is[3] = 3;
        is[4] = 1;
                
    }

    @After
    public void tearDown() throws Exception {
        ds = null;
        is = null;
    }

    @Test
    public void testAddShortShort() {
        Assert.assertEquals(2.5,ArithmeticUtil.add(1.2, 1.3));
    }

    @Test
    public void testAddIntegerInteger() {
        Assert.assertEquals(57.0,ArithmeticUtil.add(23, 34));
    }

    @Test
    public void testMaxDoubleArray() {
        Assert.assertEquals(4.3,ArithmeticUtil.max(ds));
    }

    @Test
    public void testMinDoubleArray() {
        Assert.assertEquals(1.2,ArithmeticUtil.min(ds));
    }

    @Test
    public void testMaxIntArray() {
        Assert.assertEquals(9,ArithmeticUtil.max(is));
    }

    @Test
    public void testMinIntArray() {
        Assert.assertEquals(1,ArithmeticUtil.min(is));
    }

}

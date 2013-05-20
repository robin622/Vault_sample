package com.redhat.tools.vault.util;

import java.util.Properties;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PropertiesUtilTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void readProperties() {
        Properties p = PropertiesUtil.readProperties("roles.properties");
        String role = (String) p.get("wezhao");
        Assert.assertEquals("Admin", role);
    }

}

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
        Properties p = PropertiesUtil.readProperties("/home/zym/Downloads/jboss-eap-6.0/standalone/deployments/vault-web.war/WEB-INF/classes/roles.properties");
        String role = (String) p.get("wezhao");
        Assert.assertEquals("Admin", role);
    }

}

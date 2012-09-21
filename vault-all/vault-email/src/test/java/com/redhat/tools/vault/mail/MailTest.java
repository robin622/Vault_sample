package com.redhat.tools.vault.mail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MailTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSendMail() {
        try {
            Mail.sendMail("localhost", "wguo@redhat.com", "wguo", 
                    "wguo@redhat.com", "", "Send Mail Test", "This is a test mail! Please ignore!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

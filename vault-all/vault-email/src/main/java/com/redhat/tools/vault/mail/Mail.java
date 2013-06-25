package com.redhat.tools.vault.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 
 * @author wguo@redhat.com
 * 
 */
public class Mail {

    /**
     * 
     * @param smtpServerAddress
     * @param fromUserAddress
     * @param fromUserName
     * @param toUserAddress
     * @param ccUserAddress
     * @param subject
     * @param text
     */
    public static void sendMail(String smtpServerAddress, String fromUserAddress, String fromUserName, String toUserAddress,
            String ccUserAddress, String subject, String text) throws Exception {
        try {
            String encoding = "utf-8";
            Properties props = System.getProperties();
            props.put("mail.smtp.host", smtpServerAddress);
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(fromUserAddress, fromUserName, encoding));
            mimeMessage.setRecipients(Message.RecipientType.TO, toUserAddress);
            mimeMessage.addRecipients(Message.RecipientType.CC, ccUserAddress);
            mimeMessage.setSubject(subject, encoding);
            mimeMessage.setContent(text, "text/plain");
            mimeMessage.setSentDate(new Date());
            Transport.send(mimeMessage);
        } catch (Exception e) {

        }
    }

}

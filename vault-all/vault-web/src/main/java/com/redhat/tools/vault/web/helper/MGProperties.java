/******************************************************************************
 * JBoss, a division of Red Hat                                               *
 * Copyright 2006, Red Hat Middleware, LLC, and individual                    *
 * contributors as indicated by the @authors tag. See the                     *
 * copyright.txt in the distribution for a full listing of                    *
 * individual contributors.                                                   *
 *                                                                            *
 * This is free software; you can redistribute it and/or modify it            *
 * under the terms of the GNU Lesser General Public License as                *
 * published by the Free Software Foundation; either version 2.1 of           *
 * the License, or (at your option) any later version.                        *
 *                                                                            *
 * This software is distributed in the hope that it will be useful,           *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU           *
 * Lesser General Public License for more details.                            *
 *                                                                            *
 * You should have received a copy of the GNU Lesser General Public           *
 * License along with this software; if not, write to the Free                *
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA         *
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.                   *
 ******************************************************************************/
package com.redhat.tools.vault.web.helper;
import java.util.Properties;
import org.jboss.logging.Logger;

/**
 * @author wezhao
 *
 */
public class MGProperties {

	/** The logger. */
	protected static final Logger log = Logger.getLogger(MGProperties.class);

	/** Properties */
	private Properties prop = null;

	/** Instance */
	private static MGProperties instance = null;

	private MGProperties() {
		prop = new Properties();
	}

	public static String KEY_CHECKNAME = "checkname";

	public static String KEY_CHECKURL = "checkurl";

	public static String KEY_CHECKSUMMARY = "checksummary";

	public static String KEY_CHECKEMAIL = "checkemail";

	public static String KEY_CHECKSEARCHKEY = "checksearchkey";

	public static String KEY_ATTACHEMENTPATH = "attachmentpath";

	public static String NAME_INTERVAL = "mg.til.timer.interval";

	public static String NAME_SMTP = "mg.til.mail.smtp";

	public static String NAME_MANAGER_ADDRESS = "mg.til.mail.manager.address";

	public static String NAME_MANAGER_MAXADDRESSES = "mg.til.mail.manager.maxaddresses";

	public static String NAME_MAIL_EXPRESSION = "mg.til.mail.check.expression";

	public static String NAME_ADMIN_NAME = "mg.val.admin.name";

	public static String NAME_VAULT_NAME = "mg.val.vault.name";

	public static String NAME_TEAM_NAME = "mg.til.team.name";

	public static String NAME_MANAGER_ID = "mg.til.manager.id";

	public static String NAME_TIME_ROUNDUP = "mg.til.time.roundup";

	public static String NAME_TIME_HRANGE = "mg.til.time.hrange";

	public static String NAME_TIME_MRANGE = "mg.til.time.mrange";

	public static String NAME_CHECKEXP = "checkexpression";

	public static Integer COUNT_SEND_EMAIL = 0;

	public static String NAME_SERVER = null;

	public static Integer PORT_SERVER = null;

	public static String URL_ACTION = null;

	public static String USER_NAME = null;

	public static MGProperties getInstance() {
		if (instance == null) {
			synchronized(MGProperties.class){
				if(instance == null){
					instance = new MGProperties();
				}
			}
		}
		return instance;
	}

	private void setValue(String key, String value) {
		if (value == null) {
			return;
		}
		if (prop.setProperty(key, value) == null) {
			prop.put(key, value);
		}
	}

	private String getValue(String key, String defaultValue) {
		String value = prop.getProperty(key);
		log.debug("get:" + key + "=" + value);
		if (value == null)
			value = defaultValue;
		return value;
	}

	public String getValue(String key) {
		String value = prop.getProperty(key);
		return value;
	}

	/**
	 * @param value
	 */
	public void setTimerInterval(String value) {
		String key = NAME_INTERVAL;
		this.setValue(key, value);
	}

	/**
	 * @return
	 */
	public String getTimerInterval() {
		String key = NAME_INTERVAL;
		String value = "3000";
		return getValue(key, value);
	}

	/**
	 * @param value
	 */
	public void setSMTPAddress(String value) {
		String key = NAME_SMTP;
		this.setValue(key, value);
	}

	/**
	 * @return
	 */
	public String getSMTPAddress() {
		String key = NAME_SMTP;
		String value = "localhost";
		return getValue(key, value);
	}

	/**
	 * @param value
	 */
	public void setManagerAddress(String value) {
		String key = NAME_MANAGER_ADDRESS;
		this.setValue(key, value);
	}

	/**
	 * @return
	 */
	public String getManagerAddress() {
		String key = NAME_MANAGER_ADDRESS;
		String value = "speng@redhat.com";
		return getValue(key, value);
	}

	/**
	 * @param value
	 */
	public void setManagerMaxAddresses(String value) {
		String key = NAME_MANAGER_MAXADDRESSES;
		this.setValue(key, value);
	}

	/**
	 * @return
	 */
	public int getManagerMaxAddresses() {
		String key = NAME_MANAGER_MAXADDRESSES;
		String value = "3";
		return Integer.parseInt(getValue(key, value));
	}

	/**
	 * @param value
	 */
	public void setAdminName(String value) {
		String key = NAME_ADMIN_NAME;
		this.setValue(key, value);
	}

	public void setVaultName(String value) {
		String key = NAME_VAULT_NAME;
		this.setValue(key, value);
	}

	/**
	 * @return
	 */
	public String getAdminName() {
		String key = NAME_ADMIN_NAME;
		String value = "Admin";
		return getValue(key, value);
	}

	public String getVaultName() {
		String key = NAME_VAULT_NAME;
		String value = "Vault";
		return getValue(key, value);
	}

	/**
	 * @param value
	 */
	public void setTeamName(String value) {
		String key = NAME_TEAM_NAME;
		this.setValue(key, value);
	}

	/**
	 * @return
	 */
	public String getTeamName() {
		String key = NAME_TEAM_NAME;
		String value = "User";
		return getValue(key, value);
	}

	/**
	 * @param value
	 */
	public void setManagerId(String value) {
		String key = NAME_MANAGER_ID;
		this.setValue(key, value);
	}

	/**
	 * @return
	 */
	public String getManagerId() {
		String key = NAME_MANAGER_ID;
		String value = "1";
		return getValue(key, value);
	}

	/**
	 * @param value
	 */
	public void setTimeRoundup(String value) {
		String key = NAME_TIME_ROUNDUP;
		this.setValue(key, value);
	}

	/**
	 * @return
	 */
	public String getTimeRoundup() {
		String key = NAME_TIME_ROUNDUP;
		String value = "45";
		return getValue(key, value);
	}

	/**
	 * @param value
	 */
	public void setTimeHrange(String value) {
		String key = NAME_TIME_HRANGE;
		this.setValue(key, value);
	}

	/**
	 * @return
	 */
	public String[] getTimeHrange() {
		String key = NAME_TIME_HRANGE;
		String value = "00,01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23";
		return getValue(key, value).split(",");

	}

	/**
	 * @param value
	 */
	public void setTimeMrange(String value) {
		String key = NAME_TIME_MRANGE;
		this.setValue(key, value);
	}

	/**
	 * @return
	 */
	public String[] getTimeMrange() {
		String key = NAME_TIME_MRANGE;
		String value = "05,10,15,20,25,30,35,40,45,50,55";
		return getValue(key, value).split(",");
	}

	/**
	 * @param value
	 */
	public void setMailExpression(String value) {
		String key = NAME_MAIL_EXPRESSION;
		this.setValue(key, value);
	}

	/**
	 * @return
	 */
	public String getMailExpression() {
		String key = NAME_MAIL_EXPRESSION;
		String value = "/^[0-9A-Za-z._-]+@[0-9A-Za-z._-]+$/";
		return getValue(key, value);
	}

	/**
	 * @param value
	 */
	public void setCheckExp(String value) {
		String key = NAME_CHECKEXP;
		this.setValue(key, value);
	}

	/**
	 * @return
	 */
	public String getCheckExp() {
		String key = NAME_CHECKEXP;
		String value = "/^[0-9A-Za-z:\\/.?\\-_\\+=#@&%]+$/";
		return getValue(key, value);
	}

}

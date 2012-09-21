package com.redhat.tools.vault.util;

public interface BaseConstants {
	/**
	 * Blank space
	 */
	public static final String BLANK_SPACE = " ";
	/**
	 * It is used to split complex primary key.
	 */
	public static final String PK_SPLITOR = "|";

	public static final String ITEM_SEPARATOR = ",";

	public static final String TB_WORD_SEPARATOR = "_";

	/**
	 * If the notification is mail, the receiverinfo including name, address1,
	 * address2, address3. These information must be splited by ADDRESS_SPLITOR.
	 * for example: Beijing/Haidian/Shangdi/Zhongguancun Chuangye Tower.
	 */
	public static final String ADDRESS_SPLITOR = "/";

	public static final String STR_YES = "Y";

	public static final String STR_NO = "N";

	public static final String STR_TRUE = "T";

	public static final String STR_FALSE = "F";

	/**
	 * Boolean defination.
	 */
	public static final Short YES = new Short((short) 1);

	public static final Short NO = new Short((short) 0);

	public static final Short TRUE = new Short((short) 1);

	public static final Short FALSE = new Short((short) 0);

	/**
	 * Common access level.
	 */
	public static final Integer ACCESS_LEVEL_ANYONE = new Integer(0);

	public static final Integer ACCESS_LEVEL_FRIEND = new Integer(1);

	// means frieds of my friends
	public static final Integer ACCESS_LEVEL_FOF = new Integer(2);

	public static final Integer ACCESS_LEVEL_PASSWORD = new Integer(3);

	public static final Short AVAILABLE = new Short((short) 0);

	public static final Short NOT_AVAILABLE = new Short((short) 1);

	public static final String KEY_USERPROFILE = "BTW_USERPROFILE";

	public static final String KEY_BTW_USERPROFILE = "BTW_USERPROFILE";

	public static final String KEY_CACHEMANAGER = "BTW_CACHEMANAGER";

	/**
	 * only for test
	 */
	public static final Integer TEST_USER_ID = 1;

}

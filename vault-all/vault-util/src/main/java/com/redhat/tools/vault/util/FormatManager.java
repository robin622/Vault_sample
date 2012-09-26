package com.redhat.tools.vault.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class FormatManager {
	private static final String DEFAULT_MMDD_FORMAT_PATTERN = "MM-dd";

	private static final String DEFAULT_DATE_FORMAT_PATTERN = "yyyy-MM-dd";

	private static final String DEFAULT_TIME_FORMAT_PATTERN = "HH:mm:ss";

	private static final String DEFAULT_TIMESTAMP_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

	private static final String DEFAULT_NUMBER_FORMAT = "#,##0.00";

	public static String getDefaultDateFormatPattern() {
		return DEFAULT_DATE_FORMAT_PATTERN;
	}

	public static String getDefaultTimestampFormatPattern() {
		return DEFAULT_TIMESTAMP_FORMAT_PATTERN;
	}

	public static String getDefaultTimeFormatPattern() {
		return DEFAULT_TIME_FORMAT_PATTERN;
	}

	public static DateFormat getDefaultTimestampFormat() {
		return new SimpleDateFormat(DEFAULT_TIMESTAMP_FORMAT_PATTERN);
	}

	public static String getDefaultNumberFormatPattern() {
		return DEFAULT_NUMBER_FORMAT;
	}

	public static DateFormat getDefaultDateFormat() {
		return new SimpleDateFormat(DEFAULT_DATE_FORMAT_PATTERN);
	}

	public static DateFormat getDefaultDateFormat(Locale locale) {
		return new SimpleDateFormat(DEFAULT_DATE_FORMAT_PATTERN, locale);
	}

	public static DateFormat getDateFormat(String formatString) {
		return (formatString == null ? null
				: new SimpleDateFormat(formatString));
	}

	public static NumberFormat getDefaultNumberFormat() {
		return new DecimalFormat(DEFAULT_NUMBER_FORMAT);
	}

	public static NumberFormat getNumberFormat(String formatString) {
		return (formatString == null ? null : (new DecimalFormat(formatString)));
	}

	/**
	 * get date/time format for a given format name or a pattern, and given time
	 * zone. <br>
	 * It always returns an Object unless formatPattern is null. <br>
	 * If formatPattern is a pre-defined format name, then use the pre-defined
	 * pattern for the name. <br>
	 * If formatPattern is not a pre-defined format name, then formatPattern is
	 * the pattern. <br>
	 * 
	 * @param formatPattern is pre-defined format name or any valid pattern
	 * @param timeZoneId the time zone Id
	 * @return DateFormat is the format. if formatName is null then retruens
	 * null.
	 */
	public static DateFormat getDateFormat(String formatPattern,
			String timeZoneId) {
		return (getDateFormat(formatPattern, TimeZone.getTimeZone(timeZoneId)));
	}

	/**
	 * get date/time format for a given format name or a pattern, and given time
	 * zone. <br>
	 * It always returns an Object unless formatPattern is null. <br>
	 * If formatPattern is a pre-defined format name, then use the pre-defined
	 * pattern for the name. <br>
	 * If formatPattern is not a pre-defined format name, then formatPattern is
	 * the pattern. <br>
	 * 
	 * @param formatPattern is pre-defined format name or any valid pattern
	 * @param timeZone the time zone
	 * @return DateFormat is the format. if formatName is null then retruens
	 * null.
	 */
	public static DateFormat getDateFormat(String formatPattern,
			TimeZone timeZone) {
		if (formatPattern == null)
			return (null);
		DateFormat fmt = new SimpleDateFormat(formatPattern);
		if (timeZone != null)
			fmt.setTimeZone(timeZone);
		return (fmt);
	}

	public static DateFormat getLocaleDateFormat(String localeCode) {
		if (localeCode.startsWith("zh_CN")) {
			return new SimpleDateFormat("yyyy年MM月dd日");
		}
		else if (localeCode.startsWith("ja_JP")) {
			return new SimpleDateFormat("yyyy年MM月dd日");
		}
		return new SimpleDateFormat(DEFAULT_DATE_FORMAT_PATTERN);

	}

	public static DateFormat getLocaleMMDDFormat(String localeCode) {
		if (localeCode.startsWith("zh_CN")) {
			return new SimpleDateFormat("MM月dd日");
		}
		else if (localeCode.startsWith("ja_JP")) {
			return new SimpleDateFormat("MM月dd日");
		}
		return new SimpleDateFormat(DEFAULT_MMDD_FORMAT_PATTERN);

	}
}

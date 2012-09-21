package com.redhat.tools.vault.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.jboss.logging.Logger;



/**
 * Date Tools <br>
 * Notes:
 * 
 * <pre>
 * 1. Absolute time in milliseconds from epoch (1970-01-01 00:00:00 GMT:00) is independent of any TimeZone.
 *    time fields (Year, Month, day, Hour, Minute, Second and Millisecond) are always based on a TimeZone. 
 *    When a date/time is converted from one TimeZone to another TimeZone, its absolute time in milliseconds
 *    is not changed, but the time fields may be changed.
 * 
 * 2. java.util.Date and java.util.Calendar both internally store the absolute time in milliseconds from epoch (1970-01-01 00:00:00 GMT:00). and
 *    in addition, Date only knows the system default host TimeZone, but Calendar stores a given TimeZone.
 * 
 * 3. System.currentTimeMillis() - this returns current absolute time in 
 *      milliseconds from epoch (1970-01-01 00:00:00 GMT:00). This value is same for all timezones at current time point.
 * 
 * 4. java.util.Calendar.getTimeInMillis() - this returns absolute time in milliseconds from epoch (1970-01-01 00:00:00 GMT:00).
 * 
 * 5. java.util.Date.getTime() - this returns absolute time in milliseconds from epoch (1970-01-01 00:00:00 GMT:00).
 * 
 * 6. new java.util.Date() - this returns current Date/Time in default system host timezone.
 * 
 * 7. new java.util.Date(year - 1900, month, day, hour, minute, second) - this returns Date/Time with given fields(Year, month, day, hour, minute, second) in default system host timezone.
 * 
 * 8. Calendar.getInstance() - this returns a current date/time in system deault host timezone.
 * 
 * 9. Calendar.getInstance(TimeZone) - this returns a current date/time in given timezone.
 *    How to create a Date using given year, month, day, hour, minute and second in given TimeZone?
 *       Calendar cal = Calendar.getInstance(myTimeZone);
 *       cal.set(year, month, day, hour, minute, second);
 *       Date date = cal.getTime();
 * 
 * 10. Because java.util.Calendar internally uses lazy-computation mechanism to maintain absolute time and time fields, so after you set
 *    its one or more fields (timezone, year, month, day, hour, minute, second, millisecond, etc.) it is a good practice to call calendar.get(Calendar.YEAR) to
 *    force calendar to re-compute all fields in synch. otherwise sometimes the result is confusing.
 *    for example: 
 *      Calendar cal = Calendar.getInstance();
 *      cal.setTimeZone(zone1);
 *      cal.set(Calendar.HOUR_OF_DAY, 1);
 *      cal.setTimeZone(zone2);
 *    In this example, the hour is 1 for TimeZone zone2 because setTimeZone() and set() methods don't re-compute the absolute time. After the setTimeZone(zone2) is 
 *    called, the previous timezone zone1 is replaced by zone2. So when re-compute the absolute time, it is based on zone2.
 *    but see following code:
 *      Calendar cal = Calendar.getInstance();
 *      cal.setTimeZone(zone1);
 *      cal.set(Calendar.HOUR_OF_DAY, 1);
 *      cal.get(Calendar.YEAR);
 *      cal.setTimeZone(zone2);
 *    In this example, the hour is 1 for TimeZone zone1 because get(Calendar.YEAR) forces it to recompute the absolute time. Then after its
 *    TimeZone is set to zone2, its new fields will be based on the updated absolute time.
 * 
 * 11. java.text.SimpleDateFormat uses setTimeZone(timezone) to set its format TimeZone used when format(date) is called. Its default timezone is system host timezone.
 *      formater.setTimeZone(zone);
 *      formater.format(date);
 * 
 * 12. JDBC timestamp timezone: 
 *     When set parameters in PreparedStatement:
 *       use system default host TimeZone:
 *         prepStmt.setDate(int paramIndex, java.sql.Date date)
 *         prepStmt.setTimestamp(int paramIndex, java.sql.Timestamp ts)
 *         prepStmt.setTime(int paramIndex, java.sql.Time time)
 *       use the TimeZone in given calendar:
 *         prepStmt.setDate(int paramIndex, java.sql.Date date, Calendar cal)
 *         prepStmt.setTimestamp(int paramIndex, java.sql.Timestamp ts, Calendar cal)
 *         prepStmt.setTime(int paramIndex, java.sql.Time time, Calendar cal)
 *     When get data from ResultSet:
 *       use system default host TimeZone:
 *         rs.getDate(int columnIndex)
 *         rs.getDate(String columnName)
 *         rs.getTimestamp(int columnIndex)
 *         rs.getTimestamp(String columnName)
 *         rs.getTime(int columnIndex)
 *         rs.getTime(String columnName)
 *       use the TimeZone in given calendar:
 *         rs.getDate(int columnIndex, Calendar cal)
 *         rs.getDate(String columnName, Calendar cal)
 *         rs.getTimestamp(int columnIndex, Calendar cal)
 *         rs.getTimestamp(String columnName, Calendar cal)
 *         rs.getTime(int columnIndex, Calendar cal)
 *         rs.getTime(String columnName, Calendar cal)
 *  
 * </pre>
 * 
 * @author speng@redhat.com
 */
public class DateUtil {
	/**
	 * logger
	 */
    protected static final Logger logger = Logger.getLogger(DateUtil.class);

	/**
	 * GMT:00 TimeZone TODO: after change all callers to use getGMTTimeZone() to
	 * get GMT TimeZone, please change it to be protected.
	 * 
	 * @deprecated this field will become protected. Please use getGMTTimeZone()
	 *             method instead of referencing this field directly.
	 */
	private static final TimeZone GMTTimeZone = TimeZone.getTimeZone("GMT");

	/**
	 * System timestamp format.
	 */
	private static final String SYSTEM_TIMESTAMP_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * System date format.
	 */
	private static final String SYSTEM_DATE_FORMAT_PATTERN = "yyyy-MM-dd";

	/**
	 * System timestamp with timezone format.
	 */
	private static final String SYSTEM_TIMESTAMPZ_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS z";

	private static final String SYSTEM_LONGTIMESTAMPZ_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS Z";

	private static final DateFormat HOUR_MINUTE_FORMATTER = new SimpleDateFormat("HH:mm");

	/**
	 * Calendar for GMT:00 TimeZone. Don't change its TimeZone. Its time is not
	 * used. This constant is for some situations that need to retrieve a
	 * TimeZone info from a calendar such as in JDBC preparedStatement call
	 * setTimestamp(int paramIndex, Date date, Calendar cal), ResultSet call
	 * getTimestamp(int index, Calendar cal). <br>
	 * TODO: after change all callers to use getGMTCalendar() to get GMT
	 * Calendar, please change it to be protected.
	 * 
	 * @deprecated this field will become protected. Please use getGMTCalendar()
	 *             method instead of referencing this field directly.
	 */
	// public static final Calendar GMTCalendar =
	// Calendar.getInstance(GMTTimeZone);
	/**
	 * Constructor
	 */
	protected DateUtil() {
	}

	/**
	 * get a GMT:00 TimeZone
	 * 
	 * @return java.util.TimeZone a GMT:00 TimeZone
	 */
	public static final TimeZone getGMTTimeZone() {
		// for now, calls just use the time zone after get it, don't change it,
		// so can use single instance.
		// If some calls change the time zone data, then here should be:
		// return((TimeZone) GMTTimeZone.clone());
		return (GMTTimeZone);
	}

	/**
	 * get a Calendar with GMT:00 TimeZone. Most calls change the calendar data
	 * after get it, so here it always return a new Calendar Object.
	 * 
	 * @return java.util.Calendar the Calendar with GMT:00 TimeZone
	 */
	public static final Calendar getGMTCalendar() {
		return (Calendar.getInstance(getGMTTimeZone()));
	}

	/**
	 * get current GMT:00 Timestamp
	 * 
	 * @return java.util.Calendar the current timestamp in GMT:00 timezone
	 */
	public static final Calendar getGMTTimestamp() {
		return (Calendar.getInstance(getGMTTimeZone()));
	}

	/**
	 * get only displayed GMT mean Time. Its real value is not GMT Time.
	 * 
	 * @return java.sql.Timestamp
	 */
	public static final Date getGMTDisplayTimestamp() {
		return toDate(new Date(), getGMTTimeZone());
	}

	public static final Date getGMTDisplayTimestamp(Date dt) {
		return toDate(dt, getGMTTimeZone());
	}

	/**
	 * get a calendar with a java.util.Date and given TimeZone
	 * 
	 * @param timeZone
	 *            the timezone
	 * @param date
	 *            the date
	 * @return java.util.Calendar
	 */
	public static final Calendar getCalendar(TimeZone timeZone, Date date) {
		Calendar cal = Calendar.getInstance(timeZone);
		cal.setTime(date);
		return (cal);
	}
	public static final Calendar getCalendar(String timeZoneStr) {
		TimeZone timeZone = TimeZone.getTimeZone(timeZoneStr);
		Calendar cal = Calendar.getInstance(timeZone);
		return (cal);
	}
	/**
	 * get a calendar with given date and time fields that are in given
	 * TimeZone.
	 * 
	 * @param timeZone
	 *            the TimeZone. if it is null, then use system host time zone.
	 * @param date
	 *            the date without time information or its time information will
	 *            be overridden by new values
	 * @param hour
	 *            the hour, 0 - 23
	 * @param minute
	 *            the minute, 0 - 59
	 * @param second
	 *            the second, 0 - 59
	 * @param millis
	 *            the millisecond, 0 - 999
	 * @return Calendar
	 */
	public static final Calendar getCalendar(TimeZone timeZone, Date date, int hour, int minute, int second, int millis) {
		if (timeZone == null)
			timeZone = TimeZone.getDefault();
		Calendar cal = Calendar.getInstance(timeZone);
		cal.setTime(date);
		cal.get(Calendar.YEAR); // force it to re-compute all fields
		// set new Time info
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
		cal.set(Calendar.MILLISECOND, millis);
		cal.get(Calendar.YEAR); // force it to re-compute all fields
		return (cal);
	}

	/**
	 * get a calendar with given date and time fields that are in given
	 * TimeZone.
	 * 
	 * @param timeZone
	 *            the TimeZone. if it is null, then use system host time zone.
	 * @param date
	 *            the date without time information or its time information will
	 *            be overridden by new values
	 * @param hour
	 *            the hour, 0 - 23
	 * @param minute
	 *            the minute, 0 - 59
	 * @param second
	 *            the second, 0 - 59
	 * @return Calendar
	 */
	public static final Calendar getCalendar(TimeZone timeZone, Date date, int hour, int minute, int second) {
		return (getCalendar(timeZone, date, hour, minute, second, 0));
	}

	/**
	 * get a calendar with given time fields that are in given TimeZone.
	 * 
	 * @param timeZone
	 *            the TimeZone. if it is null, then use system host time zone.
	 * @param year
	 *            the year in 4-digits.
	 * @param month
	 *            the month, 0 - 11
	 * @param day
	 *            the day of the month, start from 1.
	 * @param hour
	 *            the hour, 0 - 23
	 * @param minute
	 *            the minute, 0 - 59
	 * @param second
	 *            the second, 0 - 59
	 * @param millis
	 *            the millisecond, 0 - 999
	 * @return Calendar
	 */
	public static final Calendar getCalendar(TimeZone timeZone, int year, int month, int day, int hour, int minute,
			int second, int millis) {
		if (timeZone == null)
			timeZone = TimeZone.getDefault();
		Calendar cal = Calendar.getInstance(timeZone);
		cal.set(year, month, day, hour, minute, second);
		cal.set(Calendar.MILLISECOND, millis);
		cal.get(Calendar.YEAR); // force it to re-compute all fields
		return (cal);
	}

	/**
	 * get a calendar with given time fields that are in given TimeZone.
	 * 
	 * @param timeZone
	 *            the TimeZone. if it is null, then use system host time zone.
	 * @param year
	 *            the year in 4-digits.
	 * @param month
	 *            the month, 0 - 11
	 * @param day
	 *            the day of the month, start from 1.
	 * @param hour
	 *            the hour, 0 - 23
	 * @param minute
	 *            the minute, 0 - 59
	 * @param second
	 *            the second, 0 - 59
	 * @return Calendar
	 */
	public static final Calendar getCalendar(TimeZone timeZone, int year, int month, int day, int hour, int minute,
			int second) {
		return (getCalendar(timeZone, year, month, day, hour, minute, second, 0));
	}

	/**
	 * get a calendar with given time fields that are in system host TimeZone.
	 * 
	 * @param year
	 *            the year in 4-digits such as 2005.
	 * @param month
	 *            the month, 0 - 11
	 * @param day
	 *            the day of the month, start from 1.
	 * @param hour
	 *            the hour, 0 - 23
	 * @param minute
	 *            the minute, 0 - 59
	 * @param second
	 *            the second, 0 - 59
	 * @param millis
	 *            the millisecond, 0 - 999
	 * @return Calendar
	 */
	public static final Calendar getCalendar(int year, int month, int day, int hour, int minute, int second, int millis) {
		return (getCalendar(null, year, month, day, hour, minute, second, millis));
	}

	/**
	 * get a calendar with given time fields that are in system host TimeZone.
	 * 
	 * @param year
	 *            the year in 4-digits such as 2005.
	 * @param month
	 *            the month, 0 - 11
	 * @param day
	 *            the day of the month, start from 1.
	 * @param hour
	 *            the hour, 0 - 23
	 * @param minute
	 *            the minute, 0 - 59
	 * @param second
	 *            the second, 0 - 59
	 * @return Calendar
	 */
	public static final Calendar getCalendar(int year, int month, int day, int hour, int minute, int second) {
		return (getCalendar(null, year, month, day, hour, minute, second, 0));
	}

	/**
	 * convert a calendar to another timezone. Both have the same absolute time.
	 * 
	 * @param fromCalendar
	 *            the original calendar, which will be changed after the call.
	 * @param toTimeZone
	 *            the new timezone
	 * @return the new calendar in new timezone, which is the same calendar
	 *         object with timezone changed to new one.
	 */
	public static final Calendar convertTimeZone(Calendar fromCalendar, TimeZone toTimeZone) {
		fromCalendar.setTimeZone(toTimeZone);
		fromCalendar.get(Calendar.YEAR); // force it to re-compute all fields
		return (fromCalendar);
	}

	/**
	 * get a time zone offset in milliseconds based on a given date. If Daylight
	 * Saving Time is in effect at the given date, the offset value is adjusted
	 * with the amount of daylight saving.
	 * 
	 * @param timeZone
	 *            the TimeZone
	 * @param date
	 *            the date
	 * @return offset of the timezone in milliseconds
	 */
	public static int getTimeZoneOffset(TimeZone timeZone, Date date) {
		if (timeZone.inDaylightTime(date)) {
			return (timeZone.getRawOffset() + timeZone.getDSTSavings());
		} else {
			return (timeZone.getRawOffset());
		}
	}

	/**
	 * get a time zone offset in milliseconds based on a given date. If Daylight
	 * Saving Time is in effect at the given date, the offset value is adjusted
	 * with the amount of daylight saving.
	 * 
	 * @param timeZone
	 *            the TimeZone
	 * @param date
	 *            the date in milliseconds since January 1, 1970, 00:00:00.000
	 *            GMT
	 * @return offset of the timezone in milliseconds
	 */
	public static int getTimeZoneOffset(TimeZone timeZone, long date) {
		return (getTimeZoneOffset(timeZone, new Date(date)));
	}

	/**
	 * In some cases, when parse a String into Date using a Time Zone (called
	 * fromTimeZone), but the String actually represents a Date in another Time
	 * Zone (called toTimeZone), so after the Date object is created based on
	 * fromTimeZone, we need to adjust it to the right Date. <br>
	 * For example, assume "2006-05-01" is a date in "Asia/Shanghai" time zone,
	 * but we parse it using "America/New_York" time zone (which is the default
	 * time zone of a host in New York): <br>
	 * 
	 * <pre>
	 * String strDate = &quot;2006-05-01&quot;;
	 * SimpleDateFormat fmt = new SimpleDateFormat(&quot;yyyy-MM-dd&quot;);
	 * Date date = fmt.parse(strDate);
	 * date = DateTool.adjustDate(date, TimeZone.getDefault(), TimeZone.getTimeZone(&quot;Asia/Shanghai&quot;));
	 * // now the date is the right date representing 2006-05-01 in Asia/Shanghai time zone.
	 * </pre>
	 * 
	 * Of course we can create a formater with the desired TimeZone (using
	 * fmt.setTimeZone()) and parse the string directly. But in some cases, the
	 * string is already parsed using different TimeZone, then we need to adjust
	 * it. <br>
	 * 
	 * @param date
	 * @param fromTimeZone
	 * @param toTimeZone
	 * @return Date
	 */
	public static final Date adjustDate(Date date, TimeZone fromTimeZone, TimeZone toTimeZone) {
		return (new Date(date.getTime() + getTimeZoneOffset(fromTimeZone, date) - getTimeZoneOffset(toTimeZone, date)));
	}

	/**
	 * In some cases, we want to format a Date into a String representing this
	 * Date in a Time Zone (called toTimeZone), but we have a date formater in
	 * another Time Zone (called fromTimeZone), so we need to disrupt the Date
	 * object before applying the formater. <br>
	 * For example, we have a formater in host default time zone (which is
	 * "America/New_York") whose pattern is "yyyy-MM-dd HH:mm:ss", we want to
	 * format a Date into a string in Time Zone "Asia/Shanghai". <br>
	 * 
	 * <pre>
	 * Date date = new Date();
	 * SimpleDateFormat fmt = new SimpleDateFormat(&quot;yyyy-MM-dd HH:mm:ss&quot;);
	 * System.out.println(&quot;date in default time zone: &quot; + fmt.format(date));
	 * date = DateTool.disruptDate(date, TimeZone.getDefault(), TimeZone.getTimeZone(&quot;Asia/Shanghai&quot;));
	 * String strDate = fmt.format(date);
	 * System.out.println(&quot;date in time zone Asia/Shanghai: &quot; + strDate);
	 * // now the strDate is the String representing the Date in Asia/Shanghai time zone.
	 * </pre>
	 * 
	 * This case is the reverse of adjustDate(). <br>
	 * 
	 * @param date
	 * @param fromTimeZone
	 * @param toTimeZone
	 * @return Date
	 */
	public static final Date disruptDate(Date date, TimeZone fromTimeZone, TimeZone toTimeZone) {
		return (new Date(date.getTime() + getTimeZoneOffset(toTimeZone, date) - getTimeZoneOffset(fromTimeZone, date)));
	}

	/**
	 * get the current Date/Time which represents the absolute time now.
	 * 
	 * @return Date
	 */
	public static final Date getDate() {
		return (new Date());
	}

	public static final Date today(){
		return dateOnly(new Date());
	}
	/**
	 * get a Date with given time fields that are in given TimeZone.
	 * 
	 * @param timeZone
	 *            the TimeZone. if it is null, then use system host time zone.
	 * @param year
	 *            the year in 4-digits.
	 * @param month
	 *            the month, 0 - 11
	 * @param day
	 *            the day of the month, start from 1.
	 * @param hour
	 *            the hour, 0 - 23
	 * @param minute
	 *            the minute, 0 - 59
	 * @param second
	 *            the second, 0 - 59
	 * @param millis
	 *            the millisecond, 0 - 999
	 * @return Date
	 */
	public static final Date getDate(TimeZone timeZone, int year, int month, int day, int hour, int minute, int second,
			int millis) {
		return (getCalendar(timeZone, year, month, day, hour, minute, second, millis).getTime());
	}

	/**
	 * get a Date with given time fields that are in given TimeZone.
	 * 
	 * @param timeZone
	 *            the TimeZone. if it is null, then use system host time zone.
	 * @param year
	 *            the year in 4-digits.
	 * @param month
	 *            the month, 0 - 11
	 * @param day
	 *            the day of the month, start from 1.
	 * @param hour
	 *            the hour, 0 - 23
	 * @param minute
	 *            the minute, 0 - 59
	 * @param second
	 *            the second, 0 - 59
	 * @return Date
	 */
	public static final Date getDate(TimeZone timeZone, int year, int month, int day, int hour, int minute, int second) {
		return (getCalendar(timeZone, year, month, day, hour, minute, second, 0).getTime());
	}

	/**
	 * get a Date with given time fields that are in system host TimeZone
	 * 
	 * @param year
	 *            the year in 4-digits.
	 * @param month
	 *            the month, 0 - 11
	 * @param day
	 *            the day of the month, start from 1.
	 * @param hour
	 *            the hour, 0 - 23
	 * @param minute
	 *            the minute, 0 - 59
	 * @param second
	 *            the second, 0 - 59
	 * @param millis
	 *            the millisecond, 0 - 999
	 * @return Date
	 */
	public static final Date getDate(int year, int month, int day, int hour, int minute, int second, int millis) {
		return (getCalendar(null, year, month, day, hour, minute, second, millis).getTime());
	}

	/**
	 * get a Date with given time fields that are in system host TimeZone
	 * 
	 * @param year
	 *            the year in 4-digits.
	 * @param month
	 *            the month, 0 - 11
	 * @param day
	 *            the day of the month, start from 1.
	 * @param hour
	 *            the hour, 0 - 23
	 * @param minute
	 *            the minute, 0 - 59
	 * @param second
	 *            the second, 0 - 59
	 * @return Date
	 */
	public static final Date getDate(int year, int month, int day, int hour, int minute, int second) {
		return (getCalendar(null, year, month, day, hour, minute, second, 0).getTime());
	}

	/**
	 * Convert String Date to java.util.Date according to format.
	 * 
	 * @author diff_i@hotmail.com
	 * @param dateStr
	 * @param format,yyyy-MM-dd
	 *            HH:mm:ss
	 * @return java.util.Date
	 * 
	 * <pre>
	 * For example:
	 * String dtString = &quot;2005-1-1 20:18:19&quot;;
	 * java.util.Date date1 = DateTool.toDate(dtString,&quot;yyyy-MM-dd HH:mm:ss&quot;);
	 * </pre>
	 */
	public static final Date toDate(String dateString, String format) {
		Date date = null;
		SimpleDateFormat formatter = null;
		if (dateString == null || dateString.length() == 0 || format == null || format.length() == 0) {
			return null;
		}
		formatter = new SimpleDateFormat(format);
		try {
			date = formatter.parse(dateString);
		} catch (ParseException e) {
			logger.error("toDate(String, String) error", e);
		}
		return date;
	}

	/**
	 * Convert a java.util.Date(System date with default host timezone) to a new
	 * Date with given timezone
	 * 
	 * @author diff_i@hotmail.com
	 * @param date
	 *            source date
	 * @param timeZone
	 *            destination timezone
	 * @return java.util.Date
	 */
	public static final Date toDate(Date date, TimeZone timeZone) {
		Date dt = null;
		try {
			String ds = toString(date, timeZone);
			DateFormat formatter = FormatManager.getDefaultTimestampFormat();// .getDateFormatObject(SYSTEM_DATETIME_FORMAT);
			dt = formatter.parse(ds);
		} catch (ParseException e) {
			logger.error("toDate(Date, TimeZone parse error)", e);
		}
		return dt;
	}

	/**
	 * Convert dateString of "SYSTEM_TIMESTAMP_FORMATTER" format to Date.
	 * 
	 * @param dateString,
	 *            the input format should be "yyyy-MM-dd HH:mm:ss.SSS"
	 * @return
	 * 
	 * <pre>
	 * For example:
	 * String dtString = &quot;2005-1-1 20:18:19.567&quot;;
	 * java.util.Date date1 = DateTool.toDate(dtString);
	 * </pre>
	 */
	public static final Date toSystemDate(String dateString) {
		Date date = null;
		if (dateString == null || dateString.trim().length() == 0) {
			return null;
		}
		DateFormat format = new SimpleDateFormat(SYSTEM_TIMESTAMP_FORMAT_PATTERN);
		try {
			date = format.parse(dateString);
		} catch (ParseException e) {
			logger.error("toSystemDate(String) error", e);
		}
		return date;
	}

	/**
	 * Method toString.default output format is "yyyy-MM-dd HH:mm:ss"
	 * 
	 * @param date
	 * @return String
	 * 
	 * <pre>
	 * java.util.Date date = new java.util.Date();
	 * 
	 * String str = DateUtil.toString(date);
	 * </pre>
	 */
	public static String toString(Date date) {
		if (date == null) {
			return "";
		}
		DateFormat dateFormat = FormatManager.getDefaultTimestampFormat();
		return dateFormat.format(date);
	}

	/**
	 * Convert date to string according to the format.
	 * 
	 * @author diff_i@hotmail.com
	 * @param date
	 * @param format
	 * @return
	 * 
	 * <pre>
	 * For example:
	 * java.util.Date date = new java.util.Date();
	 * String str = DateUtil.toString(date,&quot;yyyy/MM/dd HH.mm.ss.SSS&quot;);
	 * </pre>
	 */
	public static final String toString(Date date, String format) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		DateFormat formatter = new SimpleDateFormat(format);
		formatter.setCalendar(calendar);
		return formatter.format(date);
	}

	public static final String toString(Date date, String format, String defValue) {
		if (date == null || format == null) {
			return defValue;
		}
		Calendar calendar = Calendar.getInstance();
		DateFormat formatter = new SimpleDateFormat(format);
		formatter.setCalendar(calendar);
		return formatter.format(date);
	}

	/**
	 * Convert a java.util.Date to a new Date STRING with given timezone using
	 * SYSTEM_DATE_FORMATTER
	 * 
	 * @author diff_i@hotmail.com
	 * @param date
	 *            source date (Timezone is host system timezone)
	 * @param timeZone
	 *            destination timezone
	 * @return java.util.Date
	 */
	public static final String toString(Date date, TimeZone timeZone) {
		return toString(date, timeZone, SYSTEM_TIMESTAMP_FORMAT_PATTERN);
	}

	/**
	 * Convert a java.util.Date to a new timeZone Date String according to
	 * format.
	 * 
	 * @author diff_i@hotmail.com
	 * @param date
	 * @param timeZone
	 * @param format
	 * @return String
	 */
	public static final String toString(Date date, TimeZone timeZone, String format) {
		Calendar calendar = Calendar.getInstance(timeZone);
		DateFormat formatter = FormatManager.getDateFormat(format, timeZone);
		formatter.setCalendar(calendar);
		return formatter.format(date);
	}

	/**
	 * Method toSystemString.default output format is "yyyy-MM-dd HH:mm:ss.SSS"
	 * 
	 * @param date
	 * @return String
	 * 
	 * <pre>
	 * For example:
	 * java.util.Date date = new java.util.Date();
	 * String str = DateUtil.toSystemString(date);
	 * </pre>
	 */
	public static String toSystemString(Date date) {
		DateFormat dateFormat = new SimpleDateFormat(SYSTEM_TIMESTAMP_FORMAT_PATTERN);
		return dateFormat.format(date);
	}
	public static final String toLongZString(Date date) {
		return toString(date, SYSTEM_LONGTIMESTAMPZ_FORMAT_PATTERN);
	}
	public static final String toLongString(Date date, TimeZone timeZone) {
		return toString(date, timeZone, SYSTEM_TIMESTAMP_FORMAT_PATTERN);
	}

	/**
	 * 
	 * @param date
	 * @param timeZone
	 * @return
	 */
	public static final String toLongZString(Date date, TimeZone timeZone) {
		return toString(date, timeZone, SYSTEM_LONGTIMESTAMPZ_FORMAT_PATTERN);
	}

	public static final int getBirthdaysBetween(Date birthday, Date now) {
		if (birthday == null || now == null) {
			throw new IllegalArgumentException("birthday or now cannot be null.");
		}
		Date nextBirthday = getNextBirthday(birthday);
		
		return getDaysBetween( nextBirthday, now);
	}

	public static Date getNextBirthday(Date birthday) {
		Date now = new Date();
		int month = getMonth(birthday);
		int day = getDay(birthday);
		int nowMonth = getMonth(now);
		int nowDay = getDay(now);
		Calendar cal = Calendar.getInstance();
		cal.setTime(birthday);
		int year = getYear(now);
		if (month == 2 && day == 29) {
			if (isLeapYear(now)) {
				if (nowMonth > month) {
					Date dt = getNextLeapYear(now);
					year = getYear(dt);
				} else if (nowMonth == month && nowDay > day) {
					Date dt = getNextLeapYear(now);
					year = getYear(dt);
				} 
			} else {
				Date dt = getNextLeapYear(now);
				year = getYear(dt);
			}

		} else {
			if (nowMonth > month) {
				year++;
			} else if (nowMonth == month && nowDay > day) {
				year++;
			}
		}

		cal.set(Calendar.YEAR, year);
		return cal.getTime();
	}

	public static Date getNextLeapYear(Date dt) {
		Calendar cal = Calendar.getInstance();
		int year = getYear(dt);
		cal.setTime(dt);
		year++;
		cal.set(Calendar.YEAR, year);
		while (!isLeapYear(cal.getTime())) {
			year++;
			cal.set(Calendar.YEAR, year);
		}
		return cal.getTime();
	}

	/**
	 * Get difference of days between two date. If date1 is after date2 return
	 * positive number, otherwise return negative number or 0
	 * 
	 * @author diff_i@hotmail.com
	 * @param date1
	 * @param date2
	 * @return int
	 * 
	 * <pre>
	 * For example:
	 * Date date1 = new Date();//2006-09-1 21:32:57
	 * Date date2 = toDate(&quot;2006-09-12&quot;,&quot;yyyy-MM-dd&quot;);
	 * System.out.println(calculateDays(date1,date2);
	 * the output is: 11
	 * </pre>
	 */
	public static final int calculateDays(Date date1, Date date2) {
		return getDaysBetween(date1, date2);
	}

	public static final int getSecondsBetween(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("date1 or date2 cannot be null.");
		}
		long days = (date1.getTime() - date2.getTime()) / 1000;
		return (new Long(days)).intValue();
	}

	public static final int getMinutesBetween(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("date1 or date2 cannot be null.");
		}
		long days = (date1.getTime() - date2.getTime()) / 60000;
		return (new Long(days)).intValue();
	}

	public static final int getHoursBetween(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("date1 or date2 cannot be null.");
		}
		long days = (date1.getTime() - date2.getTime()) / 3600000;
		return (new Long(days)).intValue();
	}

	/**
	 * Get days between two dates.
	 * 
	 * @author diff_i@hotmail.com
	 * @param date1
	 * @param date2
	 * @return
	 * 
	 * <pre>
	 * For example:
	 * Date date1 = new Date();//2006-09-1 21:32:57
	 * Date date2 = toDate(&quot;2006-09-12&quot;,&quot;yyyy-MM-dd&quot;);
	 * System.out.println(getDaysBetweenDates(date1,date2);
	 * the output is: 11
	 * </pre>
	 */
	public static final int getDaysBetween(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("date1 or date2 cannot be null.");
		}
		Date d1 = trimToDate((date1));
		Date d2 = trimToDate(date2);
		long days = (d1.getTime() - d2.getTime()) / 86400000;
		return (new Long(days)).intValue();
	}

	public static final int getHoursBetweenDates(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("date1 or date2 cannot be null.");
		}
		Date d1 = trimTimestamp(date1, "HH");
		Date d2 = trimTimestamp(date2, "HH");
		long days = (d1.getTime() - d2.getTime()) / 3600000;
		return (new Long(days)).intValue();
	}

	public static final int getMinutesBetweenDates(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("date1 or date2 cannot be null.");
		}
		Date d1 = trimTimestamp(date1, "mm");
		Date d2 = trimTimestamp(date2, "mm");
		long days = (d1.getTime() - d2.getTime()) / 60000;
		return (new Long(days)).intValue();
	}

	/**
	 * Judge wether the date is between date1 and date2
	 * 
	 * @author diff_i@hotmail.com
	 * @param date1
	 * @param date2
	 * @param date
	 * @return
	 */
	public static final boolean isBetweenDates(Date date1, Date date2, Date date) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("date1 or date2 cannot be null.");
		}
		return (date.getTime() <= date2.getTime() && date.getTime() >= date1.getTime()) ? true : false;
	}

	/**
	 * Judge wether the date is between date1 and date2(not including date1 and
	 * date2)
	 * 
	 * @author diff_i@hotmail.com
	 * @param date1
	 * @param date2
	 * @param date
	 * @return
	 */
	public static final boolean isBetweenDatesIn(Date date1, Date date2, Date date) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("date1 or date2 cannot be null.");
		}
		return (date.getTime() < date2.getTime() && date.getTime() > date1.getTime()) ? true : false;
	}
	/**
	 * 左闭右开
	 * @param date1
	 * @param date2
	 * @param date
	 * @return
	 */
	public static final boolean isBetweenDates2(Date date1, Date date2, Date date) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("date1 or date2 cannot be null.");
		}
		return (date.getTime() < date2.getTime() && date.getTime() >= date1.getTime()) ? true : false;
	}
	/**
	 * Check whether a date is weekend
	 * 
	 * @author diff_i@hotmail.com
	 * @param date
	 * @return boolean
	 */
	public static final boolean isWeekend(Date date) {
		if (date == null) {
			throw new IllegalArgumentException("Illegal date: " + date);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if ((cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) || (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)) {
			return true;
		}
		return false;
	}

	/**
	 * Check whether a date is weekday
	 * 
	 * @author diff_i@hotmail.com
	 * @param date
	 * @return boolean
	 */
	public static final boolean isWeekday(Date date) {
		return !isWeekend(date);
	}

	/**
	 * Check whether a date is weekday
	 * 
	 * @param date
	 * @return
	 */
	public static final boolean isMonday(Date date) {
		if (date == null) {
			throw new IllegalArgumentException("Illegal date: " + date);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
			return true;
		}
		return false;
	}

	/**
	 * judge whether the date is nth weekday.
	 * 
	 * @param date
	 * @param xWeekday
	 * @return
	 */
	public static final boolean isXWeekday(Date date, int xWeekday) {
		if (date == null) {
			throw new IllegalArgumentException("Illegal date: " + date);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (cal.get(Calendar.DAY_OF_WEEK) == xWeekday) {
			return true;
		}
		return false;
	}

	/**
	 * Check whether date1 is beofe date2 by day (unlike java.util.Date by mili
	 * seconds).
	 * 
	 * @param date1
	 *            java.util.Date
	 * @param date2
	 *            java.util.Date
	 * @return boolean
	 * 
	 * <pre>
	 * For example:
	 * Date date1 = new Date();//2006-09-12
	 * Date date2 = toDate(&quot;2006-09-11&quot;,&quot;yyyy-MM-dd&quot;);
	 * System.out.println(isBeforeByDay(date1, date2);
	 * The output is: false
	 * </pre>
	 */
	public static final boolean isBeforeByDay(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("date1 or date2 cannot be null.");
		}
		return trimToDate(date1).before(trimToDate(date2));
	}

	/**
	 * Check whether date1 is after date2 by day (unlike java.util.Date by mili
	 * seconds).
	 * 
	 * @author diff_i@hotmail.com
	 * @param date1
	 *            java.util.Date
	 * @param date2
	 *            java.util.Date
	 * @return boolean
	 * 
	 * <pre>
	 * For example:
	 * Date date1 = new Date();//2006-09-12
	 * Date date2 = toDate(&quot;2006-09-11&quot;,&quot;yyyy-MM-dd&quot;);
	 * System.out.println(isAfterByDay(date1, date2);
	 * The output is: true
	 * </pre>
	 */
	public static final boolean isAfterByDay(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("date1 or date2 cannot be null.");
		}
		return trimToDate(date1).after(trimToDate(date2));

	}

	/**
	 * Check whether date1 is equal to date2 by day (ignore time part).
	 * 
	 * @author diff_i@hotmail.com
	 * @param date1
	 *            java.util.Date
	 * @param date2
	 *            java.util.Date
	 * @return boolean
	 * @throws IllegalArgumentException
	 *             if date1==null and date2==null
	 * 
	 * <pre>
	 * For example:
	 * Date date1 = new Date();//2006-09-12
	 * Date date2 = toDate(&quot;2006-09-11&quot;,&quot;yyyy-MM-dd&quot;);
	 * System.out.println(isOnSameDay(date1, date2);
	 * The output is: false
	 * </pre>
	 */
	public static final boolean isOnSameDay(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("date1 or date2 cannot be null.");
		}
		return trimToDate(date1).equals(trimToDate(date2));
	}

	public static final boolean isToday(Date date1) {
		if (date1 == null) {
			return false;
		}
		return trimToDate(date1).equals(trimToDate(new Date()));
	}

	/**
	 * Check whether date1 is equal to date2 by day (ignore time part).
	 * 
	 * @author diff_i@hotmail.com
	 * @param cal1
	 *            Calendar
	 * @param cal2
	 *            Calendar
	 * @return boolean
	 * @throws IllegalArgumentException
	 *             if cal1==null and cal2==null
	 * 
	 * <pre>
	 * For example:
	 * Calendar cal1 = new Date();//2006-09-12
	 * Calendar cal2 = toDate(&quot;2006-09-11&quot;,&quot;yyyy-MM-dd&quot;);
	 * System.out.println(isOnSameDay(cal1, cal2);
	 * The output is: false
	 * </pre>
	 */
	public static final boolean isOnSameDay(Calendar cal1, Calendar cal2) {
		if (cal1 == null || cal2 == null) {
			throw new IllegalArgumentException("calendar1 or calendar2 cannot be null.");
		}
		return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1
				.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
	}

	/**
	 * Compare two date in accurancy of Day. If date1 is before date2 return
	 * negative number. If date1 is after date2, return positive number. If they
	 * are equal by day, return 0.
	 * 
	 * @author diff_i@hotmail.com
	 * @param date1
	 *            java.util.Date
	 * @param date2
	 *            java.util.Date if date1==dat2 return 0,if date1>date2 return
	 *            1,if date1<date2 return -1
	 * @return int
	 */
	public static final int compareByDay(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("date1 or date2 cannot be null.");
		}
		long milli1 = trimToDate(date1).getTime();
		long milli2 = trimToDate(date2).getTime();
		int retVal = 0;
		if (milli1 > milli2) {
			retVal = 1;
		} else if (milli1 < milli2) {
			retVal = -1;
		}
		return retVal;
	}

	/**
	 * Compare two date in accurancy of Date&Time(Without millisecond). If date1
	 * is before date2 return negative number. If date1 is after date2, return
	 * positive number. If they are equal by day, return 0.
	 * 
	 * @author diff_i@hotmail.com
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static final int compareByDateTime(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("date1 or date2 cannot be null.");
		}
		long milli1 = date1.getTime() / 1000;
		long milli2 = date2.getTime() / 1000;
		int retVal = 0;
		if (milli1 > milli2) {
			retVal = 1;
		} else if (milli1 < milli2) {
			retVal = -1;
		}
		return retVal;
	}

	/**
	 * Compare two date in accurancy of Date&Time(With milliSecond if exists).
	 * If date1 is before date2 return negative number. If date1 is after date2,
	 * return positive number. If they are equal by day, return 0.
	 * 
	 * @author diff_i@hotmail.com
	 * @param date1
	 *            java.util.Date
	 * @param date2
	 *            java.util.Date if date1==dat2 return 0,if date1>date2 return
	 *            1,if date1<date2 return -1
	 * @return int
	 */
	public static final int compareByTimestamp(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("date1 or date2 cannot be null.");
		}
		long milli1 = date1.getTime();
		long milli2 = date2.getTime();
		int retVal = 0;
		if (milli1 > milli2) {
			retVal = 1;
		} else if (milli1 < milli2) {
			retVal = -1;
		}
		return retVal;
	}

	/**
	 * Compare two date in accurancy of Date(no including time). If date1 is
	 * before date2 return negative number. If date1 is after date2, return
	 * positive number. If they are equal by day, return 0.
	 * 
	 * @author diff_i@hotmail.com
	 * @param date1
	 *            java.util.Date
	 * @param date2
	 *            java.util.Date if date1==dat2 return 0,if date1>date2 return
	 *            1,if date1<date2 return -1
	 * @return int
	 */
	public static final int compareByDate(Date date1, Date date2) {
		int val = getDaysBetween(date1, date2);
		if (val > 0)
			return 1;
		else if (val < 0)
			return -1;
		return val;
	}

	/**
	 * To date without milli second
	 * 
	 * @author diff_i@hotmail.com
	 * @param date
	 * @return
	 */
	public static final Date toDateWithoutMs(java.util.Date date) {
		if (date == null) {
			return null;
		}
		return new Date((date.getTime() / 1000) * 1000);
	}

	/**
	 * Trim Timestamp according the field
	 * 
	 * <pre>
	 * ss, trim to second. The return format is &quot;yyyy-MM-dd HH:mm:ss.000&quot;
	 * mm, trim to minute. The return format is &quot;yyyy-MM-dd HH:mm:00.000&quot;
	 * HH, trim to hour. The return format is &quot;yyyy-MM-dd HH:00:00.000&quot;
	 * dd, trim to hour. The return format is &quot;yyyy-MM-dd 00:00:00.000&quot;
	 * MM, trim to hour. The return format is &quot;yyyy-MM-01 00:00:00.000&quot;
	 * YYYY, trim to hour. The return format is &quot;yyyy-01-01 00:00:00.000&quot;
	 * </pre>
	 * 
	 * @param date
	 * @param field
	 * @return
	 * 
	 * <pre>
	 * For example:
	 * Date date = new Date();
	 * System.out.println(trimTimestamp(date,&quot;mm&quot;));
	 * the out is: 2006-09-11 10:00:00.000
	 * </pre>
	 */
	public static final Date trimTimestamp(java.util.Date date, String field) {
		if (date == null || field == null || field.length() == 0) {
			return date;
		} else if ("ss".equals(field)) {
			return new Date((date.getTime() / 1000) * 1000);
		} else if ("mm".equals(field)) {
			return new Date((date.getTime() / 1000 / 60) * 1000 * 60);
		} else if ("HH".equals(field)) {
			return new Date((date.getTime() / 1000 / 60 / 60) * 1000 * 60 * 60);
		} else if ("dd".equals(field)) {
			return trimToDate(date);
		} else if ("MM".equals(field)) {
			return getFirstDayOfMonth(date);
		} else if ("YYYY".equals(field)) {
			return getFirstDayOfYear(date);
		}
		return null;
	}

	/**
	 * Only get date part of timestamp.
	 * 
	 * @param date
	 * @return
	 * @author
	 * @see
	 * @since 2006-6-11 10:10:55
	 */
	public static final Date toDateWithoutTime(java.util.Date date) {
		return trimToDate(date);
	}

	/**
	 * Trim the time part of timestamp.
	 * 
	 * @param date
	 * @return
	 * @author
	 * @see
	 * @since 2006-9-11 10:11:28
	 */
	public static final Date trimToDate(java.util.Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static final Date addMinutes(Date date, int minutes) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, minutes);
		return cal.getTime();
	}

	public static final Date addHours(Date date, int hours) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, hours);
		return cal.getTime();
	}

	/**
	 * add N days base on the input date.
	 * 
	 * @author diff_i@hotmail.com
	 * @param date
	 * @param days
	 * @return java.util.Date
	 */
	public static final Date addDays(Date date, int days) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}
	
	public static final Date subDays(Date date, int days) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, (-days));
		return cal.getTime();
	}

	/**
	 * Add weeks
	 * 
	 * @author diff_i@hotmail.com
	 * @param date
	 * @param weeks
	 * @return
	 */
	public static final Date addWeeks(Date date, int weeks) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, weeks * 7);
		return cal.getTime();
	}

	/**
	 * Add months
	 * 
	 * @author diff_i@hotmail.com
	 * @param date
	 * @param months
	 * @return
	 */
	public static final Date addMonths(Date date, int months) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, months);
		return cal.getTime();
	}

	/**
	 * Add quarters
	 * 
	 * @author diff_i@hotmail.com
	 * @param date
	 * @param quarters
	 * @return
	 */
	public static final Date addQuarters(Date date, int quarters) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, quarters * 3);
		return cal.getTime();
	}

	/**
	 * Add years
	 * 
	 * @author diff_i@hotmail.com
	 * @param date
	 * @param years
	 * @return java.util.Date
	 */
	public static final Date addYears(Date date, int years) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, years);
		return cal.getTime();
	}

	/**
	 * judge the year whether it is a leap year.
	 * 
	 * @author diff_i@hotmail.com
	 * @param date
	 * @return boolean
	 */
	public static final boolean isLeapYear(Date date) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.isLeapYear(cal.get(Calendar.YEAR));
	}

	/**
	 * Get first day of week
	 * 
	 * @author diff_i@hotmail.com
	 * @param date
	 * @return java.util.Date
	 */
	public static final Date getFirstDayOfWeek(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int days = cal.get(Calendar.DAY_OF_WEEK);
		return addDays(date, (-1) * days + 1);
	}

	/**
	 * Get last day of week
	 * 
	 * @author diff_i@hotmail.com
	 * @param date
	 * @return java.util.Date
	 */
	public static final Date getLastDayOfWeek(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int days = cal.get(Calendar.DAY_OF_WEEK);
		return addDays(date, 7 - days);
	}

	/**
	 * Get first day of this month
	 * 
	 * @author diff_i@hotmail.com
	 * @param date
	 * @return java.util.Date
	 */
	public static final Date getFirstDayOfMonth(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, 1);
		return cal.getTime();
	}

	/**
	 * Get last day of a month.
	 * 
	 * @author diff_i@hotmail.com
	 * @param date
	 * @return java.util.Date
	 */
	public static final Date getLastDayOfMonth(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// int d=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

		return cal.getTime();
	}

	/**
	 * Get left days of this month
	 * 
	 * @author diff_i@hotmail.com
	 * @param date
	 * @return
	 */
	public static final int getLeftDaysOfMonth(Date date) {
		return getDaysBetween(getLastDayOfMonth(date), date);

	}

	/**
	 * Get left days of the quarter
	 * 
	 * @author diff_i@hotmail.com
	 * @param date
	 * @return
	 */
	public static final int getLeftDaysOfQuarter(Date date) {
		return getDaysBetween(getLastDayOfQuarter(date), date);

	}

	/**
	 * Get left days of year.
	 * 
	 * @author diff_i@hotmail.com
	 * @param date
	 * @return
	 */
	public static final int getLeftDaysOfYear(Date date) {
		return getDaysBetween(getLastDayOfYear(date), date);

	}

	/**
	 * Get first day of quarter
	 * 
	 * @author diff_i@hotmail.com
	 * @param date
	 * @return
	 */
	public static final Date getFirstDayOfQuarter(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH);
		int quarter = month / 3;

		cal.set(Calendar.MONTH, quarter * 3);
		cal.set(Calendar.DATE, 1);
		return cal.getTime();
	}

	/**
	 * Get last day of quarter
	 * 
	 * @author diff_i@hotmail.com
	 * @param date
	 * @return
	 */
	public static final Date getLastDayOfQuarter(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH);
		int quarter = month / 3;

		cal.set(Calendar.MONTH, quarter * 3 + 2);
		cal.set(Calendar.DATE, 1);
		return getLastDayOfMonth(cal.getTime());
	}

	/**
	 * Get first day of this year.
	 * 
	 * @author diff_i@hotmail.com
	 * @param date
	 * @return
	 */
	public static final Date getFirstDayOfYear(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, 1);
		return cal.getTime();
	}

	public static final Date getFirstDayOfYear(int year) {
		return toDate(year + "-01-01", SYSTEM_DATE_FORMAT_PATTERN);
	}

	/**
	 * Get last day of year.
	 * 
	 * @author diff_i@hotmail.com
	 * @param date
	 * @return
	 */
	public static final Date getLastDayOfYear(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MONTH, 11);
		cal.set(Calendar.DATE, 31);
		return cal.getTime();
	}

	public static final Date getLastDayOfYear(int year) {
		return toDate(year + "-12-31", SYSTEM_DATE_FORMAT_PATTERN);
	}

	/**
	 * Get begin of date, Such as 2006-01-01 00:00:00
	 * 
	 * @author diff_i@hotmail.com
	 * @param date
	 * @return
	 */
	public static final Date getBeginTimeOfDay(Date date) {
		return trimToDate(date);
	}

	/**
	 * Get end of date, Such as 2006-01-01 23:59:59
	 * 
	 * @author diff_i@hotmail.com
	 * @param date
	 * @return
	 */
	public static final Date getEndTimeOfDay(Date date) {
		return new Date(trimToDate(addDays(date, 1)).getTime() - 1000);
	}

	/**
	 * Get end Timestamp of day, the precision is ms.
	 * 
	 * @param date
	 * @return
	 */
	public static final Date getEndTimestampOfDay(Date date) {
		return new Date(trimToDate(addDays(date, 1)).getTime() - 1);
	}

	/**
	 * Attach time to a date.
	 * 
	 * @param date
	 * @param hours
	 * @param minutes
	 * @param seconds
	 * @return
	 * @author
	 * @see
	 * @since Jun 21, 2006 10:48:42 AM
	 */
	public static final Date attachTime(Date date, int hours, int minutes, int seconds) {
		Calendar cal = Calendar.getInstance();
		if (date == null) {
			return null;
		}
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, hours);
		cal.set(Calendar.MINUTE, minutes);
		cal.set(Calendar.SECOND, seconds);
		return cal.getTime();
	}

	/**
	 * Attach time to a date.
	 * 
	 * @param date
	 * @param hours
	 * @param minutes
	 * @param seconds
	 * @return
	 * @author
	 * @see
	 * @since Jun 21, 2006 11:00:35 AM
	 */
	public static final Date attachTime(Date date, String hours, String minutes, String seconds) {
		Date dt = null;
		try {
			dt = attachTime(date, isEmpty(hours) ? 0 : Integer.parseInt(trim(hours)), isEmpty(minutes) ? 0 : Integer
					.parseInt(trim(minutes)), isEmpty(seconds) ? 0 : Integer.parseInt(trim(seconds)));
		} catch (Exception e) {
			logger.error("attachTime(...)", e);
		}
		return dt;
	}

	/**
	 * 
	 * Get offset by hours between two timezones
	 * 
	 * @param date
	 * @param fromZone
	 * @param toZone
	 * @return
	 * @author
	 * @see
	 * @since Jul 12, 2006 4:20:05 PM
	 */
	public static final int getOffsetBetweenTimeZone(Date fromDate, TimeZone fromZone, TimeZone toZone) {
		long rawOffset = fromZone.getRawOffset() - toZone.getRawOffset();
		Date dt = new Date((fromDate.getTime() - rawOffset));
		DateFormat formatter = new SimpleDateFormat(SYSTEM_TIMESTAMP_FORMAT_PATTERN);
		formatter.setTimeZone(TimeZone.getDefault());
		int fromZoneOffset = 0;
		String fromZoneOffsetStr = null;
		fromZoneOffset = fromZone.getRawOffset() / 3600000;
		fromZoneOffsetStr = (fromZoneOffset > 0 ? "+" : "") + (fromZone.getRawOffset() / 3600000) + ":00";
		String dateStr = formatter.format(fromDate) + " GMT" + fromZoneOffsetStr;
		long offset = rawOffset;
		Date dt2 = toDate(dateStr, SYSTEM_TIMESTAMPZ_FORMAT_PATTERN);
		if (fromZone.inDaylightTime(dt2)) {
			offset = offset + fromZone.getDSTSavings();
		}
		if (toZone.inDaylightTime(dt2)) {
			offset = offset - toZone.getDSTSavings();
		}
		return (int) (offset / 3600000);
	}

	/**
	 * 
	 * Get real offset by hours relative to GMT
	 * 
	 * @param localDate
	 * @param zone
	 * @return
	 * @author
	 * @see
	 * @since Jul 12, 2006 4:33:27 PM
	 */
	public static final int getLocalOffset(Date localDate, TimeZone zone) {
		long offset = zone.getRawOffset();
		if (zone.inDaylightTime(localDate)) {
			offset = offset + zone.getDSTSavings();
		}
		return (int) (offset / 3600000);
	}

	/**
	 * Convert a java.util.Date to only Date String.
	 * 
	 * @author diff_i@hotmail.com
	 * @param date
	 * @return String, the return is: yyyy-MM-dd
	 */
	public static final String onlyDateString(Date date) {
		DateFormat format = new SimpleDateFormat(SYSTEM_DATE_FORMAT_PATTERN);
		return format.format(date);
	}

	/**
	 * Convert dateString to date without time.
	 * 
	 * @param dateString
	 * @return
	 * @author
	 * @see
	 * @since 2006-9-11 10:18:23
	 */
	public static final Date toDateOnly(String dateString) {
		Date date = null;
		if (dateString == null || dateString.trim().length() == 0) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat(SYSTEM_DATE_FORMAT_PATTERN);
		try {
			date = format.parse(dateString);
		} catch (ParseException e) {
			logger.warn("toDateOnly(String)", e);
		}
		return date;
	}
	public static final Date dateOnly(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	
	public static final Date dateTomorrow(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(date.getTime()+86400000L));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	/**
	 * get year of the date.
	 * 
	 * @param date
	 * @return
	 * @author
	 */
	public static final int getYear(Date date) {
		if (date == null)
			return 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		return year;
	}
	public static final int getYear() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int year = cal.get(Calendar.YEAR);
		return year;
	}
	/**
	 * get the natural month of the date.
	 * 
	 * @param date
	 * @return
	 * @author
	 */
	public static final int getMonth(Date date) {
		if (date == null)
			return 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH);
		return month + 1;
	}
	public static final int getMonth() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int month = cal.get(Calendar.MONTH);
		return month + 1;
	}
	/**
	 * get month day of the date.
	 * 
	 * @param date
	 * @return
	 * @author
	 */
	public static final int getDay(Date date) {
		if (date == null)
			return 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		return day;
	}
	public static final int getDay() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int day = cal.get(Calendar.DAY_OF_MONTH);
		return day;
	}
	public static final int getDay2(Date date) {
		if (date == null)
			return 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_YEAR);
		return day;
	}
	public static final int getDay2() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int day = cal.get(Calendar.DAY_OF_YEAR);
		return day;
	}
	/**
	 * get hour of the date.
	 * 
	 * @param date
	 * @return
	 * @author
	 */
	public static final int getHour(Date date) {
		if (date == null)
			return 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		return hour;
	}
	public static final int getHour() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		return hour;
	}
	/**
	 * get the minute of the date.
	 * 
	 * @param date
	 * @return
	 * @author
	 */
	public static final int getMinute(Date date) {
		if (date == null)
			return 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int minute = cal.get(Calendar.MINUTE);
		return minute;
	}
	public static final int getMinute() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int minute = cal.get(Calendar.MINUTE);
		return minute;
	}
	/**
	 * get second of the date.
	 * 
	 * @param date
	 * @return
	 * @author
	 */
	public static final int getSecond(Date date) {
		if (date == null)
			return 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int second = cal.get(Calendar.SECOND);
		return second;
	}
	public static final int getSecond() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int second = cal.get(Calendar.SECOND);
		return second;
	}
	/**
	 * get millisecond of the date.
	 * 
	 * @param date
	 * @return
	 * @author
	 */
	public static final int getMilliSecond(Date date) {
		if (date == null)
			return 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int milliSecond = cal.get(Calendar.MILLISECOND);
		return milliSecond;
	}
	public static final int getMilliSecond() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int milliSecond = cal.get(Calendar.MILLISECOND);
		return milliSecond;
	}
	/**
	 * convert date string to date.
	 * 
	 * @param dateString
	 * @return
	 * @author
	 * @since 2006-9-11 9:48:12
	 * 
	 * <pre>
	 * Date dt=toDate(&quot;2006-9-11 9:48:12&quot;);
	 * System.out.println(dt);
	 * output: 2006-9-11 9:48:12
	 * </pre>
	 */
	public static final Date toDate(String dateString) {
		Date date = null;
		if (dateString == null || dateString.trim().length() == 0) {
			return null;
		}
		DateFormat format = FormatManager.getDefaultTimestampFormat();
		try {
			date = format.parse(dateString);
		} catch (ParseException e) {
			logger.error("toDate(String) parse error", e);
		}
		return date;
	}

	/**
	 * Method toShortDate.
	 * 
	 * @author diff_i@hotmail.com
	 * @param dateStr,Format:yyyy-MM-dd
	 * @return Date
	 * @author USCHET
	 * 
	 * <pre>
	 * For example:
	 * String dtString = &quot;2005-12-19&quot;
	 * java.util.Date date1 = DateUtil.toShortDate(dtString);
	 * </pre>
	 */
	public static final Date toShortDate(String dateString) {
		Date date = null;
		if (dateString == null || dateString.trim().length() == 0) {
			return null;
		}
		DateFormat format = FormatManager.getDefaultDateFormat();
		try {
			date = format.parse(dateString);
		} catch (ParseException e) {
			logger.warn("toShortDate(String) parse error", e);
		}
		return date;
	}

	/**
	 * 
	 * @param dateString
	 * @return
	 * @author
	 * 
	 * <pre>
	 * Date dt=toLongDate(&quot;2006-9-11 09:48:12.345&quot;);
	 * System.out.println(dt);
	 * output: 2006-9-11 9:48:12.234
	 * </pre>
	 */
	public static final Date toLongDate(String dateString) {
		Date date = null;
		if (dateString == null || dateString.trim().length() == 0) {
			return null;
		}
		DateFormat format = FormatManager.getDateFormat(SYSTEM_TIMESTAMP_FORMAT_PATTERN);
		try {
			date = format.parse(dateString);
		} catch (ParseException e) {
			logger.warn("toLongDate(String) parse error", e);
		}
		return date;
	}

	/**
	 * Convert dateString to date according to LongZ format.
	 * 
	 * @param dateString
	 * @return
	 * @see toLongZString
	 * 
	 * <pre>
	 * String dateString = &quot;2006-9-18 12:58:23.223 +0700&quot;;
	 * 
	 * Date dt = toLongZDate(dateString);
	 * </pre>
	 */
	public static final Date toLongZDate(String dateString) {
		Date date = null;
		if (dateString == null || dateString.trim().length() == 0) {
			return null;
		}
		DateFormat format = FormatManager.getDateFormat(SYSTEM_LONGTIMESTAMPZ_FORMAT_PATTERN);
		try {
			date = format.parse(dateString);
		} catch (ParseException e) {
			logger.warn("toLongZDate(String) parse error", e);
		}
		return date;
	}

	/**
	 * Convert a java.util.Date to Short Date String.
	 * 
	 * @author diff_i@hotmail.com
	 * @param date
	 * @return String, the return is: yyyy-MM-dd
	 * 
	 * <pre>
	 * Date dt = new Date();
	 * String s = toShortString(dt);
	 * output:2006-08-19
	 * </pre>
	 */
	public static final String toShortString(Date date) {
		if (date == null) {
			return null;
		}
		DateFormat dateFormat = FormatManager.getDefaultDateFormat();
		return dateFormat.format(date);
	}

	public static final String toShortString(Date date, String defaultValue) {
		if (date == null) {
			return defaultValue;
		}
		DateFormat dateFormat = FormatManager.getDefaultDateFormat();
		return dateFormat.format(date);
	}

	public static final String toLocaleShortString(Date date, String localeCode) {
		DateFormat dateFormat = FormatManager.getLocaleDateFormat(localeCode);
		return dateFormat.format(date);
	}

	public static final String toLocaleMMDDString(Date date, String localeCode) {
		if (date == null) {
			return "";
		}
		DateFormat dateFormat = FormatManager.getLocaleMMDDFormat(localeCode);
		return dateFormat.format(date);
	}

	/**
	 * tyf
	 * 
	 * @return
	 */
	public static long getLongTime() {
		return System.currentTimeMillis();
	}

	public static String getHourMinute(Date date) {
		if (date == null) {
			return null;
		}
		return HOUR_MINUTE_FORMATTER.format(date);
	}

	/**
	 * convert time with unit to mills.
	 * 
	 * @param time
	 * @return
	 */
	public static int asDays(String time) {
		if (time == null || time.trim().length() == 0) {
			return 0;
		}
		String tm = time.trim();
		double amt = Double.parseDouble(tm.substring(0, tm.length() - 1));
		int days = 0;
		String unit = tm.substring(tm.length() - 1, tm.length());
		if (isTimeUnit(unit)) {
			if ("y".equals(unit)) {
				days = (int) (amt * 365 * 30);
			} else if ("M".equals(unit)) {
				days = (int) (30 * amt);
			} else if ("d".equals(unit)) {
				days = (int) amt;
			} else if ("H".equals(unit)) {
				days = (int) (amt / 24);
			} else if ("m".equals(unit)) {
				days = (int) (amt / (24 * 60));
			} else if ("s".equals(unit)) {
				days = (int) (amt / (24 * 60 * 60));
			} else if ("S".equals(unit)) {
				days = (int) amt / (24 * 60 * 60 * 1000);
			} else {
				days = (int) amt;
			}
		} else {
			days = (int) amt;

		}
		return days;

	}
	public static int asHours(String time) {
		if (time == null || time.trim().length() == 0) {
			return 0;
		}
		String tm = time.trim();
		double amt = Double.parseDouble(tm.substring(0, tm.length() - 1));
		int days = 0;
		String unit = tm.substring(tm.length() - 1, tm.length());
		if (isTimeUnit(unit)) {
			if ("y".equals(unit)) {
				days = (int) (amt * 365 * 30 * 24);
			} else if ("M".equals(unit)) {
				days = (int) (30 * amt * 24);
			} else if ("d".equals(unit)) {
				days = (int) (amt*24);
			} else if ("H".equals(unit)) {
				days = (int) (amt);
			} else if ("m".equals(unit)) {
				days = (int) (amt / (60));
			} else if ("s".equals(unit)) {
				days = (int) (amt / (60 * 60));
			} else if ("S".equals(unit)) {
				days = (int) amt / (60 * 60 * 1000);
			} else {
				days = (int) amt;
			}
		} else {
			days = (int) amt;

		}
		return days;

	}
	public static int asMinutes(String time) {
		if (time == null || time.trim().length() == 0) {
			return 0;
		}
		String tm = time.trim();
		double amt = Double.parseDouble(tm.substring(0, tm.length() - 1));
		int days = 0;
		String unit = tm.substring(tm.length() - 1, tm.length());
		if (isTimeUnit(unit)) {
			if ("y".equals(unit)) {
				days = (int) (amt * 365 * 30 * 24*60);
			} else if ("M".equals(unit)) {
				days = (int) (30 * amt * 24*60);
			} else if ("d".equals(unit)) {
				days = (int) (amt*24*60);
			} else if ("H".equals(unit)) {
				days = (int) (amt*60);
			} else if ("m".equals(unit)) {
				days = (int) (amt);
			} else if ("s".equals(unit)) {
				days = (int) (amt / 60);
			} else if ("S".equals(unit)) {
				days = (int) amt / (60 * 1000);
			} else {
				days = (int) amt;
			}
		} else {
			days = (int) amt;

		}
		return days;

	}
	public static long asMills(String time) {
		if (time == null || time.trim().length() == 0) {
			return 0L;
		}
		String tm = time.trim();
		double amt = Double.parseDouble(tm.substring(0, tm.length() - 1));
		long millis = 0L;
		String unit = tm.substring(tm.length() - 1, tm.length());
		if (isTimeUnit(unit)) {
			if ("y".equals(unit)) {
				millis = (long) (amt * 365L * 30L * 24L * 60L * 60L * 1000L);
			} else if ("M".equals(unit)) {
				millis = (long) (30L * 24L * 60L * 60L * 1000L * amt);
			} else if ("d".equals(unit)) {
				millis = (long) (24L * 60L * 60L * 1000L * amt);
			} else if ("H".equals(unit)) {
				millis = (long) (60L * 60L * 1000L * amt);
			} else if ("m".equals(unit)) {
				millis = (long) (amt * 60L * 1000L);
			} else if ("s".equals(unit)) {
				millis = (long) (amt * 1000L);
			} else if ("S".equals(unit)) {
				millis = (long) amt;
			} else {
				millis = (long) amt;
			}
		} else {
			millis = (long) amt;

		}
		return millis;

	}

	public static String duration(long millis){
		long theMillis = millis;
		long baseDay = 24 * 60 * 60 *1000;
		long baseHour = 60 * 60 *1000;
		long baseMinute = 60 *1000;
		long baseSecond = 1000;
		StringBuffer buf = new StringBuffer();
		int day =(int)(theMillis /baseDay);
		
		if( day>0){
			buf.append(day).append(" day(s)");
		}
		theMillis -= day*baseDay;

		int hour =(int)(theMillis /baseHour);
		if( hour>0){
			buf.append(",").append(hour).append(" hour(s)");
		}
		theMillis -= hour*baseHour;
		
		int minutes =(int)(theMillis /baseMinute);
		if( minutes>0){
			buf.append(",").append(minutes).append(" minute(s)");
		}
		theMillis -= minutes*baseMinute;
		
		int seconds =(int)(theMillis /baseSecond);
		if( seconds>0){
			buf.append(",").append(seconds).append(" second(s)");
		}
		theMillis -= seconds*baseSecond;
		return buf.toString();
	}

	public static String toChineseString(String date) {
		//20060101101520
		StringBuffer sb = new StringBuffer();
		sb.append(getChineseNumber(Integer.parseInt(date.substring(0, 1))));
		sb.append(getChineseNumber(Integer.parseInt(date.substring(1, 2))));
		sb.append(getChineseNumber(Integer.parseInt(date.substring(2, 3))));
		sb.append(getChineseNumber(Integer.parseInt(date.substring(3, 4))));
		sb.append("年");

		String month1 = date.substring(4, 5);
		if (!"0".equals(month1)) {
			sb.append("ʮ");
		}
		String month2 = date.substring(5, 6);
		if (!"0".equals(month2)) {
			sb.append(getChineseNumber(Integer.parseInt(month2)));
		}
		sb.append("月");

		String day1 = date.substring(6, 7);
		if ("0".equals(day1)) {
			// nothing
		} else if ("1".equals(day1)) {
			sb.append("ʮ");
		} else {
			sb.append(getChineseNumber(Integer.parseInt(day1)));
			sb.append("ʮ");
		}
		String day2 = date.substring(7, 8);
		if (!"0".equals(day2)) {
			sb.append(getChineseNumber(Integer.parseInt(day2)));
		}
		sb.append("日");
		return sb.toString();
	}

	public static String getMiddleDateTime(Date dt) {
		if (dt == null) {
			return "";
		}
		DateFormat dateFormat = FormatManager.getDateFormat("MM-dd HH:mm");
		return dateFormat.format(dt);
	}
	public static Date randomDateBetween(Date begin, Date end){
		if(begin==null || end==null || end.before(begin)){
			return null;
		}
		int minutes = getMinutesBetweenDates(end, begin);
		int randMin = ArithmeticUtil.randomInt(minutes);
		return addMinutes(begin, randMin);
	}

	private static String getChineseNumber(int digital) {
		switch (digital) {
		case 0:
			return "零";
		case 1:
			return "壹";
		case 2:
			return "贰";
		case 3:
			return "叁";
		case 4:
			return "肆";
		case 5:
			return "伍";
		case 6:
			return "陆";
		case 7:
			return "柒";
		case 8:
			return "捌";
		case 9:
			return "玖";
		}
		return "";
	}
	
	public static String getAMTDateToString(Date date){
		if(date == null){
			return "";
		}
		Date date0 = DateUtil.toDate(DateUtil.toString(date, "yyyy-MM-dd"),"yyyy-MM-dd");
		Date date1 = DateUtil.getGMTDisplayTimestamp(date0);
		String date2 = date1.toGMTString();
		String date3 = date2.substring(0,11);
		return date3;
	}
	
	private static final boolean isTimeUnit(String u) {
		return ("yMdHmsS".indexOf(u) != -1);
	}

	private static final boolean isEmpty(String str) {
		return (str == null || str.trim().length() == 0) ? true : false;
	}

	private static final String trim(String str) {
		return (str == null) ? null : str.trim();
	}
	/**
	 * test
	 * 
	 * @param argv
	 */
	public static void main(String[] argv) throws ParseException {

	}
}

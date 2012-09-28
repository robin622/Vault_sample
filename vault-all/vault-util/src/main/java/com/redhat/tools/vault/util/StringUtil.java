package com.redhat.tools.vault.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.jboss.logging.Logger;
/**
 * 
 * @author wguo@redhat.com
 *
 */
public class StringUtil {
	/**
	 * logger
	 */
    protected static final Logger logger = Logger.getLogger(StringUtil.class);

	/**
	 * do not allow to create instance.
	 */
	private StringUtil() {
	}
	public static final String LOGIC_YES = "YES";

	public static final String LOGIC_NO = "NO";

	public static final String LOGIC_YES_ABBR = "Y";

	public static final String LOGIC_NO_ABBR = "N";

	public static final String LOGIC_TRUE = "TRUE";

	public static final String LOGIC_FALSE = "FALSE";

	public static final String DEFAULT_ATTR_ASSIGN_LOGIC = "=";

	public static final String DEFAULT_OPTION_DELIMITER = "=";
	
	public static final String DEFAULT_GENERAL_SPLITOR = ",|;|\\s+";

	private final static String[] hex = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0A", "0B", "0C",
			"0D", "0E", "0F", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "1A", "1B", "1C", "1D", "1E",
			"1F", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "2A", "2B", "2C", "2D", "2E", "2F", "30",
			"31", "32", "33", "34", "35", "36", "37", "38", "39", "3A", "3B", "3C", "3D", "3E", "3F", "40", "41", "42",
			"43", "44", "45", "46", "47", "48", "49", "4A", "4B", "4C", "4D", "4E", "4F", "50", "51", "52", "53", "54",
			"55", "56", "57", "58", "59", "5A", "5B", "5C", "5D", "5E", "5F", "60", "61", "62", "63", "64", "65", "66",
			"67", "68", "69", "6A", "6B", "6C", "6D", "6E", "6F", "70", "71", "72", "73", "74", "75", "76", "77", "78",
			"79", "7A", "7B", "7C", "7D", "7E", "7F", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "8A",
			"8B", "8C", "8D", "8E", "8F", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "9A", "9B", "9C",
			"9D", "9E", "9F", "A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "AA", "AB", "AC", "AD", "AE",
			"AF", "B0", "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9", "BA", "BB", "BC", "BD", "BE", "BF", "C0",
			"C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "CA", "CB", "CC", "CD", "CE", "CF", "D0", "D1", "D2",
			"D3", "D4", "D5", "D6", "D7", "D8", "D9", "DA", "DB", "DC", "DD", "DE", "DF", "E0", "E1", "E2", "E3", "E4",
			"E5", "E6", "E7", "E8", "E9", "EA", "EB", "EC", "ED", "EE", "EF", "F0", "F1", "F2", "F3", "F4", "F5", "F6",
			"F7", "F8", "F9", "FA", "FB", "FC", "FD", "FE", "FF" };

	private final static byte[] val = { 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x00,
			0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x0A, 0x0B,
			0x0C, 0x0D, 0x0E, 0x0F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
			0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F };

	private static final char[] QUOTE_ENCODE = "&quot;".toCharArray();

	private static final char[] AMP_ENCODE = "&amp;".toCharArray();

	private static final char[] LT_ENCODE = "&lt;".toCharArray();

	private static final char[] GT_ENCODE = "&gt;".toCharArray();

	/**
	 * Array of numbers and letters of mixed case. Numbers appear in the list
	 * twice so that there is a more equal chance that a number will be picked.
	 * We can use the array to get a random number or letter by picking a random
	 * array index.
	 */
	private static char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz"
			+ "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();

	private static Random randGen = new Random();
	
	/**
	 * Returns a random String of numbers and letters (lower and upper case) of
	 * the specified length. The method uses the Random class that is built-in
	 * to Java which is suitable for low to medium grade security uses. This
	 * means that the output is only pseudo random, i.e., each number is
	 * mathematically generated so is not truly random.
	 * <p>
	 * 
	 * The specified length must be at least one. If not, the method will return
	 * null.
	 * 
	 * @param length
	 *            the desired length of the random String to return.
	 * @return a random String of numbers and letters of the specified length.
	 */
	public static final String randomString(int length) {
		if (length < 1) {
			return null;
		}
		// Create a char buffer to put random letters and numbers in.
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
		return new String(randBuffer);
	}

	/**
	 * Split string to array. the empty string between two delimitors will not
	 * be ignored.
	 * 
	 * @param str
	 * @param delimitor
	 * @return
	 * @author
	 * @see
	 * @since 2007-1-7 14:19:47
	 */
	public static String[] splitStringToArray(String str, String delimitor) {
		if (str == null || str.length() == 0 || delimitor == null || delimitor.length() == 0)
			return null;
		return str.split(delimitor);
	}

	/**
	 * 
	 * @param str
	 * @return
	 * @sample
	 *  String s="jiang;diff,cls,ok nn";
		String[] ar = s.split("[;, ]");
		System.out.println(java.util.Arrays.asList(ar));
	 */
	public static String[] stringToArray2(String str) {
		if (str == null || str.length() == 0){
			return (null);
		}
		return str.split(DEFAULT_GENERAL_SPLITOR);
	}
	public static List<String> stringToList2(String str) {
		if (str == null || str.length() == 0){
			return (null);
		}
		return Arrays.asList(str.split(DEFAULT_GENERAL_SPLITOR));
	}
	public static String[] stringToArray2(String str, String splitor) {
		if (str == null || str.length() == 0) {
			return (null);
		}
		String sp = splitor;
		if (sp == null || sp.length() == 0) {
			sp = DEFAULT_GENERAL_SPLITOR;
		}
		return str.split(sp);
	}
	public static List<String> stringToList2(String str, String splitor) {
		if (str == null || str.length() == 0){
			return (null);
		}
		String sp = splitor;
		if (sp == null || sp.length() == 0) {
			sp = DEFAULT_GENERAL_SPLITOR;
		}		
		return Arrays.asList(str.split(sp));
	}
	/**
	 * construct a string array from a comma-separated item string the empty
	 * string between two delimitors will be ignored.
	 * 
	 * @param str
	 * @param delimitor
	 * @return array of String
	 * @author
	 * @see
	 * @since May 29, 2006 6:27:22 PM
	 *        <p>
	 *        usage
	 *        </p>
	 * 
	 * <pre>
	 * String[] s=csvStringToArray(&quot;Beijing/NewYork/Tokyo/Landon&quot;,&quot;/&quot;);&lt;pre&gt;
	 * 
	 */
	public static String[] stringToArray(String str, String delimitor) {
		List<String> list = stringToList(str, delimitor);
		if(list == null){
			return null;
		}
		return ((String[]) list.toArray(new String[list.size()]));
	}
	/**
	 * usage: Pattern pattern=Pattern.compile(",");
	 *   String[] ss = stringToArray(str,pattern);
	 * @param str
	 * @param pattern
	 * @return
	 */
	public static final String[] stringToArray(String str, Pattern pattern){
		if (str == null || str.length() == 0)
			return (null);
		List<Integer> list = new ArrayList<Integer>();
		String []items=pattern.split(str,0);
		return items;
	}
	public static List<String> stringToList(String str, String delimitor) {
		if (str == null || str.length() == 0)
			return (null);
		List<String> list = new ArrayList<String>();
		if (str.indexOf(delimitor) < 0) {
			list.add(str);
		} else {
			StringTokenizer tok = new StringTokenizer(str, delimitor);
			while (tok.hasMoreTokens()) {
				list.add(tok.nextToken().trim());
			}
		}
		return list;
	}
	/**
	 * usage: Pattern pattern=Pattern.compile(",");
	 *   List list = stringToList(str,pattern);
	 * @param str
	 * @param pattern
	 * @return
	 */
	public static final List<String> stringToList(String str, Pattern pattern){
		if (str == null || str.length() == 0)
			return (null);
		List<Integer> list = new ArrayList<Integer>();
		String []items=pattern.split(str,0);
		return Arrays.asList(items);
	}
	/**
	 * System.out.println(keywordsToList("jiangbing \"li haiyan\" zhangwei"));
	 * @param str
	 * @return
	 */
	public static List<String> keywordsToList(String str) {
		if (str == null || str.length() == 0)
			return (null);
		char[] chs = str.toCharArray();
		boolean qbegin = false;
		StringBuffer buf = new StringBuffer();
		List<String> list =new ArrayList<String>();
		for(char ch : chs){
			if(ch=='"'){
				qbegin=!qbegin;
				continue;
			}
			if(ch==' '&&!qbegin){
				if(buf.length()>0){
					list.add(buf.toString());
				}
				buf = new StringBuffer();
			} else {
				buf.append(ch);
			}
		}
		list.add(buf.toString());
		return list;
	}
	public static Set<String> stringToSet(String str, String delimitor) {
		if (str == null || str.length() == 0)
			return (null);
		Set<String> set = new HashSet<String>();
		if (str.indexOf(delimitor) < 0) {
			set.add(str);
		} else {
			StringTokenizer tok = new StringTokenizer(str, delimitor);
			while (tok.hasMoreTokens()) {
				set.add(tok.nextToken());
			}
		}
		return set;
	}
	public static Map<String, String> stringToMap(String str, String delimitor) {
		if (str == null || str.length() == 0)
			return (null);
		Map<String, String> map = new HashMap<String, String>();
		if (str.indexOf(delimitor) < 0) {
			map.put(str,null);
		} else {
			StringTokenizer tok = new StringTokenizer(str, delimitor);
			while (tok.hasMoreTokens()) {
				map.put(tok.nextToken(),null);
			}
		}
		return map;
	}	
	/**
	 * construct a string array from a comma-separated item string and trim the
	 * qualifier
	 * 
	 * @param str
	 * @param delimitor
	 * @param qualifier
	 * @return
	 * @author
	 * @see
	 * @since 2007-1-7 15:15:53
	 */
	public static String[] stringToArray(String str, String delimitor, String qualifier) {
		if (str == null || str.length() == 0)
			return (null);
		List list = new ArrayList();
		if (str.indexOf(delimitor) < 0) {
			list.add(StringUtil.exTrim(str, qualifier));
		} else {
			StringTokenizer tok = new StringTokenizer(str, delimitor);
			while (tok.hasMoreTokens()) {
				list.add(StringUtil.exTrim(tok.nextToken(), qualifier));
			}
		}
		return ((String[]) list.toArray(new String[list.size()]));
	}

	/**
	 * convert a string array to a delimited item stringBuffer
	 * 
	 * @param aryStr
	 *            the string array
	 * @param delimitor
	 *            the delimitor
	 * @return a string
	 * @usage StringBuffer sb=arrayToString(columns[],"/");
	 *        Result:PRODCUT_ID/PRODUCT_NAME/DATA_STATUS
	 */
	public static StringBuffer arrayToString(String[] aryStr, String delimitor) {
		if (aryStr == null)
			return (null);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < aryStr.length; ++i) {
			if (delimitor != null && i > 0)
				sb.append(delimitor);
			sb.append(aryStr[i]);
		}
		return (sb);
	}

	public static String listToString(List list, String delimitor) {
		if (list == null){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); ++i) {
			if (delimitor != null && i > 0)
				sb.append(delimitor);
			sb.append(list.get(i));
		}
		return (sb.toString());
	}
	/**
	 * set to string
	 * @param set
	 * @param delimitor
	 * @return
	 */
	public static String setToString(Set set, String delimitor) {
			if (set == null){
				return null;
			}
		StringBuffer sb = new StringBuffer();
		Iterator it = set.iterator();
		int loopNum = 0;
		for (;it.hasNext();) {
			String line = (String)it.next();
			if (delimitor != null && loopNum > 0)
				sb.append(delimitor);
			sb.append(line);
			loopNum++;
		}
		return (sb.toString());
	}
	/**
	 * Convert String array to SQL set,such as 'A','B','D'
	 * 
	 * @param aryStr
	 * @return
	 */
	public static String concatInList(String[] array) {
		if (array == null || array.length==0){
			throw new SystemException("util.string.error.argument-is-empty");
		}
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < array.length; ++i) {
			if (i > 0) {
				buf.append(",");
			}
			buf.append("'");
			buf.append(array[i]);
			buf.append("'");
		}
		return (buf.toString());
	}

	/**
	 * Convert char array to SQL set,such as 'A','B','D'
	 * 
	 * @param aryStr:char
	 *            array
	 * @return
	 */
	public static String concatInList(char[] array) {
		if (array == null || array.length == 0){
			throw new SystemException("util.string.error.argument-is-empty");
		}
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < array.length; ++i) {
			if (i > 0) {
				buf.append(",");
			}
			buf.append("'");
			buf.append(array[i]);
			buf.append("'");
		}
		return (buf.toString());
	}

	/**
	 * Convert List array to SQL set,such as 'A','B','D'
	 * 
	 * @param aryStr:
	 *            List of String Type
	 * @return
	 */
	public static String concatInList(List list, boolean quote) {
		if (list == null || list.size() == 0) {
			throw new SystemException("util.string.error.argument-is-empty");
		}
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < list.size(); ++i) {
			if (i > 0) {
				buf.append(",");
			}
			if (quote) {
				buf.append("'");
			}
			buf.append((String) list.get(i));
			if (quote) {
				buf.append("'");
			}
		}
		return (buf.toString());
	}
	public static String concatInList(List list){
		return concatInList(list, true);
	}
	/**
	 * Convert String array to SQL set with parentheses,such as ('A','B','D')
	 * 
	 * @param aryStr:String
	 *            array
	 * @return
	 */
	public static String concatInList2(String[] array) {
		if (array == null || array.length == 0)
			return (null);
		StringBuffer buf = new StringBuffer();
		return (buf.append("(").append(concatInList(array)).append(")")).toString();
	}

	/**
	 * Convert char array to SQL set with parentheses,such as ('A','B','D')
	 * 
	 * @param aryStr:char
	 *            array
	 * @return
	 */
	public static String concatInList2(char[] array) {
		if (array == null || array.length == 0){
			throw new SystemException("util.string.error.argument-is-empty");
		}
		StringBuffer buf = new StringBuffer();
		return buf.append("(").append(concatInList(array)).append(")").toString();
	}
	/**
	 * 
	 * @param set
	 * @param quote, indicate whether the list set include quotation.
	 * @return
	 */
	public static String concatInList(Set set, boolean quote) {
		if (set == null || set.size() == 0) {
			throw new SystemException("util.string.error.argument-is-empty");
		}
		StringBuffer buf = new StringBuffer();
		Iterator it = set.iterator();
		for (int i = 0; it.hasNext(); i++) {
			if (i > 0) {
				buf.append(",");
			}
			if (quote) {
				buf.append("'");
			}
			buf.append(it.next());
			if (quote) {
				buf.append("'");
			}
		}
		return (buf.toString());
	}

	public static String concatInList(Set set) {
		return concatInList(set, true);
	}	
	public static String concatInList2(Set set, boolean quote) {
		if (set == null || set.size() == 0) {
			throw new SystemException("util.string.error.argument-is-empty");
		}
		StringBuffer buf = new StringBuffer();
		return buf.append("(").append(concatInList(set, quote)).append(")").toString();
	}
	public static String concatInList2(Set set) {
		return concatInList2(set, true);
	}
	/**
	 * Convert String array to SQL set with parentheses,such as ('A','B','D')
	 * 
	 * @param aryStr
	 * @return
	 */
	public static String concatInList2(List list, boolean quote) {
		if (list == null || list.size() == 0){
			throw new SystemException("util.string.error.argument-is-empty");
		}
		StringBuffer buf = new StringBuffer();
		return buf.append("(").append(concatInList(list, quote)).append(")").toString();
	}
	public static String concatInList2(List list) {
		return concatInList2(list, true);
	}
	/**
	 * Convert String array to SQL parameters,such as ?,?,?
	 * 
	 * @param array
	 * @return
	 * 
	 * <pre>
	 * for example:
	 * String[] s =new String[]{&quot;C&quot;,&quot;V&quot;,&quot;X&quot;,&quot;D&quot;};
	 * String result=concatSQLBindings(s);
	 * output is: ?,?,?,?
	 * </pre>
	 */
	public static String concatValueBindings(String[] array) {
		if (array == null || array.length == 0)
			return (null);
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < array.length; ++i) {
			if (i > 0) {
				buf.append(",");
			}
			buf.append("?");

		}
		return (buf.toString());
	}

	/**
	 * Convert String List to SQL parameters,such as ?,?,?
	 * 
	 * @param array
	 * @return
	 * 
	 * <pre>
	 * for example:
	 * String[] s =new String[]{&quot;C&quot;,&quot;V&quot;,&quot;X&quot;,&quot;D&quot;};
	 * List list=Arrays.asList(s);
	 * String result=arrayToSQLBundings(list);
	 * output is: ?,?,?,?
	 * </pre>
	 */
	public static String concatValueBindings(List list) {
		if (list == null || list.size() == 0)
			return (null);
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < list.size(); ++i) {
			if (i > 0) {
				buf.append(",");
			}
			buf.append("?");
		}
		return (buf.toString());
	}

	/**
	 * Convert String array to SQL parameters,such as (?,?,?)
	 * 
	 * @param array
	 * @return
	 * 
	 * <pre>
	 * for example:
	 * String[] s =new String[]{&quot;C&quot;,&quot;V&quot;,&quot;X&quot;,&quot;D&quot;};
	 * String result=arrayToSQLParamsWithParentheses(s);
	 * output is: (?,?,?,?)
	 * </pre>
	 */
	public static String concatValueBindings2(String[] array) {
		if (array == null || array.length == 0)
			return (null);
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < array.length; ++i) {
			if (i > 0) {
				buf.append(",");
			}
			buf.append("?");

		}
		return (buf.append("(").append(concatValueBindings(array)).append(")").toString());
	}

	/**
	 * Convert String List to SQL parameters,such as (?,?,?)
	 * 
	 * @param list
	 * @return
	 */
	public static String concatValueBindings2(List list) {
		if (list == null || list.size() == 0)
			return (null);
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < list.size(); ++i) {
			if (i > 0) {
				buf.append(",");
			}
			buf.append("?");

		}
		return (buf.append("(").append(concatValueBindings(list)).append(")").toString());
	}
	public static String concatUpdateBindings(List list) {
		if (list == null || list.size() == 0)
			return (null);
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < list.size(); ++i) {
			if (i > 0) {
				buf.append(",");
			}
			buf.append(list.get(i)).append("=?");
		}
		return (buf.toString());
	}
	public static String concatWhereBindings(List list) {
		if (list == null || list.size() == 0)
			return (null);
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < list.size(); ++i) {
			if (i > 0) {
				buf.append(" and ");
			}
			buf.append(list.get(i)).append("=?");
		}
		return (buf.toString());
	}
	/**
	 * get duplicate String according to patString
	 * 
	 * @param patString:pattern
	 *            String
	 * @param dupTimes:duplicate
	 *            times
	 * @param delimitor
	 * @return
	 *            <p>
	 *            usage:
	 *            </p>
	 * 
	 * <pre>
	 * StringBuffer sb=duplicateString(&quot;?&quot;,6,&quot;,&quot;);
	 * Result:?,?,?,?,?,?
	 * StringBuffer sb=duplicateString(&quot;*&quot;,5,null);
	 * Result:*****
	 * </pre>
	 */
	public static String duplicateString(String patString, int dupTimes, String delimitor) {
		if (patString == null) {
			return null;
		}
		if (dupTimes <= 0) {
			throw new IllegalArgumentException("Illegal duplicate times: " + dupTimes);
		}

		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < dupTimes; i++) {
			if (delimitor != null && i > 0) {
				buf.append(delimitor);
			}
			buf.append(patString);
		}
		return buf.toString();
	}
	public static String repeatString(String patString, int repTimes) {
		return repeatString(patString, repTimes,"");
	}
	public static String repeatString(String patString, int dupTimes, String delimitor) {
		if (patString == null) {
			return null;
		}
		if (dupTimes <= 0) {
			throw new IllegalArgumentException("Illegal duplicate times: " + dupTimes);
		}

		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < dupTimes; i++) {
			if (delimitor != null && i > 0) {
				buf.append(delimitor);
			}
			buf.append(patString);
		}
		return buf.toString();
	}
	/**
	 * Left pad the string for example:leftPad("98",4,"0"),the result is:0098
	 * 
	 * @param srcStr
	 * @param padLen
	 * @param padStr
	 * @return
	 */
	public static String leftPad(String srcStr, int padLen, String padStr) {
		if (srcStr == null || padStr == null || padStr.length() == 0) {
			return srcStr;
		}
		StringBuffer padString = new StringBuffer();
		if (srcStr.length() >= padLen) {
			return srcStr.substring(0, padLen);
		}
		int padLength = 0;
		padLength = padLen - srcStr.length();
		for (int i = 0; i < padLength; i++) {
			padString.append(padStr);
			if (padString.length() > padLength) {
				break;
			}
		}
		srcStr = padString.substring(0, padLength).toString() + srcStr;
		return srcStr;
	}

	/**
	 * left pad for int;
	 * 
	 * @param ii
	 * @param padLen
	 * @return for example:leftPad(98,4),the result is:0098
	 */
	public static String leftPad(int ii, int padLen) {
		StringBuffer padString = new StringBuffer();
		String str = String.valueOf(ii);

		int padLength = padLen - str.length();
		for (int i = 0; i < padLength; i++) {
			padString.append("0");
		}
		return padString.toString() + str;
	}

	/**
	 * Right pad the string for example:rightPad("98",4,"0"),the result is:9800
	 * rightPad("",8,"*"),the result is:********
	 * 
	 * @param srcStr
	 * @param padLen
	 * @param padStr
	 * @return
	 */
	public static String rightPad(String srcStr, int padLen, String padStr) {
		if (srcStr == null || padStr == null || padStr.length() == 0) {
			return srcStr;
		}
		StringBuffer padString = new StringBuffer();
		if (srcStr.length() >= padLen) {
			return srcStr.substring(0, padLen);
		}
		int padLength = padLen - srcStr.length();
		for (int i = 0; i < padLength; i++) {
			padString.append(padStr);
			if (padString.length() > padLength) {
				break;
			}
		}
		srcStr += padString.substring(0, padLength).toString();
		return srcStr;
	}

	/**
	 * trim left string
	 * 
	 * @param srcString
	 * @param patString
	 * @return
	 *            <p>
	 *            usage
	 *            </p>
	 *            leftTrim("ok,ok,this is a good main...","ok,");
	 */
	public static String leftTrim(String srcString, String patString) {
		if (srcString == null || srcString.length() == 0 || patString == null || patString.length() == 0) {
			return srcString;
		}
		String str = null;
		str = srcString;
		while (str.startsWith(patString)) {
			str = str.substring(patString.length());
		}
		return str;
	}

	/**
	 * trim right string
	 * 
	 * @param srcString
	 * @param patString
	 * @return
	 *            <p>
	 *            usage
	 *            <p>
	 *            rightTrim("this is a good main...",".");
	 * 
	 */
	public static String rightTrim(String srcString, String patString) {
		if (srcString == null || srcString.length() == 0 || patString == null || patString.length() == 0) {
			return srcString;
		}
		String str = null;
		str = srcString;
		while (str.endsWith(patString)) {
			str = str.substring(0, str.length() - patString.length());
		}
		return str;
	}

	/**
	 * trim head and tail string
	 * 
	 * @param srcString
	 * @param patString
	 * @return
	 */
	public static String trim(String srcString, String patString) {
		return rightTrim(leftTrim(srcString, patString), patString);
	}

	/**
	 * Remove all blank space in a string, attention:it will also eliminate the
	 * blank space in the middle of letters.
	 * 
	 * @param srcString
	 * @return
	 * 
	 * <pre>
	 * String s=&quot;this is a good man.&quot;;
	 * String ss=StrintUtil.trimAll(s);
	 * output: thisisagoodman.
	 * </pre>
	 */
	public static String trimAll(String srcString) {
		if (srcString == null) {
			return null;
		}
		StringBuffer buf = new StringBuffer(srcString);// a bug.
		for (int i = 0; i < buf.length(); i++) {
			if (buf.charAt(i) == ' ')
				buf.deleteCharAt(i);
		}
		return buf.toString();
	}

	/**
	 * trim left string including space
	 * 
	 * @param srcString
	 * @param patString
	 * @return
	 * 
	 * <pre>
	 * leftTrim(&quot;ok,ok,this is a good main...&quot;, &quot;ok,&quot;);
	 * </pre>
	 */
	public static String exLeftTrim(String srcString, String patString) {
		if (srcString == null || srcString.length() == 0 || patString == null || patString.length() == 0) {
			return srcString;
		}
		String str = null;
		str = srcString.trim();
		while (str.startsWith(patString)) {
			str = str.substring(patString.length()).trim();
		}
		return str;
	}

	/**
	 * trim right string including space
	 * 
	 * @param srcString
	 * @param patString
	 * @return
	 * 
	 * <pre>
	 * exRightTrim(&quot;this is a good main... &quot;, &quot;.&quot;);
	 * </pre>
	 * 
	 */
	public static String exRightTrim(String srcString, String patString) {
		if (srcString == null || srcString.length() == 0 || patString == null || patString.length() == 0) {
			return srcString;
		}
		String str = null;
		str = srcString.trim();
		while (str.endsWith(patString)) {
			str = str.substring(0, str.length() - patString.length()).trim();
		}
		return str;
	}

	/**
	 * trim head and tail string
	 * 
	 * @param srcString
	 * @param patString
	 * @return
	 */
	public static String exTrim(String srcString, String patString) {
		return exRightTrim(exLeftTrim(srcString, patString), patString);
	}

	/**
	 * get char number of a String.
	 * 
	 * @param src
	 * @param ch
	 * @return
	 *            <p>
	 *            usage
	 *            <p>
	 * 
	 * <pre>
	 * int num=getCharNumOfString(&quot;select * from mytable where deparment=? and birthday=? and idcard like ?&quot;,&quot;?&quot;);
	 * Result:3
	 * </pre>
	 */
	public static int charCount(String src, char ch) {
		if (src == null || src.length() == 0) {
			return 0;
		}

		char[] c = src.toCharArray();
		int len = c.length;
		int num = 0;
		for (int i = 0; i < len; i++) {
			if (c[i] == ch) {
				num++;
			}
		}
		return num;
	}

	/**
	 * Get the number of question mark in sql, it will filter the question mark
	 * in constant.
	 * 
	 * @param sql
	 * @return
	 * 
	 * <pre>
	 * 	 System.out.println(&quot;getBundingNumberInSQL:&quot;+getBundingNumberInSQL(&quot;select * from do where id like 'd?%' and d=? and x=? and a='???'&quot;));
	 * 	 output:2
	 * 	 StringUtil.getBundingNumberInSQL(&quot;select * from do where id like ? and d=? and x=? and a=?&quot;)
	 * 	 output:4
	 * </pre>
	 */
	public static int bundingNumber(String sql) {
		if (isEmpty(sql)) {
			return 0;
		}
		char[] sqlChars = sql.toCharArray();
		boolean isQuoteBegin = false; // Default Single quotes not begin
		int num = 0;
		for (int i = 0; i < sqlChars.length; i++) {
			if (sqlChars[i] == '\'' || sqlChars[i] == '\"') {
				if (isQuoteBegin) {
					isQuoteBegin = false;
				} else {
					isQuoteBegin = true;
				}
			} else {
				if (sqlChars[i] == '?' && !isQuoteBegin) {
					num++;
				}
			}

		}
		return num;
	}
	public static int getBundingNumberInSQL(String sql) {
		if (isEmpty(sql)) {
			return 0;
		}
		char[] sqlChars = sql.toCharArray();
		boolean isQuoteBegin = false; // Default Single quotes not begin
		int num = 0;
		for (int i = 0; i < sqlChars.length; i++) {
			if (sqlChars[i] == '\'' || sqlChars[i] == '\"') {
				if (isQuoteBegin) {
					isQuoteBegin = false;
				} else {
					isQuoteBegin = true;
				}
			} else {
				if (sqlChars[i] == '?' && !isQuoteBegin) {
					num++;
				}
			}

		}
		return num;
	}
	/**
	 * get repeat times of a string in source string.
	 * 
	 * @param src
	 * @param pat
	 * @return
	 * @author
	 * @see
	 * @since 2006-1-18 22:08:03
	 * 
	 * <pre>
	 * StringUtil.getRepeatTimesOfPattern(&quot;This this is is ok, is that right?&quot;,&quot;is&quot;);
	 * output:5
	 * </pre>
	 */
	public static int patternCount(String src, String pat) {
		if (src == null || src.length() == 0 || pat == null || pat.length() == 0) {
			return 0;
		}
		int num = 0;
		int p = src.indexOf(pat);
		int len = pat.length();
		while (p >= 0) {
			num++;
			p = src.indexOf(pat, p + len);
		}
		return num;
	}

	/**
	 * replace source string with parameter to patString with repString
	 * 
	 * @param source
	 * @param patString
	 *            is paramter format which is ${PARAMETER_NAME}
	 * @param repString
	 * @return
	 * 
	 * <pre>
	 * String s=&quot;This is content,id is:${XX_ID}, name is:${XX_NAME}
	 * String result = replaceParameter(s,&quot;${XX_ID}&quot;,&quot;100081&quot;);
	 * output:&quot;This is content,id is:100081, name is:${XX_NAME}
	 * </pre>
	 */
	public static String replaceParameter(String source, String patString, String repString) {
		if (isEmpty(source) || isEmpty(patString) || repString == null)
			return source;
		if (patString.indexOf("$") > -1 || patString.indexOf("{") > -1) {
			patString = "\\$\\{" + rightTrim(leftTrim(patString.trim(), "${"), "}") + "\\}";
		}
		Pattern pattern = Pattern.compile(patString);
		Matcher m = pattern.matcher(source);
		String result = m.replaceAll(repString);
		return result;
	}

	/**
	 * replace first source string patString with repString
	 * 
	 * @param source
	 * @param patString
	 * @param repString
	 * @return
	 * @author
	 * 
	 * <pre>
	 * StringUtil.replaceFirst(&quot;this is a good man. And this is his dog.&quot;,&quot;that&quot;);
	 * output:that is a good man. And this is his dog.
	 * </pre>
	 */
	public static String replaceFirst(String source, String patString, String repString) {
		if (isEmpty(source) || isEmpty(patString) || repString == null)
			return source;
		Pattern pattern = Pattern.compile(patString);
		Matcher m = pattern.matcher(source);
		String result = m.replaceFirst(repString);
		return result;
	}

	/**
	 * replace source string from [from] to [to]
	 * 
	 * @param source
	 * @param from
	 * @param to
	 * @return
	 */
	public static String replaceAll(String source, String patString, String repString) {
		return replaceAll(source, 0, patString, repString);
	}

	/**
	 * replace source string from [from] to [to] ,but from startPosition.
	 * 
	 * @param source
	 * @param startPosition
	 * @param from
	 * @param to
	 * @return
	 */
	public static String replaceAll(String source, int startPosition, String patString, String repString) {
		if (isEmpty(source) || isEmpty(patString) || repString == null) {
			return source;
		}
		if (startPosition < 0) {
			throw new IllegalArgumentException("Illegal start position: " + startPosition);
		}
		String retString = "";
		StringBuffer buf = new StringBuffer();
		int start = startPosition;
		int fromlen = patString.length();
		int subFrom = 0;
		while (source.indexOf(patString, start) != -1) {
			buf.append(source.substring(subFrom, source.indexOf(patString, start))).append(repString);
			start = source.indexOf(patString, start) + fromlen;
			subFrom = start;
		}
		buf.append(source.substring(subFrom));
		return buf.toString();
	}

	/**
	 * Replaces all instances of oldString with newString in line.
	 * 
	 * @param line
	 *            the String to search to perform replacements on
	 * @param oldString
	 *            the String that should be replaced by newString
	 * @param newString
	 *            the String that will replace all instances of oldString
	 * 
	 * @return a String will all instances of oldString replaced by newString
	 * @author tyf
	 */
	public static final String replace(String line, String oldString, String newString) {
		if (line == null) {
			return null;
		}
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0) {
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = line.indexOf(oldString, i)) > 0) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return line;
	}

	/**
	 * Replaces all instances of oldString with newString in line with the added
	 * feature that matches of newString in oldString ignore case.
	 * 
	 * @param line
	 *            the String to search to perform replacements on
	 * @param oldString
	 *            the String that should be replaced by newString
	 * @param newString
	 *            the String that will replace all instances of oldString
	 * 
	 * @return a String will all instances of oldString replaced by newString
	 * @author tyf
	 */
	public static final String replaceIgnoreCase(String line, String oldString, String newString) {
		if (line == null) {
			return null;
		}
		String lcLine = line.toLowerCase();
		String lcOldString = oldString.toLowerCase();
		int i = 0;
		if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = lcLine.indexOf(lcOldString, i)) > 0) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return line;
	}

	/**
	 * Replaces all instances of oldString with newString in line with the added
	 * feature that matches of newString in oldString ignore case. The count
	 * paramater is set to the number of replaces performed.
	 * 
	 * @param line
	 *            the String to search to perform replacements on
	 * @param oldString
	 *            the String that should be replaced by newString
	 * @param newString
	 *            the String that will replace all instances of oldString
	 * @param count
	 *            a value that will be updated with the number of replaces
	 *            performed.
	 * 
	 * @return a String will all instances of oldString replaced by newString
	 * @author tyf
	 */
	public static final String replaceIgnoreCase(String line, String oldString, String newString, int[] count) {
		if (line == null) {
			return null;
		}
		String lcLine = line.toLowerCase();
		String lcOldString = oldString.toLowerCase();
		int i = 0;
		if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
			int counter = 0;
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = lcLine.indexOf(lcOldString, i)) > 0) {
				counter++;
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			count[0] = counter;
			return buf.toString();
		}
		return line;
	}

	/**
	 * Replaces all instances of oldString with newString in line. The count
	 * Integer is updated with number of replaces.
	 * 
	 * @param line
	 *            the String to search to perform replacements on
	 * @param oldString
	 *            the String that should be replaced by newString
	 * @param newString
	 *            the String that will replace all instances of oldString
	 * 
	 * @return a String will all instances of oldString replaced by newString
	 * @author tyf
	 */
	public static final String replace(String line, String oldString, String newString, int[] count) {
		if (line == null) {
			return null;
		}
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0) {
			int counter = 0;
			counter++;
			char[] line2 = line.toCharArray();
			char[] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = line.indexOf(oldString, i)) > 0) {
				counter++;
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			count[0] = counter;
			return buf.toString();
		}
		return line;
	}

	/**
	 * get property values of class array by the getMethodName.
	 * 
	 * @param obj:Object
	 *            array
	 * @param c:class
	 *            name.
	 * @param getMethodName.
	 * @return
	 */
	public static String queryScopeSet(Object[] obj, String className, String getMethodName, String[] excepts) {
		Class cls = null;
		String result = "";
		try {
			cls = Class.forName(className);
		} catch (ClassNotFoundException e1) {
			logger.error("class not found error", e1);
		}
		Method meth = null;
		try {
			meth = cls.getMethod(getMethodName, (java.lang.Class[])null);
		} catch (SecurityException e2) {
			logger.error("security exception", e2);
		} catch (NoSuchMethodException e2) {
			logger.error("no such method error", e2);
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < obj.length; i++) {
			if (obj[i] != null) {
				String retobj = "";
				try {
					// no any arg, so the arglist is null.
					retobj = (String) meth.invoke(obj[i], (Object)null);
					boolean exceptFlag = false;
					if (excepts != null)
						for (int j = 0; j < excepts.length; j++) {
							if (excepts[j] != null) {
								if (excepts[j].equals(retobj)) {
									exceptFlag = true;
									break;
								}
							}
						}
					// if include the except String, then execute next loop.

					if (exceptFlag)
						continue;
				} catch (IllegalArgumentException e) {
					logger.error("illegal argument exception", e);
				} catch (IllegalAccessException e) {
					logger.error("illegal access exception", e);
				} catch (InvocationTargetException e) {
					logger.error("invocation target exception", e);
				}
				sb.append("'" + retobj + "',");
			}
		}
		result = sb.toString();
		// remove the last comma.
		if (result.length() > 0)
			result = result.substring(0, result.lastIndexOf(","));
		return result;
	}

	/**
	 * 
	 * Judge wether the string is null or equal "", before compare the string
	 * would be trim.
	 * 
	 * @param str
	 * @return
	 * @author
	 * @see isNull
	 * @since 2006-5-30 15:10:25
	 * 
	 * <pre>
	 * Example:
	 * isEmpty(null)=true
	 * isEmpty(&quot;&quot;)=true
	 * isEmpty(&quot;  &quot;)=true
	 * isEmpty(&quot;d &quot;)=false
	 * </pre>
	 */
	public static boolean isEmpty(String str) {
		if (str == null || str.trim().length() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * Judge wether the string is null or equal "", before compare the string
	 * wouldn't be trim.
	 * 
	 * @param str
	 * @return
	 * @author
	 * @see isEmpty
	 * @since 2006-5-30 15:11:24
	 * 
	 * <pre>
	 * Example:
	 * isNull(null)=true
	 * isNull(&quot;&quot;)=false
	 * isNull(&quot;  &quot;)=false
	 * isNull(&quot;d &quot;)=false
	 * </pre>
	 */
	public static boolean isNull(String str) {
		if (str == null || str.length() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * Judge wether the string array is null or size equals to zero.
	 * 
	 * @param str
	 * @return
	 * @author
	 * @see
	 * @since 2006-5-30 15:11:53
	 * 
	 * <pre>
	 * Example:
	 * isEmpty(null)=true
	 * isNull(new Sring[0])=true
	 * </pre>
	 */
	public static boolean isEmpty(String[] str) {
		if (str == null || str.length == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * Different from the substring method of java.lang.String object It get a
	 * number of char in [srcStr] from [start] according to [len]
	 * 
	 * @param srcStr
	 * @param start
	 * @param len
	 *            length of string, not end position.
	 * @return
	 * @author
	 * @see java.lang.String#substring
	 * @since 2006-5-30 16:12:15
	 * 
	 * <pre>
	 * System.out.println(substr(&quot;342342.234&quot;,-6,3))
	 * output:234
	 * </pre>
	 */
	public static String substr(String srcStr, int start, int len) {
		if (isEmpty(srcStr) || len <= 0 || Math.abs(start) > srcStr.length()) {
			return null;
		}
		char[] dest = null;
		if (start >= 0) {
			return srcStr.substring(start, start + len);
		} else {
			int srcLen = srcStr.length();
			if (len > Math.abs(start)) {
				len = Math.abs(start);
			}
			dest = new char[len];
			char[] src = srcStr.toCharArray();
			System.arraycopy(src, srcLen - len, dest, 0, len);
		}
		return String.valueOf(dest);
	}

	/**
	 * Judge whether the object is null.
	 * 
	 * @param source
	 * @return
	 * @author
	 * @see
	 * @since 2006-9-19 9:59:30
	 */
	public static boolean isNullObject(Object source) {
		if (source == null)
			return true;

		return false;
	}

	/**
	 * 
	 * Like substring of java.lang.String object, but it supports the negative
	 * number.
	 * 
	 * @param srcStr
	 * @param start
	 * @param end
	 * @return
	 * @author
	 * @see substr,java.lang.String#substring
	 * @since 2006-5-30 16:15:42
	 * 
	 * <pre>
	 * System.out.println(substring(&quot;342342.234&quot;,-6,-80));
	 * output:3423
	 * System.out.println(substring(&quot;342342.234&quot;,-6,-3));
	 * output:42.
	 * </pre>
	 */
	public static String substring(String srcStr, int start, int end) {
		if (isEmpty(srcStr))
			return null;
		int srcLen = srcStr.length();
		if (start * end < 0 || (srcLen + start) < 0) {
			return null;
		}
		if (start >= 0 && end >= 0) {
			return srcStr.substring(start, end);
		} else {
			if (Math.abs(start) > Math.abs(end)) {
				return srcStr.substring(srcLen + start, srcLen + end);
			} else {
				return srcStr.substring((srcLen + end) < 0 ? 0 : (srcLen + end), srcLen + start);
			}
		}

	}

	/**
	 * substring according to length.
	 * 
	 * @param srcStr
	 * @param length
	 * @return
	 */
	public static String substring(String srcStr, int length) {
		if (isEmpty(srcStr) || length < 0)
			return null;
		int srcLen = srcStr.length();
		if (length > srcLen)
			return srcStr;

		return srcStr.substring(0, length);

	}

	public static String leftString(String srcStr, int length) {
		return substring(srcStr, length);

	}

	public static String rightString(String srcStr, int length) {
		if (isEmpty(srcStr) || length < 0)
			return null;
		int len = srcStr.length() < length ? srcStr.length() : length;
		return substring(srcStr, srcStr.length() - len, srcStr.length());

	}

	public static String rightString(String srcStr, int length, int end) {
		if (isEmpty(srcStr) || length < 0 || end < 0 || srcStr.length() < end)
			return null;
		int len = srcStr.length() - end < length ? srcStr.length() - end : length;
		return substring(srcStr, srcStr.length() - len - end, srcStr.length() - end);

	}

	/**
	 * Reverse string
	 * 
	 * @param str
	 * @return
	 * 
	 * <pre>
	 * String s=&quot;jiang bing&quot;;
	 * String ss=StringUtil.reverse(s);
	 * output:gnib gnaij
	 * </pre>
	 */
	public static String reverse(String str) {
		if (isEmpty(str)) {
			return str;
		}
		char[] ch = str.toCharArray();
		int _len = ch.length;
		char[] ch2 = new char[_len];
		for (int i = 0; i < _len; i++) {
			ch2[i] = ch[_len - i - 1];
		}
		return String.copyValueOf(ch2);
	}

	/**
	 * Check whether the value is "TRUE" or "YES", if true, return true, else
	 * return false.
	 * 
	 * @param value
	 * @return
	 */
	public static boolean checkTrue(String value) {
		if (LOGIC_YES.equalsIgnoreCase(value) || LOGIC_TRUE.equalsIgnoreCase(value)
				|| LOGIC_YES_ABBR.equalsIgnoreCase(value)) {
			return true;
		}
		return false;
	}

	/**
	 * Check whether the value is "FALSE" or "NO", if true, return false, else
	 * return true.
	 * 
	 * @param value
	 * @return
	 * @author
	 * @see
	 * @since 2006-9-19 10:01:14
	 */
	public static boolean checkFalse(String value) {
		if (LOGIC_NO.equalsIgnoreCase(value) || LOGIC_FALSE.equalsIgnoreCase(value)
				|| LOGIC_NO_ABBR.equalsIgnoreCase(value)) {
			return false;
		}
		return true;
	}

	/**
	 * return system line separator.
	 * 
	 * @return
	 */
	public static String crLf() {
		return System.getProperty("line.separator");
	}

	/**
	 * same as crLf
	 * 
	 * @return
	 * @author
	 */
	public static String swCrLf() {
		char[] crLfArr = { 0x0d, 0x0a };
		return (String.copyValueOf(crLfArr));
	}

	/**
	 * 
	 * @param attrEntry
	 * @return
	 * @see getAttrValue
	 */
	public static String getAttrName(String attrEntry) {
		return getAttrName(attrEntry, DEFAULT_ATTR_ASSIGN_LOGIC);
	}

	/**
	 * Get Attribute Name from attrEntry
	 * 
	 * @param attrEntry,
	 *            format is XxxxxAttribuete=XxxxValue
	 * @param attrAssignLogic,
	 *            the assignment if the assignment isn't equals sign.
	 * @return
	 * @see getAttrValue
	 */
	public static String getAttrName(String attrEntry, String attrAssignLogic) {
		if (isEmpty(attrEntry) || isEmpty(attrAssignLogic)) {
			return null;
		}
		if (attrEntry.indexOf(attrAssignLogic) <= 0) {
			return attrEntry.trim();
		}
		return attrEntry.substring(0, attrEntry.indexOf(attrAssignLogic)).trim();
	}

	/**
	 * Get Attribute Value from attrEntry
	 * 
	 * @param attrEntry,
	 *            format is XxxxxAttribuete=XxxxValue
	 * @return
	 * @see getAttrName
	 */
	public static String getAttrValue(String attrEntry) {
		return getAttrValue(attrEntry, DEFAULT_ATTR_ASSIGN_LOGIC);
	}

	/**
	 * Get Attribute Value from attrEntry
	 * 
	 * @param attrEntry,
	 *            format is XxxxxAttribuete=XxxxValue
	 * @param attrAssignLogic,
	 *            the assignment if the assignment isn't equals sign.
	 * @return
	 * @see getAttrName
	 */
	public static String getAttrValue(String attrEntry, String attrAssignLogic) {
		if (isEmpty(attrEntry) || isEmpty(attrAssignLogic)) {
			return null;
		}
		if (attrEntry.indexOf(attrAssignLogic) <= 0) {
			return null;
		}
		return attrEntry.substring(attrEntry.indexOf(attrAssignLogic) + attrAssignLogic.length(), attrEntry.length())
				.trim();
	}

	public static String getOptionValue(String option) {
		return getOptionValue(option, DEFAULT_OPTION_DELIMITER);
	}

	public static String getOptionDisplay(String option) {
		return getOptionDisplay(option, DEFAULT_OPTION_DELIMITER);
	}

	public static String getOptionValue(String option, String delimiter) {
		if (isEmpty(option) || isEmpty(delimiter)) {
			return null;
		}
		if (option.indexOf(delimiter) <= 0) {
			return option.trim();
		}
		return option.substring(0, option.indexOf(delimiter)).trim();
	}

	public static String getOptionDisplay(String option, String delimiter) {
		if (isEmpty(option) || isEmpty(delimiter)) {
			return null;
		}
		if (option.indexOf(delimiter) <= 0) {
			return null;
		}
		return option.substring(option.indexOf(delimiter) + delimiter.length(), option.length()).trim();
	}

	/**
	 * 
	 * UpperCase the first character of string.
	 * 
	 * @param s
	 * @return
	 * @author
	 * @see firstLowerCase
	 * @since May 31, 2006 11:39:40 AM
	 * 
	 * <pre>
	 * String s=&quot;jiang bing&quot;;
	 * String ss=StringUtil.firstUpperCase(s);
	 * output:Jiang bing
	 * </pre>
	 */
	public static String firstUpperCase(String s) {
		if (s == null || s.length() == 0) {
			return s;
		}
		return s.substring(0, 1).toUpperCase() + s.substring(1);

	}

	/**
	 * LowerCase the first character of string.
	 * 
	 * @param s
	 * @return
	 * @author
	 * @see firstUpperCase
	 * @since May 31, 2006 11:39:13 AM
	 * 
	 * <pre>
	 * String s=&quot;Jiang bing&quot;;
	 * String ss=StringUtil.firstLowerCase(s);
	 * output:jiang bing
	 * </pre>
	 */
	public static String firstLowerCase(String s) {
		if (s == null || s.length() == 0) {
			return s;
		}
		return s.substring(0, 1).toLowerCase() + s.substring(1);

	}

	/**
	 * Initial capital of word in a string.
	 * 
	 * @param s
	 * @return
	 * 
	 * <pre>
	 * String s=&quot;This is a good man&quot;;
	 * System.out.println(initialCapital(s));
	 * output: This Is A Good Man
	 * String s=&quot;customer_producT_NAME&quot;;
	 * System.out.println(initialCapital(s));
	 * output: Customer_Product_Name
	 * </pre>
	 */
	public static String initialCapital(String s) {
		if (isEmpty(s))
			return s;
		boolean beginFlag = true;
		char sc[] = s.toLowerCase().toCharArray();
		for (int i = 0; i < sc.length; i++) {
			if (beginFlag) {
				sc[i] = (char) (sc[i] ^ 32);
			}
			if (sc[i] == ' ' || sc[i] == '-' || sc[i] == '_' || sc[i] == ';' || sc[i] == '.') {
				beginFlag = true;
			} else {
				beginFlag = false;
			}
		}
		return String.valueOf(sc);
	}

	public static String initialCap(String s) {
		if (isEmpty(s))
			return s;
		boolean b = true;
		char sc[] = s.toLowerCase().toCharArray();
		for (int i = 0; i < sc.length; i++) {
			if (b) {
				sc[i] = (char) (sc[i] ^ 32);
			}

			if (sc[i] == ' ' || sc[i] == '-' || sc[i] == '_' || sc[i] == ';' || sc[i] == '.') {
				b = true;
			} else {
				b = false;
			}
		}
		return String.valueOf(sc);
	}

	public static String escapeValue(String value, String escapeChar){
		if(value==null) return null;
		if(escapeChar==null) return value;
		String val = value;
		boolean flag = false;
		if(val.indexOf("{~}")!=-1){
			val=val.replace("{~}", "{~}{~}");
			flag = true;
		}
		if(val.indexOf("{`}")!=-1){
			val=val.replace("{`}", "{~}{`}");
		}
		if(val.indexOf(escapeChar)!=-1){
			if(flag){
				val=val.replace(escapeChar,"{`}");
			} else{
				val=val.replace(escapeChar,"{~}");
			}
		}
		return val;
		
	}
	
	public static String unescapeValue(String value, String escapeChar) {
		if (value == null)
			return null;
		if (escapeChar == null)
			return value;
		String val = value;
		boolean flag = false;
		if (val.indexOf("{~}{~}") != -1) {
			val=val.replace("{~}{~}", "{~}");
			flag = true;
		}
		if (val.indexOf("{~}{`}") != -1) {
			val=val.replace("{~}{`}", "{`}");
			flag = true;
		}
		if(flag){
			if(val.indexOf("{`}") != -1){
				val=val.replace("{`}", escapeChar);
			}
		} else{
			if(val.indexOf("{~}") != -1){
				val=val.replace("{~}", escapeChar);
			}
		}
		return val;

	}	
	/**
	 * escape sql.
	 * 
	 * @param str
	 * @return
	 * 
	 * <pre>
	 * String sql=&quot;select * from MYTABLE where NAME = &quot;;
	 * String value=&quot;Jiang's&quot;);
	 * sql=sql+&quot;'&quot;+value+&quot;'&quot;;
	 * sql=StringUtil.escapeSql(sql);
	 * output:select * from MYTABLE where NAME = 'Jiang''s'
	 * </pre>
	 */
	public static String escapeSql(String str) {
		if (str == null) {
			return null;
		}
		return replaceAll(str, "'", "''");
	}

	/**
	 * escape HTML
	 * 
	 * @param input
	 * @return
	 * 
	 * <pre>
	 * escapeHtml(&quot;&lt;html&gt;&lt;body&gt;&quot;);
	 * output:&lt;html&gt;&lt;body&gt;
	 * </pre>
	 */
	public static String escapeHtml(String string) {
		StringBuffer sb = new StringBuffer(string.length());
		// true if last char was blank
		boolean lastWasBlankChar = false;
		int len = string.length();
		char c;
		char pc = '\0';
		for (int i = 0; i < len; i++) {
			c = string.charAt(i);
			if (c == ' ') {
				if (lastWasBlankChar) {
					lastWasBlankChar = false;
					sb.append("&nbsp;");
				} else {
					lastWasBlankChar = true;
					sb.append(' ');
				}
			} else {
				lastWasBlankChar = false;
				//
				// HTML Special Chars
				if (c == '"')
					sb.append("&quot;");
				else if (c == '&')
					sb.append("&amp;");
				else if (c == '<')
					sb.append("&lt;");
				else if (c == '>')
					sb.append("&gt;");
				else if (c == '\r')
					// Handle Newline
					sb.append("&lt;BR&gt;");
				else if (c == '\n') {
					if (pc != '\r')
						sb.append("&lt;BR&gt;");
				} else {
					int ci = 0xffff & c;
					if (ci < 160)
						// nothing special only 7 Bit
						sb.append(c);
					else {
						// Not 7 Bit use the unicode system
						sb.append("&#");
						sb.append(new Integer(ci).toString());
						sb.append(';');
					}
				}
			}
			pc = c;
		}
		return sb.toString();
	}

	/**
	 * Parse string with a splitor and return the value according to index.
	 * 
	 * @param src
	 * @param splitor
	 * @param index
	 * @return
	 * @throws IllegalArgumentException
	 *             if index is less or equal zero.
	 * 
	 * <pre>
	 * String str=&quot;A$BC$DX$che$cc$$&quot;;
	 * String str2=&quot;A$$BC$DX$che$$cc$$&quot;;
	 * StringUtil.parse(str,&quot;$&quot;,2);
	 * output: BC
	 * StringUtil.parse(str,&quot;$$&quot;,2);
	 * output:BC$DX$che
	 * </pre>
	 */
	public static String parse(String src, String splitor, int index) {
		if (isEmpty(src) || isEmpty(splitor))
			return src;
		if (index <= 0) {
			throw new IllegalArgumentException("Illegal index: " + index);
		}
		String str = trim(src, splitor);
		int splitorLen = index == 1 ? 0 : splitor.length();
		int prevLoc = 0;
		int loc = str.indexOf(splitor);
		int loop = 1;
		while (loc != -1 && loop < index) {
			prevLoc = loc;
			loc = str.indexOf(splitor, prevLoc + splitorLen);
			loop++;
		}
		if (loc == -1) {
			if (loop < index) {
				return null;
			} else {
				return str.substring(prevLoc + splitorLen);
			}
		} else {
			return str.substring(prevLoc + splitorLen, loc);
		}
	}

	/**
	 * convert object to string according to some format.
	 * 
	 * @param value
	 * @param type
	 * @param fmt
	 * @return
	 * @author
	 */
	public static String objectToString(Object value, String type, String fmt) {
		if (value == null) {
			return null;
		}
		if ("java.lang.String".equals(type)) {
			return (String) value;
		} else if ("byte".equals(type) || "short".equals(type) || "int".equals(type) || "long".equals(type)) {
			return String.valueOf(value);
		} else if ("float".equals(type)) {
			DecimalFormat format = null;
			if (fmt != null && fmt.length() > 0) {
				format = new DecimalFormat(fmt);
			} else {
				format = new DecimalFormat("#,##0.00#");
			}
			return format.format(Float.parseFloat((String) value));
		} else if ("double".equals(type)) {
			DecimalFormat format = null;
			if (fmt != null && fmt.length() > 0) {
				format = new DecimalFormat(fmt);
			} else {
				format = new DecimalFormat("#,##0.00#");
			}
			return format.format(Double.parseDouble((String) value));
		} else if ("Byte".equals(type) || "Short".equals(type) || "Integer".equals(type) || "Long".equals(type)
				|| "Float".equals(type) || "Double".equals(type)) {
			return value.toString();
		} else if ("java.util.Date".equals(type)) {
			SimpleDateFormat format = null;
			if (fmt != null && fmt.length() > 0) {
				format = new SimpleDateFormat(fmt);
			} else {
				format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			}

			return format.format(new Date(((java.util.Date) value).getTime()));
		} else if ("java.sql.Date".equals(type)) {
			SimpleDateFormat format = null;
			if (fmt != null && fmt.length() > 0) {
				format = new SimpleDateFormat(fmt);
			} else {
				format = new SimpleDateFormat("yyyy-MM-dd");
			}

			return format.format((Date) value);
		} else if ("java.sql.Timestamp".equals(type)) {
			SimpleDateFormat format = null;
			if (fmt != null && fmt.length() > 0) {
				format = new SimpleDateFormat(fmt);
			} else {
				format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			}

			return format.format(new Date(((java.sql.Timestamp) value).getTime()));
		} else if ("java.sql.Time".equals(type)) {
			SimpleDateFormat format = null;
			if (fmt != null && fmt.length() > 0) {
				format = new SimpleDateFormat(fmt);
			} else {
				format = new SimpleDateFormat("HH:mm:ss");
			}

			return format.format(new Date(((java.sql.Time) value).getTime()));
		} else {
			return value.toString();
		}
	}

	/**
	 * escape javascript.
	 * 
	 * @param s
	 * @return
	 */
	public static String escapeJavascript(String s) {
		StringBuffer sbuf = new StringBuffer();
		int len = s.length();
		for (int i = 0; i < len; i++) {
			int ch = s.charAt(i);
			if (ch == ' ') { // space : map to '+'
				sbuf.append('+');
			} else if ('A' <= ch && ch <= 'Z') { // 'A'..'Z' : as it was
				sbuf.append((char) ch);
			} else if ('a' <= ch && ch <= 'z') { // 'a'..'z' : as it was
				sbuf.append((char) ch);
			} else if ('0' <= ch && ch <= '9') { // '0'..'9' : as it was
				sbuf.append((char) ch);
			} else if (ch == '-' || ch == '_' // unreserved : as it was
					|| ch == '.' || ch == '!' || ch == '~' || ch == '*' || ch == '\'' || ch == '(' || ch == ')') {
				sbuf.append((char) ch);
			} else if (ch <= 0x007F) { // other ASCII : map to %XX
				sbuf.append('%');
				sbuf.append(hex[ch]);
			} else { // unicode : map to %uXXXX
				sbuf.append('%');
				sbuf.append('u');
				sbuf.append(hex[(ch >>> 8)]);
				sbuf.append(hex[(0x00FF & ch)]);
			}
		}
		return sbuf.toString();
	}

	public static String unescapeJavascript(String s) {
		StringBuffer sbuf = new StringBuffer();
		int i = 0;
		int len = s.length();
		while (i < len) {
			int ch = s.charAt(i);
			if (ch == '+') { // + : map to ' '
				sbuf.append(' ');
			} else if ('A' <= ch && ch <= 'Z') { // 'A'..'Z' : as it was
				sbuf.append((char) ch);
			} else if ('a' <= ch && ch <= 'z') { // 'a'..'z' : as it was
				sbuf.append((char) ch);
			} else if ('0' <= ch && ch <= '9') { // '0'..'9' : as it was
				sbuf.append((char) ch);
			} else if (ch == '-' || ch == '_' // unreserved : as it was
					|| ch == '.' || ch == '!' || ch == '~' || ch == '*' || ch == '\'' || ch == '(' || ch == ')') {
				sbuf.append((char) ch);
			} else if (ch == '%') {
				int cint = 0;
				if ('u' != s.charAt(i + 1)) { // %XX : map to ascii(XX)
					cint = (cint << 4) | val[s.charAt(i + 1)];
					cint = (cint << 4) | val[s.charAt(i + 2)];
					i += 2;
				} else { // %uXXXX : map to unicode(XXXX)
					cint = (cint << 4) | val[s.charAt(i + 2)];
					cint = (cint << 4) | val[s.charAt(i + 3)];
					cint = (cint << 4) | val[s.charAt(i + 4)];
					cint = (cint << 4) | val[s.charAt(i + 5)];
					i += 5;
				}
				sbuf.append((char) cint);
			}
			i++;
		}
		return sbuf.toString();
	}

	/**
	 * judge wether the the string is an IP.
	 * 
	 * @param ipStr
	 * @return
	 * @author
	 * @see
	 */
	public static boolean isIP(String ipStr) {
		if (ipStr == null || ipStr.length() == 0) {
			return false;
		}
		String[] sections = stringToArray(ipStr, ".");
		if (sections.length != 4) {
			return false;
		}
		for (int i = 0; i < sections.length; i++) {
			try {
				int n = Integer.parseInt(sections[i]);
				if (n < 0 || n > 255) {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Parse a string to boolean.
	 * 
	 * @param param
	 *            string to parse
	 * @return boolean value, if param begin with(1,y,Y,t,T) return true, on
	 *         exception return false.
	 * @author tyf
	 */
	public static boolean parseBoolean(String param) {
		if (isEmpty(param)) {
			return false;
		}
		switch (param.charAt(0)) {
		case '1':
		case 'y':
		case 'Y':
		case 't':
		case 'T':
			return true;
		}
		return false;
	}

	/**
	 * Check a string null or blank.
	 * 
	 * @param param
	 *            string to check
	 * @return boolean
	 */
	public static boolean nullOrBlank(String param) {
		return (param == null || param.length() == 0 || param.trim().equals("")) ? true : false;
	}

	public static String notNull(String param) {
		return param == null ? "" : param.trim();
	}

	public static String replaceComma(String text) {
		if (text != null) {
			text = text.replaceAll("", ",");
		}
		return text;
	}

	public static String replaceBr(String text) {
		if (text != null) {
			text = text.replaceAll("\n", "<BR>");
		}
		return text;
	}

	/**
	 * Escapes all necessary characters in the String so that it can be used in
	 * an XML doc.
	 * 
	 * @param string
	 *            the string to escape.
	 * @return the string with appropriate characters escaped.
	 */
	public static final String escapeForXML(String string) {
		if (string == null) {
			return null;
		}
		char ch;
		int i = 0;
		int last = 0;
		char[] input = string.toCharArray();
		int len = input.length;
		StringBuffer out = new StringBuffer((int) (len * 1.3));
		for (; i < len; i++) {
			ch = input[i];
			if (ch > '>') {
				continue;
			} else if (ch == '<') {
				if (i > last) {
					out.append(input, last, i - last);
				}
				last = i + 1;
				out.append(LT_ENCODE);
			} else if (ch == '&') {
				if (i > last) {
					out.append(input, last, i - last);
				}
				last = i + 1;
				out.append(AMP_ENCODE);
			} else if (ch == '"') {
				if (i > last) {
					out.append(input, last, i - last);
				}
				last = i + 1;
				out.append(QUOTE_ENCODE);
			}
		}
		if (last == 0) {
			return string;
		}
		if (i > last) {
			out.append(input, last, i - last);
		}
		return out.toString();
	}
	
	public static String subUtf8String(String utf8String, int limited) {
		char[] ch = utf8String.toCharArray();
		int pos = 0;
		int length = 0;
		int incLength = 0;
		for (char c : ch) {
			int ascii = (int) c;
			if (ascii < 127) {
				incLength = 1;
			} else if (ascii < 2047) {
				incLength = 2;
			} else if (ascii < 65535) {
				incLength = 3;
			} else if (ascii < 1114111) {
				incLength = 4;
			}
			if (length + incLength > limited) {
				break;
			}
			length += incLength;
			pos++;
		}
		return utf8String.substring(0, pos);
	}
	public static String subGbkString(String gbkString, int limited) throws Exception {
		String uniString = null;
		try{
			uniString = new String(gbkString.getBytes(),"unicode");
		}catch(Exception e){
			throw new SystemException("encoding error", e);
		}
		char[] ch = uniString.toCharArray();
		int pos = 0;
		int length = 0;
		int incLength = 0;
		for (char c : ch) {
			int ascii = (int) c;
			if (ascii < 127) {
				incLength = 1;
			} else {
				incLength = 2;
			}
			if (length + incLength > limited) {
				break;
			}
			length += incLength;
			pos++;
		}
		return gbkString.substring(0, pos);
	}
	public static String substring(String str, String characterSet, int limited){
		if("UTF-8".equals(characterSet)){
			return subUtf8String(str, limited);
		} else if("GBK".equals(characterSet)){
			return subUtf8String(str, limited);
		}
		return null;
	}
	public static boolean containCsvItem(String strList, String item) {
		if (item == null || item.trim().length() == 0 || strList == null || strList.trim().length() == 0) {
			return false;
		}
		String list = "," + strList + ",";
		String it = "," + item + ",";
		return (list.indexOf(it) != -1);
	}
	private static String encode(String value, boolean trimWhiteSpace, char escapeCharacter) {
		String result = "";
		if (value != null) {
			if (trimWhiteSpace) {
				result = value.trim();
			} else {
				result = value;
			}
			if (escapeCharacter != '\0') {
				char[] cs = result.toCharArray();
				StringBuffer buf = new StringBuffer();
				for (int i = 0; i < cs.length; i++) {
					if (cs[i] == ",".toCharArray()[0]) {
						buf.append(escapeCharacter);
					}
					buf.append(cs[i]);
				}
				result = buf.toString();
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param ch
	 * @return
	 */
	public static boolean isIdChar(char ch){
		return "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_".indexOf(ch)!=-1;
	}
	
	public static boolean isVaildEmail(String email){
	     String emailPattern="[a-zA-Z0-9][a-zA-Z0-9._-]{1,16}[a-zA-Z0-9]@[a-zA-Z0-9]+.[a-zA-Z0-9]+";
	     boolean result=Pattern.matches(emailPattern, email);
	     return result;
	} 
	
	public static String convertToHref(String Str)

	{
		if (Str==null||Str.equals("")) return Str;
	       Matcher matcher=null;
	       Pattern pattern=null;
	       String str2="";
	       pattern = Pattern.compile("(https://[A-Za-z0-9\\./=\\?%\\-&_~`@':+!]+)|(http://[A-Za-z0-9\\./=\\?%\\-&_~`@':+!]+)|(www\\.[A-Za-z0-9\\./=\\?%\\-&_~`@':+!]+)",Pattern.CASE_INSENSITIVE);
	       matcher = pattern.matcher(Str);
	       StringBuffer stringbuffer = new StringBuffer();
	       for(; matcher.find(); matcher.appendReplacement(stringbuffer, str2)){
	    	   	if(matcher.group(3)!=null){
                str2 = "<a href=\"http://" + matcher.group(3) + "\" target=\"_blank\">"+matcher.group(3)+"</a>";
	    	   	}else  if(matcher.group(2)!=null){
	    	   		str2 = "<a href=\"" + matcher.group(2) + "\" target=\"_blank\">"+matcher.group(2)+"</a>";
	    	   	}else{
	            	 str2 = "<a href=\"" + matcher.group(1) + "\" target=\"_blank\">"+matcher.group(1)+"</a>";
	    	   	}
	      }

	      matcher.appendTail(stringbuffer);
	      return stringbuffer.toString();	      
	}
	
	public static String showInEmail(String str){
		str = str.replaceAll("&amp;","&");
		str = str.replaceAll("&quot;","\"");
		str = str.replaceAll("&nbsp;"," ");
		str = str.replaceAll("&copy;","©");
		str = str.replaceAll("<br>","\r\n");
		return str;
	}
	
	public static String removeComma(String str){
		if(!isEmpty(str)){
		    if(str.substring(str.length()-1).equals(",")){
		        return str.substring(0,str.length()-1);
		    }else{
		        return str;
		    }
		}
		return "";
	}
	
	public static String escapeHTMLXMLJavaScript(String input){
		input = StringEscapeUtils.escapeHtml(input);		
		input = input.replaceAll("&lt;br&gt;", "<br>");
		return input;
	}
	
	public static String escapeHTMLForDesc(String input){
		input = input.replaceAll("&amp;amp;","&");
		input = input.replaceAll("&amp;","&");
		input = input.replaceAll("&nbsp;"," ");
		input = input.replaceAll("\n","<br>");
		input = input.replaceAll("&quot;","\"");
		input = input.replaceAll("&copy;","©");
		input = StringEscapeUtils.escapeHtml(input);
		input = input.replaceAll("&lt;br&gt;", "<br>");
		return input;
	}
	
	
	public static String formartCCList(String cc){
		if(StringUtils.isBlank(cc)){
			return cc;
		}
		String result = "";
		String[] array = cc.split(",");
		
				
		if(array!=null && array.length>0){			
			if(array.length <= 6){
				return cc;
			}
			for(int i=0;i<array.length;i++){
				if(i < array.length-1){
					if(i >= 5 && (i+1)%6 == 0){
						result += array[i] + ",<br>";	
					}else{
						result += array[i] + ",";
					}
						
				}else{
					result += array[i];
				}
			}
		}
		
		return result;
	}
	
	public static String deFormartCCList(String forward){
		if(StringUtils.isNotBlank(forward) && forward.indexOf("<br>") > -1){
			forward = forward.replaceAll("<br>", "");	
		}
		return forward;
	}
}

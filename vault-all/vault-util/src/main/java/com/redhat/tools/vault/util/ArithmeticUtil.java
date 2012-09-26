package com.redhat.tools.vault.util;

import java.math.BigDecimal;
import java.util.Random;

/**
 * @author speng@redhat.com
 */
public class ArithmeticUtil {
	// Default compute scale
	private static final int DEFAULT_DIV_SCALE = 32;

	private static final int ID_MIN_LENGTH = 6;

	private static Random random = new Random();

	private static final char[] RANDOM_SRC_LETTER = new char[] { 'A', 'B', 'C',
			'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
			'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c',
			'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

	private static final char[] RANDOM_SRC_ID = new char[] { 'A', 'B', 'C',
			'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
			'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c',
			'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2',
			'3', '4', '5', '6', '7', '8', '9', '-', '_' };

	//
	private ArithmeticUtil() {
	}

	/**
	 * add short for two arguments.
	 * @param short1
	 * @param short2
	 * @return
	 */
	public static Short add(Short short1, Short short2) {
		short sh1, sh2;
		if (short1 == null) {
			sh1 = 0;
		}
		else {
			sh1 = short1.shortValue();
		}
		if (short2 == null) {
			sh2 = 0;
		}
		else {
			sh2 = short2.shortValue();
		}
		return Short.valueOf((short) (sh1 + sh2));
	}

	/**
	 * add integer
	 * @param integer1
	 * @param integer2
	 * @return
	 */
	public static Integer add(Integer integer1, Integer integer2) {
		int int1, int2;
		if (integer1 == null) {
			int1 = 0;
		}
		else {
			int1 = integer1.intValue();
		}
		if (integer2 == null) {
			int2 = 0;
		}
		else {
			int2 = integer2.intValue();
		}
		return Integer.valueOf(int1 + int2);
	}

	/**
	 * add integer.
	 * @param integer1
	 * @param integer2
	 * @param integer3
	 * @return
	 */
	public static Integer add(Integer integer1, Integer integer2,
			Integer integer3) {
		int int1, int2, int3;
		if (integer1 == null) {
			int1 = 0;
		}
		else {
			int1 = integer1.intValue();
		}
		if (integer2 == null) {
			int2 = 0;
		}
		else {
			int2 = integer2.intValue();
		}
		if (integer3 == null) {
			int3 = 0;
		}
		else {
			int3 = integer3.intValue();
		}
		return Integer.valueOf(int1 + int2 + int3);
	}

	/**
	 * add long arguments.
	 * @param long1
	 * @param long2
	 * @return
	 */
	public static Long add(Long long1, Long long2) {
		int l1, l2;
		if (long1 == null) {
			l1 = 0;
		}
		else {
			l1 = long1.intValue();
		}
		if (long2 == null) {
			l2 = 0;
		}
		else {
			l2 = long2.intValue();
		}
		return Long.valueOf(l1 + l2);
	}

	/**
	 * add long for three arguments.
	 * @param long1
	 * @param long2
	 * @return
	 */
	public static Long add(Long long1, Long long2, Long long3) {
		int l1, l2, l3;
		if (long1 == null) {
			l1 = 0;
		}
		else {
			l1 = long1.intValue();
		}
		if (long2 == null) {
			l2 = 0;
		}
		else {
			l2 = long2.intValue();
		}
		if (long3 == null) {
			l3 = 0;
		}
		else {
			l3 = long3.intValue();
		}
		return Long.valueOf(l1 + l2 + l3);
	}

	/**
	 * add computation
	 * 
	 * @param v1 addend
	 * @param v2 augend
	 * @return the and
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * add computation with three parameters
	 * 
	 * @param v1
	 * @param v2
	 * @param v3
	 * @return
	 * @author
	 */
	public static double add(double v1, double v2, double v3) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		BigDecimal b3 = new BigDecimal(Double.toString(v3));
		return b1.add(b2).add(b3).doubleValue();
	}

	/**
	 * add computation with for parameters.
	 * 
	 * @param v1
	 * @param v2
	 * @param v3
	 * @param v4
	 * @return
	 * @author
	 */
	public static double add(double v1, double v2, double v3, double v4) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		BigDecimal b3 = new BigDecimal(Double.toString(v3));
		BigDecimal b4 = new BigDecimal(Double.toString(v4));
		return b1.add(b2).add(b3).add(b4).doubleValue();
	}

	/**
	 * subtration
	 * 
	 * @param v1 minuend
	 * @param v2 subtrahend
	 * @return difference
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * multiplication
	 * 
	 * @param v1 multiplier
	 * @param v2 multiplier
	 * @return product
	 */
	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * multiplication for three parameters.
	 * 
	 * @param v1
	 * @param v2
	 * @param v3
	 * @return
	 * @author
	 */
	public static double mul(double v1, double v2, double v3) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		BigDecimal b3 = new BigDecimal(Double.toString(v3));
		return b1.multiply(b2).multiply(b3).doubleValue();
	}

	public static double mul(double v1, double v2, double v3, double v4) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		BigDecimal b3 = new BigDecimal(Double.toString(v3));
		BigDecimal b4 = new BigDecimal(Double.toString(v4));
		return b1.multiply(b2).multiply(b3).multiply(b4).doubleValue();
	}

	/**
	 * dividing
	 * 
	 * @param v1 dividend
	 * @param v2 divsor
	 * @return quotient
	 */
	public static double div(double v1, double v2) {
		return div(v1, v2, DEFAULT_DIV_SCALE);
	}

	public static double div(double v1, double v2, double v3) {
		return div(v1, v2, v3, DEFAULT_DIV_SCALE);
	}

	/**
	 * divding with int parameters.
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 * @author
	 */
	public static double div(int v1, int v2) {
		return div(v1, v2, DEFAULT_DIV_SCALE);
	}

	/**
	 * dividing
	 * 
	 * @param v1 dividend
	 * @param v2 divsor
	 * @param scale
	 * @return quotient
	 */
	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static double div(double v1, double v2, double v3, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		BigDecimal b3 = new BigDecimal(Double.toString(v3));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP)
				.divide(b3, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * dividing with integer parameters.
	 * 
	 * @param v1
	 * @param v2
	 * @param scale
	 * @return
	 * @author
	 */
	public static double div(int v1, int v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Integer.toString(v1));
		BigDecimal b2 = new BigDecimal(Integer.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * round processing
	 * 
	 * @param v number
	 * @param scale scale
	 * @return result
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * summary the double array and the return value is accurate
	 * 
	 * @param d
	 * @return
	 * @author
	 */
	public static double sum(double[] d) {
		if (d == null || d.length == 0) {
			return 0D;
		}
		BigDecimal sum = new BigDecimal("0");
		for (int i = 0; i < d.length; i++) {
			sum.add(new BigDecimal(Double.toString(d[i])));
		}
		return sum.doubleValue();
	}

	/**
	 * average the double array and the return value is accurate
	 * 
	 * @param d
	 * @return
	 * @author
	 */
	public static double average(double[] d) {
		if (d == null || d.length == 0) {
			return 0D;
		}
		BigDecimal sum = new BigDecimal("0");
		for (int i = 0; i < d.length; i++) {
			sum.add(new BigDecimal(Double.toString(d[i])));
		}
		sum.divide(new BigDecimal(Integer.toString(d.length)),
				DEFAULT_DIV_SCALE, BigDecimal.ROUND_HALF_UP);
		return sum.doubleValue();
	}

	/**
	 * quickly compute the summary of the double array and the return value is
	 * not accurate
	 * 
	 * @param d
	 * @return
	 * @author
	 */
	public static double quickSum(double[] d) {
		double sum = 0D;
		if (d == null || d.length == 0) {
			return sum;
		}
		for (int i = 0; i < d.length; i++) {
			sum += d[i];
		}
		return sum;
	}

	/**
	 * quickly compute the average of the double array and the return value is
	 * not accurate
	 * 
	 * @param d
	 * @return
	 * @author
	 */
	public static double quickAverage(double[] d) {
		double sum = 0D;
		if (d == null || d.length == 0) {
			return sum;
		}
		for (int i = 0; i < d.length; i++) {
			sum += d[i];
		}
		return (sum / d.length);
	}

	/**
	 * maximum value in double array.
	 * 
	 * @param d
	 * @return
	 * @author
	 */
	public static double max(double[] d) {
		if (d == null || d.length == 0) {
			throw new IllegalArgumentException(
					"double array is null or length equal zero.");
		}
		double max = Double.MIN_VALUE;
		for (int i = 0; i < d.length; i++) {
			if (d[i] > max) {
				max = d[i];
			}
		}
		return max;
	}

	/**
	 * minimum value in double array.
	 * 
	 * @param d
	 * @return
	 * @author
	 */
	public static double min(double[] d) {
		if (d == null || d.length == 0) {
			throw new IllegalArgumentException(
					"double array is null or length is equal to zero.");
		}
		double min = Double.MAX_VALUE;
		for (int i = 0; i < d.length; i++) {
			if (d[i] < min) {
				min = d[i];
			}
		}
		return min;
	}

	/**
	 * maximum value in double array.
	 * 
	 * @param d
	 * @return
	 * @author
	 */
	public static int max(int[] d) {
		if (d == null || d.length == 0) {
			throw new IllegalArgumentException(
					"double array is null or length is equal to zero.");
		}
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < d.length; i++) {
			if (d[i] > max) {
				max = d[i];
			}
		}
		return max;
	}

	/**
	 * minimum value in double array.
	 * 
	 * @param d
	 * @return
	 * @author
	 */
	public static int min(int[] d) {
		if (d == null || d.length == 0) {
			throw new IllegalArgumentException(
					"double array is null or length is equal to zero.");
		}
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < d.length; i++) {
			if (d[i] < min) {
				min = d[i];
			}
		}
		return min;
	}

	/**
	 * 
	 * sign method
	 * 
	 * @param i
	 * @return
	 * @author
	 * @see
	 * @since 2006-1-30 16:19:05
	 */
	public static int sign(int i) {
		if (i > 0) {
			return 1;
		}
		else if (i < 0) {
			return -1;
		}
		return 0;
	}

	/**
	 * 
	 * sign method
	 * 
	 * @param d
	 * @return
	 * @author
	 * @see
	 * @since 2006-1-30 16:19:22
	 */
	public static int sign(double d) {
		if (d > 0) {
			return 1;
		}
		else if (d < 0) {
			return -1;
		}
		return 0;
	}

	/**
	 * return random integer.
	 * 
	 * @return
	 * @author
	 */
	public static int randomInt() {
		return random.nextInt();
	}

	public static int randomInt(int max) {
		return random.nextInt(max);
	}

	public static String randomIntPad(int max) {
		int len = String.valueOf(max).length();
		return StringUtil.leftPad(random.nextInt(max), len - 1);
	}

	public static long randomLong() {
		return random.nextLong();
	}

	/**
	 * return random double.
	 * 
	 * @return
	 * @author
	 */
	public static double randomDouble() {
		return random.nextDouble();
	}

	/**
	 * return random character.
	 * 
	 * @return
	 * @author
	 */
	public static char randomCharacter() {
		Integer i = new Integer(32);
		byte c = (byte) (random.nextDouble() * 95 + 32);
		return (char) c;
	}

	/**
	 * return random letter.
	 * 
	 * @return
	 * @author
	 */
	public static int randomLetter() {
		int i = random.nextInt(RANDOM_SRC_LETTER.length);
		return RANDOM_SRC_LETTER[i];
	}

	public static String randomId() {
		return randomId(16);
	}

	/**
	 * return random ID which include 26 letters and underscore(_), minus(-) and
	 * first character must be letter or _
	 * 
	 * @param len
	 * @return
	 * @author
	 */
	public static String randomId(int len) {
		if (len < ID_MIN_LENGTH) {
			throw new IllegalArgumentException(
					"The minimum size of ID must be " + ID_MIN_LENGTH);
		}
		int length = random.nextInt(len) + 1;
		if (length < ID_MIN_LENGTH) {
			length = ID_MIN_LENGTH;
		}
		char ch;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			ch = RANDOM_SRC_ID[random.nextInt(RANDOM_SRC_ID.length)];
			if (i == 0) {
				while ((ch >= '0' && ch <= '9') || ch == '-') {
					ch = RANDOM_SRC_ID[random.nextInt(RANDOM_SRC_ID.length)];
				}
			}
			sb.append(ch);
		}
		return sb.toString();
	}

	public static void main(String args[]) {
		/*
		 * double d1=1234.12345678012345678909999d; System.out.println(d1);
		 * System.out.println("A decimal:" + Arith.div(1,3670000));
		 * System.out.println("A double :" +(double)(1d/3670000)); long
		 * l=System.currentTimeMillis(); for(int i=0;i<1000000;i++){
		 * Arith.div(1,3670000,18); }
		 * System.out.println(System.currentTimeMillis()-l);
		 * l=System.currentTimeMillis();
		 * 
		 * for(int i=0;i<1000000;i++){ Arith.div(1,3670000,24); }
		 * System.out.println(System.currentTimeMillis()-l);
		 * 
		 * l=System.currentTimeMillis();
		 * 
		 * for(int i=0;i<1000000;i++){ double d=1/3670000d; }
		 * System.out.println(System.currentTimeMillis()-l);
		 * 
		 * System.out.println("B decimal:" +
		 * (107688883434L*9876543L*18976987531L
		 * *Arith.div(1,3670000)+987.2342342)); System.out.println("B double :"
		 * +
		 * (107688883434L*9876543L*18976987531L*(double)(1d/3670000)+987.2342342
		 * ));
		 */
		// double result = ArithmeticUtil.div(3.702 / 100, 360);
		// System.out.println(result);
		int result = ArithmeticUtil.randomInt(50);
		System.out.println(result);

	}
}

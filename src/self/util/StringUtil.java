package self.util;

import java.io.UnsupportedEncodingException;

public class StringUtil {

	/**
	 * 按照字节截取字符串，如果最后一位是半个字符，则不要
	 * 
	 * @param str
	 *            - 原串
	 * @param num
	 *            - 截取的字节数
	 * @return 得到的字符串
	 */
	public static String subStringByBytes(String str, int num) {
		byte[] b1 = str.getBytes();
		char[] c1 = str.toCharArray();
		StringBuilder b2 = new StringBuilder();
		int i = 0;
		int ci = 0;
		while (i < num - 1) {
			if (b1[i] >= '\0') {
				b2.append((char) b1[i]);
				i++;
			} else {
				b2.append(c1[ci]);
				i += 2;
			}
			ci++;
		}
		return b2.toString();
	}

	/**
	 * 在字符串前补0
	 * 
	 * @param value
	 *            - 数据
	 * @param len
	 *            - 补至长度
	 * @return 返回字符串
	 */
	public static String add0BeforeInt(int value, int len) {
		String str = String.valueOf(value);
		if (str.length() >= len)
			return str;
		return "0".repeat(len - str.length()) + str;
	}

	/**
	 * 在字符串后补空格
	 * 
	 * @param str
	 *            - 字符串
	 * @param len
	 *            - 补至长度
	 * @return 返回字符串
	 */
	public static String addSpaceAfterString(String str, int len) {
		int nowLen;
		try {
			nowLen = str.getBytes("gbk").length;
		} catch (UnsupportedEncodingException e) {
			nowLen = str.length();
		}
		if (nowLen >= len)
			return str;
		return str + " ".repeat(len - nowLen);
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 *            - 字符串
	 * @return 是否为空
	 */
	public static boolean isNullStr(String str) {
		if (str == null)
			return true;
		return str.trim().length() == 0;
	}
	
	/**
	 * 查找某字符串第index次出现的位置
	 * @param str - 字符串
	 * @param searchStr - 查找的字符串
	 * @param begin - 开始位置
	 * @param index - 第几次（1,2,3）
	 * @return 存在则返回位置，不存在则返回-1
	 */
	public static int indexOf(String str, String searchStr, int begin, int index) {
		if (index <= 0)
			throw new IndexOutOfBoundsException(String.valueOf(index));
		int loc = begin;
		for (int i = 0; i < index; i++) {
			loc = str.indexOf(searchStr, loc);
			if (loc == -1)
				return -1;
			loc += searchStr.length();
		}
		return loc - searchStr.length();
	}

	/**
	 * 查找某字符串第index次出现的位置
	 * @param str - 字符串
	 * @param searchStr - 查找的字符串
	 * @param index - 第几次（1,2,3）
	 * @return 存在则返回位置，不存在则返回-1
	 */
	public static int indexOf(String str, String searchStr, int index) {
		return indexOf(str, searchStr, 0, index);
	}
	
	/**
	 * 查找某字符第index次出现的位置
	 * @param str - 字符串
	 * @param ch - 查找的字符
	 * @param begin - 开始位置
	 * @param index - 第几次（1,2,3）
	 * @return 存在则返回位置，不存在则返回-1
	 */
	public static int indexOf(String str, int ch, int begin, int index) {
		if (index <= 0)
			throw new IndexOutOfBoundsException(String.valueOf(index));
		int loc = begin;
		for (int i = 0; i < index; i++) {
			loc = str.indexOf(ch, loc);
			if (loc == -1)
				return -1;
			loc ++;
		}
		return loc - 1;
	}

	/**
	 * 查找某字符第index次出现的位置
	 * @param str - 字符串
	 * @param ch - 查找的字符
	 * @param index - 第几次（1,2,3）
	 * @return 存在则返回位置，不存在则返回-1
	 */
	public static int indexOf(String str, int ch, int index) {
		return indexOf(str, ch, 0, index);
	}
	
	/**
	 * 将double转化为字符串，如果没有小数，则不显示小数点
	 */
	public static String toString(double d) {
		if (Double.compare(d, (int) d) == 0) {
			return String.valueOf((int) d);
		} else {
			return String.valueOf(d);
		}
	}
}

package self.util;

/**
 * 一个对数组进行操作的工具类
 */
public class ArrayUtil {

	private ArrayUtil() {
	}

	/**
	 * 返回一个数组，是数组array的子串，从下标from到to-1
	 * 
	 * @param array
	 *            - 待操作的数组
	 * @param from
	 *            - 复制的起始位置
	 * @param to
	 *            - 结束位置的后一位
	 * @return 得到的子串
	 * @throws IllegalArgumentException
	 *             如果from > to
	 */
	public static int[] subArrayOf(int[] array, int from, int to) {
		int len = to - from;
		if (len < 0)
			throw new IllegalArgumentException(from + " > " + to);
		int[] newArray = new int[len];
		System.arraycopy(array, from, newArray, 0, len);
		return newArray;
	}

	/**
	 * 返回一个数组，是数组array的子串，从下标from到to-1
	 * 
	 * @param array
	 *            - 待操作的数组
	 * @param from
	 *            - 复制的起始位置
	 * @param to
	 *            - 结束位置的后一位
	 * @return 得到的子串
	 * @throws IllegalArgumentException
	 *             如果from > to
	 */
	public static String[] subArrayOf(String[] array, int from, int to) {
		int len = to - from;
		if (len < 0)
			throw new IllegalArgumentException(from + " > " + to);
		String[] newArray = new String[len];
		System.arraycopy(array, from, newArray, 0, len);
		return newArray;
	}
}

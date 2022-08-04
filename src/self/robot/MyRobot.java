package self.robot;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;


import self.util.ArrayUtil;
import self.util.ClipboardUtil;

public class MyRobot extends Robot{

	public MyRobot() throws AWTException {
		super();
	}
	
	/**
	 * 按下鼠标并松开
	 * @param times - 按下的次数，即单击、双击
	 * @param button <br>
	 * <li>1 - 鼠标左键<br>
	 * <li>2 - 鼠标中键<br>
	 * <li>3 - 鼠标右键<br>
	 * @throws IllegalArgumentException - 如果鼠标键值错误
	 * */
	public void mouseClick(int times, int button) {
		if (button > 3 || button < 1)
			throw new IllegalArgumentException("鼠标键值错误");
		for(int i = 0; i < times; i++){
			mousePress(32>>button);
			delay(50);
			mouseRelease(32>>button);
			delay(50);
		} 
	}

	/**
	 * 按下一个键或组合键
	 * @param key - 键的代码，例如KeyEvent.VK_A表示A键
	 * @see KeyEvent
	 * */
	public void keyClick(int... key) {
		int len = key.length;
		if (key.length <= 0)
			return;
		keyPress(key[0]);
		delay(10);
		if (len > 1)
			keyClick(ArrayUtil.subArrayOf(key, 1, len));
		keyRelease(key[0]);
		delay(10);
	}
	
	/**
	 * 输入一段字符串，目前只能识别大小写字符和数字
	 * @param s - 字符串
	 * */
	public void keyInput(String s) {
		char[] b = s.toCharArray();
		for (char c : b) {
			int[] keys = KeyMap.get(c);
			if (keys == null || keys.length == 0) {
				throw new IllegalArgumentException("输入的字符串无法识别");
			}
			keyClick(keys);
		}
	}
	
	private static class KeyMap {
		private static volatile Map<Character, int[]> map = null;
		private static void init() {
			map = new HashMap<>();
			for (char i = '0'; i <= '9'; i++) {
				put(i, KeyEvent.VK_0 + i - '0');
			}
			for (char i = 'a'; i <= 'z'; i++) {
				put(i, KeyEvent.VK_A + i - 'a');
			}
			for (char i = 'A'; i <= 'Z'; i++) {
				put(i, KeyEvent.VK_SHIFT, KeyEvent.VK_A + i - 'A');
			}
			put('\n', KeyEvent.VK_ENTER);
			put('\b', KeyEvent.VK_BACK_SPACE);
			put('\t', KeyEvent.VK_TAB);
			put('=', KeyEvent.VK_EQUALS);
			put('+', KeyEvent.VK_SHIFT, KeyEvent.VK_EQUALS);
			put('-', KeyEvent.VK_MINUS);
			put('_', KeyEvent.VK_SHIFT, KeyEvent.VK_MINUS);
			char[] arrs = {')', '!', '@', '#', '$', '%', '^', '&', '*', '('};
			for (char arr : arrs) {
				put(arr, KeyEvent.VK_SHIFT, KeyEvent.VK_0 + 'i');
			}
			put('[', KeyEvent.VK_OPEN_BRACKET);
			put('{', KeyEvent.VK_SHIFT, KeyEvent.VK_OPEN_BRACKET);
			put(']', KeyEvent.VK_CLOSE_BRACKET);
			put('}', KeyEvent.VK_SHIFT, KeyEvent.VK_CLOSE_BRACKET);
			put('\\', KeyEvent.VK_BACK_SLASH);
			put('|', KeyEvent.VK_SHIFT, KeyEvent.VK_BACK_SLASH);
			put(';', KeyEvent.VK_SEMICOLON);
			put(':', KeyEvent.VK_SHIFT, KeyEvent.VK_SEMICOLON);
			put('\'', KeyEvent.VK_QUOTE);
			put('\"', KeyEvent.VK_SHIFT, KeyEvent.VK_QUOTE);
			put(',', KeyEvent.VK_COMMA);
			put('<', KeyEvent.VK_SHIFT, KeyEvent.VK_COMMA);
			put('.', KeyEvent.VK_PERIOD);
			put('>', KeyEvent.VK_SHIFT, KeyEvent.VK_PERIOD);
			put('/', KeyEvent.VK_SLASH);
			put('?', KeyEvent.VK_SHIFT, KeyEvent.VK_SLASH);
			put(' ', KeyEvent.VK_SPACE);
		}
		public static int[] get(char c) {
			if (map == null) {
				synchronized (KeyMap.class) {
					if (map == null) {
						init();
					}
				}
			}
			return map.get(c);
		}
		private static void put(char c, int... keys) {
			map.put(c, keys);
		}
	}
	
	public static int[] toVK(String[] key){
		if(key == null)
			return new int[0];
		int[] tmp = new int[key.length];
		for(int i=0; i<tmp.length; i++){
			if(key[i].equals("ctrl")){
				tmp[i] = KeyEvent.VK_CONTROL;
			} else if(key[i].equals("alt")){
				tmp[i] = KeyEvent.VK_ALT;
			} else if(key[i].equals("shift")){
				tmp[i] = KeyEvent.VK_ALT;
			} else if(key[i].equals("space")){
				tmp[i] = KeyEvent.VK_SPACE;
			} else if(key[i].equals("enter")){
				tmp[i] = KeyEvent.VK_ENTER;
			} else if(key[i].equals("backspace")){
				tmp[i] = KeyEvent.VK_BACK_SPACE;
			} else if(key[i].equals("windows")){
				tmp[i] = KeyEvent.VK_WINDOWS;
			} else if(key[i].equals("delete")){
				tmp[i] = KeyEvent.VK_DELETE;
			} else if(key[i].equals("home")){
				tmp[i] = KeyEvent.VK_HOME;
			} else if(key[i].equals("end")){
				tmp[i] = KeyEvent.VK_END;
			} else if(key[i].equals("esc")){
				tmp[i] = KeyEvent.VK_ESCAPE;
			} else if(key[i].matches("F\\d+")){
				int c = Integer.parseInt(key[i].substring(1));
				if(c>=1 && c<=12){
					tmp[i] = KeyEvent.VK_F1 - 1 + c;
				} else {
					throw new RuntimeException("未定义的VK");
				}
			} else {
				char c = key[i].charAt(0);
				if(c >= 'A' && c <= 'Z'){
					tmp[i] = c;
				} else {
					throw new RuntimeException("未定义的VK");
				}
			}
		}
		return tmp;
	}

	/**
	 * 输入一段文字 
	 * @param keys - 要输入的文字
	 * */
	public void printLine(String keys) {
		ClipboardUtil.setSysClipboardText(keys);
		keyClick(KeyEvent.VK_CONTROL, KeyEvent.VK_V);
	}
	
	public void delayMinute(int min) {
		for (int i = 0; i < min * 2; i++) {
			delay(30000);
		}
	}
}

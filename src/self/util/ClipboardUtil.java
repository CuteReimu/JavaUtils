package self.util;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ClipboardUtil {

	private ClipboardUtil() {
	}

	/**
	 * 获取剪切板中的文本
	 * 
	 * @return 剪切板中的文本，若剪切板中无内容或不是文本，则返回null
	 */
	public static String getSysClipboardText() {
		String ret = null;
		Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
		// 获取剪切板中的内容
		Transferable clipTf = sysClip.getContents(null);

		if (clipTf != null) {
			// 检查内容是否是文本类型
			if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				try {
					ret = (String) clipTf.getTransferData(DataFlavor.stringFlavor);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return ret;
	}

	/**
	 * 向剪切板中写文字
	 * 
	 * @param str
	 *            - 要写入的文字
	 */
	public static void setSysClipboardText(String str) {
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable tText = new StringSelection(str);
		clip.setContents(tText, null);
	}

	/**
	 * 从剪切板中获取图片
	 * 
	 * @return 获取的图片，若剪切板中无内容或不是图片，则返回null
	 */
	public static BufferedImage getImageFromClipboard() {
		BufferedImage tmp = null;
		Clipboard sysc = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable cc = sysc.getContents(null);
		if (cc != null && cc.isDataFlavorSupported(DataFlavor.imageFlavor)) {
			try {
				tmp = (BufferedImage) cc.getTransferData(DataFlavor.imageFlavor);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return tmp;
	}

	/**
	 * 向剪切板中写入图片
	 * 
	 * @param image
	 *            - 要写入的图片
	 */
	public static void setClipboardImage(final Image image) {
		Transferable trans = new Transferable() {
			public DataFlavor[] getTransferDataFlavors() {
				return new DataFlavor[] { DataFlavor.imageFlavor };
			}

			public boolean isDataFlavorSupported(DataFlavor flavor) {
				return DataFlavor.imageFlavor.equals(flavor);
			}

			public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
				if (isDataFlavorSupported(flavor))
					return image;
				throw new UnsupportedFlavorException(flavor);
			}

		};
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(trans, null);
	}
}

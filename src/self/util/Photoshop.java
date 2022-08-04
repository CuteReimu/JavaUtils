package self.util;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Photoshop {
	private Photoshop() {
	}
	
	/**
	 * 正片叠底
	 */
	public static BufferedImage multiply(BufferedImage... imgs) {
		if (imgs.length == 0)
			throw new PhotoShopException();
		int[] maxSize = getMaxSize(imgs);
		BufferedImage image = new BufferedImage(maxSize[0], maxSize[1], imgs[0].getType());
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				float a = 255.0f, r = 255.0f, g = 255.0f, b = 255.0f;
				for (BufferedImage img : imgs) {
					if (x < img.getWidth() && y < img.getHeight()) {
						int rgb = img.getRGB(x, y);
						a *= ((rgb >>> 24) & 0xff) / 255.0f;
						r *= ((rgb >>> 16) & 0xff) / 255.0f;
						g *= ((rgb >>> 8) & 0xff) / 255.0f;
						b *= (rgb & 0xff) / 255.0f;
					}
				}
				image.setRGB(x, y, Math.round(a) << 24 | Math.round(r) << 16 | Math.round(g) << 8 | Math.round(b));
			}
		}
		return image;
	}
	
	/**
	 * 滤色
	 */
	public static BufferedImage screen(BufferedImage... imgs) {
		if (imgs.length == 0)
			throw new PhotoShopException();
		int[] maxSize = getMaxSize(imgs);
		BufferedImage image = new BufferedImage(maxSize[0], maxSize[1], imgs[0].getType());
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				float a = 255.0f, r = 255.0f, g = 255.0f, b = 255.0f;
				for (BufferedImage img : imgs) {
					if (x < img.getWidth() && y < img.getHeight()) {
						int rgb = img.getRGB(x, y);
						a *= 1 - ((rgb >>> 24) & 0xff) / 255.0f;
						r *= 1 - ((rgb >>> 16) & 0xff) / 255.0f;
						g *= 1 - ((rgb >>> 8) & 0xff) / 255.0f;
						b *= 1 - (rgb & 0xff) / 255.0f;
					}
				}
				image.setRGB(x, y, Math.round(255.0f - a) << 24 | Math.round(255.0f - r) << 16 | Math.round(255.0f - g) << 8 | Math.round(255.0f - b));
			}
		}
		return image;
	}
	
	private static int[] getMaxSize(BufferedImage... imgs) {
		int width = 0, height = 0;
		for (BufferedImage img : imgs) {
			if (img.getWidth() > width)
				width = img.getWidth();
			if (img.getHeight() > height)
				height = img.getHeight();
		}
		return new int[]{width, height};
	}
	
	public static BufferedImage lineGradChgColor(int width, int height, int imgType, Color[] c, double[] percent, Rectangle line) {
		if (c.length != percent.length || c.length == 0)
			throw new PhotoShopException(c.length + " : " + percent.length);
		double syn = percent[percent.length - 1];
		for (int i = 1; i < percent.length; i++) {
			if (percent[i - 1] > percent[i])
				throw new PhotoShopException(Arrays.toString(percent));
		}
		if (Double.compare(syn, 1.0) != 0) {
			for (int i = 0; i < percent.length; i++)
				percent[i] /= syn;
		}
		double angle = Math.atan2(line.height, line.width);
		double lineLen = Math.sqrt(line.width * line.width + line.height * line.height);
		BufferedImage img = new BufferedImage(width, height, imgType);
		for (int i = 0; i < width; i++) {
			double x = i - line.x;
			for (int j = 0; j < height; j++) {
				double y = j - line.y;
				if (x == 0 && y == 0) {
					img.setRGB(i, j, c[0].getAlpha() << 24 | c[0].getRed() << 16 | c[0].getGreen() << 8 | c[0].getBlue());
					continue;
				}
				double per = Math.cos(Math.atan2(y, x) - angle) * Math.sqrt(x * x + y * y) / lineLen;
				if (per < percent[0]) {
					img.setRGB(i, j, c[0].getAlpha() << 24 | c[0].getRed() << 16 | c[0].getGreen() << 8 | c[0].getBlue());
					continue;
				}
				for (int n = 0; n < percent.length - 1; n++) {
					if (percent[n] == per || n == percent.length - 1) {
						img.setRGB(i, j, c[n].getAlpha() << 24 | c[n].getRed() << 16 | c[n].getGreen() << 8 | c[n].getBlue());
						break;
					} else if (Double.compare(percent[n], per) < 0 && Double.compare(per, percent[n + 1]) < 0) {
						double per1 = (percent[n + 1] - per) / (percent[n + 1] - percent[n]);
						double per2 = 1.0 - per1;
						int a = (int) Math.round(c[n].getAlpha() * per1 + c[n + 1].getAlpha() * per2);
						int r = (int) Math.round(c[n].getRed() * per1 + c[n + 1].getRed() * per2);
						int g = (int) Math.round(c[n].getGreen() * per1 + c[n + 1].getGreen() * per2);
						int b = (int) Math.round(c[n].getBlue() * per1 + c[n + 1].getBlue() * per2);
						img.setRGB(i, j, a << 24 | r << 16 | g << 8 | b);
						break;
					}
				}
			}
		}
		return img;
	}
	
	public static class PhotoShopException extends RuntimeException {
		public PhotoShopException(){
			super();
		}
	    public PhotoShopException(String message) {
	    	super(message);
	    }
	    public PhotoShopException(String message, Throwable cause) {
	        super(message, cause);
	    }
	    public PhotoShopException(Throwable cause) {
	        super(cause);
	    }
	}
}

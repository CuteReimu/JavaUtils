package self.util.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import self.util.ImageUtil;

public class AnimatedImage implements Iterable<BufferedImage> {

	private final List<Frame> frames = new ArrayList<>();
	private int loopCount;
	private int width;
	private int height;
	private Color background = Color.BLACK;
	
	public AnimatedImage() {
	}
	
	public AnimatedImage(String filePath) throws IOException {
		load(new File(filePath));
	}
	
	public AnimatedImage(File file) throws IOException {
		load(file);
	}
	
	public void load(String filePath) throws IOException {
		load(new File(filePath));
	}
	
	public void load(File file) throws IOException {
		if (file == null)
			throw new IOException("sourceFile未配置");
		if (!file.exists())
			throw new FileNotFoundException(file.getPath());
		String formatName = ImageUtil.getImageFormatName(file);
		if (formatName == null)
			throw new IOException("打开文件失败：" + file.getPath());
		if (!"gif".equals(formatName))
			throw new IOException("不是gif文件：" + file.getPath());
		GifDecoder decoder = new GifDecoder();
		int status = decoder.read(file.getPath());
		if (status != GifDecoder.STATUS_OK) {
			throw new IOException("读取图片失败：" + file.getPath());
		}
		loopCount = decoder.getLoopCount();
		frames.clear();
		for (int i = 0; i < decoder.getFrameCount(); i++) {
			frames.add(new Frame(decoder.getFrame(i), decoder.getDelay(i)));
		}
		adjustFrameSize();
	}
	
	public int getFrameCount() {
		return frames.size();
	}
	
	private void adjustFrameSize() {
		width = 0;
		height = 0;
		for (Frame f : frames) {
			if (f.img.getWidth() > width)
				width = f.img.getWidth();
			if (f.img.getHeight() > height)
				height = f.img.getHeight();
		}
		for (Frame f : frames) {
			if (f.img.getWidth() < width || f.img.getHeight() < height)
				f.img = ImageUtil.zoomGraphics(f.img, 0, 0, width, height, background);
		}
	}
	
	public void save(String filePath) {
		save(new File(filePath));
	}
	
	public void save(File file) {
		if (frames.isEmpty())
			throw new NullPointerException();
		if (loopCount < 0)
			throw new IllegalArgumentException("loopCount:" + loopCount);
		if (width <= 0 || height <= 0)
			throw new IllegalArgumentException("width:" + width + ", height:" + height);
		AnimatedGifEncoder encoder = new AnimatedGifEncoder();
		encoder.start(file.getPath());
		encoder.setRepeat(loopCount);
		for (Frame f : frames) {
			encoder.setDelay(f.delay);
			encoder.addFrame(f.img);
		}
		encoder.finish();
	}
	
	public void setFrame(int i, BufferedImage img) {
		if (img.getWidth() == width && img.getHeight() == height) {
			frames.get(i).img = img;
		} else if (img.getWidth() <= width && img.getHeight() <= height) {
			frames.get(i).img = ImageUtil.zoomGraphics(img, 0, 0, width, height, background);
		} else {
			frames.get(i).img = img;
			adjustFrameSize();
		}
	}
	
	public void setDelay(int i, int ms) {
		frames.get(i).delay = ms;
	}
	
	public BufferedImage getFrame(int i) {
		return frames.get(i).img;
	}
	
	public int getDelay(int i) {
		return frames.get(i).delay;
	}
	
	public void addFrame(int i, BufferedImage img, int ms) {
		if (img.getWidth() == width && img.getHeight() == height) {
			frames.add(i, new Frame(img, ms));
		} else if (img.getWidth() <= width && img.getHeight() <= height) {
			frames.add(i, new Frame(ImageUtil.zoomGraphics(img, 0, 0, width, height, background), ms));
		} else {
			frames.add(i, new Frame(img, ms));
			adjustFrameSize();
		}
	}
	
	public void addFrame(BufferedImage img, int ms) {
		addFrame(frames.size(), img, ms);
	}
	
	public Frame removeFrame(int i) {
		return frames.remove(i);
	}
	
	public int getLoopCount() {
		return loopCount;
	}

	public void setLoopCount(int loopCount) {
		this.loopCount = loopCount;
	}

	public Color getBackground() {
		return background;
	}

	public void setBackground(Color background) {
		this.background = background;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	private static class Frame {
		public BufferedImage img;
		public int delay;
		public Frame(BufferedImage img, int delay) {
			this.img = img;
			this.delay = delay;
		}
	}

	@Override
	public Iterator<BufferedImage> iterator() {
		return new Iterator<>() {
			private int cursor;

			public boolean hasNext() {
				return cursor < frames.size();
			}

			public BufferedImage next() {
				BufferedImage img = frames.get(cursor).img;
				cursor++;
				return img;
			}

			public void remove() {
				int i = cursor - 1;
				removeFrame(i);
				this.cursor = i;
			}
		};
	}
}

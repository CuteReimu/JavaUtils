package self.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import self.util.image.AnimatedImage;

/**
 * 图像处理的一些工具
 */
public class ImageUtil {

    private ImageUtil() {
    }

    /**
     * 判断a图像和b图像是否完全相同
     *
     * @return 是否相同
     */
    public static boolean equals(BufferedImage a, BufferedImage b) {
        int width = a.getWidth();
        int height = a.getHeight();
        if (width != b.getWidth() || height != b.getHeight())
            return false;
        int[] colorA = a.getRGB(0, 0, width, height, null, 0, width);
        int[] colorB = b.getRGB(0, 0, width, height, null, 0, width);
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                if (colorA[j * width + i] != colorB[j * width + i]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断从a图像的(x, y)位置的子图像和b图像是否相同
     *
     * @return 是否相同
     */
    private static boolean equals(BufferedImage a, int x, int y, BufferedImage b) {
        int width = b.getWidth();
        int height = b.getHeight();
        if (a.getWidth() - x < width || a.getHeight() - y < height)
            return false;
        int[] colorA = a.getRGB(x, y, width, height, null, 0, width);
        int[] colorB = b.getRGB(0, 0, width, height, null, 0, width);
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                if (colorA[j * width + i] != colorB[j * width + i])
                    return false;
            }
        }
        return true;
    }

    /**
     * 在outer图像中，从(x, y)位置开始搜索inner图像
     *
     * @return outer图像中inner图像从(x, y)位置开始的从左向右从上向下第一个坐标，如果不存在则返回null
     */
    public static Rectangle search(BufferedImage outer, int x, int y, BufferedImage inner) {
        int width = outer.getWidth() - inner.getWidth() + 1;
        int height = outer.getHeight() - inner.getHeight() + 1;
        for (int j = y; j < height; j++) {
            for (int i = x; i < width; i++) {
                if (equals(outer, i, j, inner))
                    return new Rectangle(i, j, inner.getWidth(), inner.getHeight());
            }
        }
        return null;
    }

    /**
     * 转化为灰度图像
     */
    public static void toGray(BufferedImage img) {
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                int rgb = img.getRGB(i, j);
                int[] argb = {rgb & 0xff, rgb >>> 8 & 0xff, rgb >>> 16 & 0xff};
                int ave = (argb[0] + argb[1] + argb[2] + 1) / 3;
                img.setRGB(i, j, ave | ave << 8 | ave << 16 | 0xff << 24);
            }
        }
    }

    /**
     * 转化为灰度图像
     */
    public static void toGray(AnimatedImage img) {
        for (int i = 0; i < img.getFrameCount(); i++) {
            toGray(img.getFrame(i));
        }
    }

    /**
     * 反色
     */
    public static void inverse(BufferedImage img) {
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                int rgb = img.getRGB(i, j);
                int[] argb = {rgb & 0xff, rgb >>> 8 & 0xff, rgb >>> 16 & 0xff};
                for (int k = 0; k < 3; k++) {
                    argb[k] = 255 - argb[k];
                }
                img.setRGB(i, j, argb[0] | argb[1] << 8 | argb[2] << 16 | 0xff << 24);
            }
        }
    }

    /**
     * 反色
     */
    public static void inverse(AnimatedImage img) {
        for (int i = 0; i < img.getFrameCount(); i++) {
            inverse(img.getFrame(i));
        }
    }

    /**
     * 水平翻转
     */
    public static void flipH(BufferedImage img) {
        BufferedImage image = clone(img);
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                img.setRGB(image.getWidth() - 1 - x, y, image.getRGB(x, y));
            }
        }
    }

    /**
     * 水平翻转
     */
    public static void flipH(AnimatedImage img) {
        for (int i = 0; i < img.getFrameCount(); i++) {
            flipH(img.getFrame(i));
        }
    }

    /**
     * 垂直翻转
     */
    public static void flipV(BufferedImage img) {
        BufferedImage image = clone(img);
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                img.setRGB(x, image.getHeight() - 1 - y, image.getRGB(x, y));
            }
        }
    }

    /**
     * 垂直翻转
     */
    public static void flipV(AnimatedImage img) {
        for (int i = 0; i < img.getFrameCount(); i++) {
            flipV(img.getFrame(i));
        }
    }

    /**
     * 获取图片格式
     *
     * @param file 图片文件
     * @return <li>bmp</li>
     * <li>jpg</li>
     * <li>wbmp</li>
     * <li>jpeg</li>
     * <li>png</li>
     * <li>gif</li>
     */
    public static String getImageFormatName(File file) throws IOException {
        String formatName = null;

        ImageInputStream iis = ImageIO.createImageInputStream(file);
        Iterator<ImageReader> imageReader = ImageIO.getImageReaders(iis);
        if (imageReader.hasNext()) {
            ImageReader reader = imageReader.next();
            formatName = reader.getFormatName().toLowerCase();
        }

        return formatName;
    }

    /**
     * 获取系统支持的图片格式
     */
    public static void getOSSupportsStandardImageFormat() {
        String[] readerFormatName = ImageIO.getReaderFormatNames();
        String[] readerSuffixName = ImageIO.getReaderFileSuffixes();
        String[] readerMIMEType = ImageIO.getReaderMIMETypes();
        System.out.println("========================= OS supports reader ========================");
        System.out.println("OS supports reader format name :  " + Arrays.asList(readerFormatName));
        System.out.println("OS supports reader suffix name :  " + Arrays.asList(readerSuffixName));
        System.out.println("OS supports reader MIME type :  " + Arrays.asList(readerMIMEType));

        String[] writerFormatName = ImageIO.getWriterFormatNames();
        String[] writerSuffixName = ImageIO.getWriterFileSuffixes();
        String[] writerMIMEType = ImageIO.getWriterMIMETypes();

        System.out.println("========================= OS supports writer ========================");
        System.out.println("OS supports writer format name :  " + Arrays.asList(writerFormatName));
        System.out.println("OS supports writer suffix name :  " + Arrays.asList(writerSuffixName));
        System.out.println("OS supports writer MIME type :  " + Arrays.asList(writerMIMEType));
    }

    /**
     * 放大或缩小图片
     *
     * @param sourceImage 待处理图片
     * @param width       新图片高度
     * @param height      新图片宽度
     * @return 新图片
     */
    public static BufferedImage zoom(BufferedImage sourceImage, int width, int height) {
        if (sourceImage.getHeight() == height && sourceImage.getWidth() == width)
            return sourceImage;
        BufferedImage zoomImage = new BufferedImage(width, height, sourceImage.getType());
        Image image = sourceImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        Graphics gc = zoomImage.getGraphics();
//		gc.setColor(Color.WHITE);
        gc.drawImage(image, 0, 0, null);
        return zoomImage;
    }

    /**
     * 放大或缩小图片
     *
     * @param sourceImage 待处理图片
     * @param rate        放大比例
     * @return 新图片
     */
    public static BufferedImage zoom(BufferedImage sourceImage, float rate) {
        return zoom(sourceImage, Math.round(sourceImage.getWidth() * rate),
                Math.round(sourceImage.getHeight() * rate));
    }

    /**
     * 放大或缩小图片
     *
     * @param img  待处理图片
     * @param rate 放大比例
     * @return 新图片
     */
    public static AnimatedImage zoom(AnimatedImage img, float rate) {
        AnimatedImage image = new AnimatedImage();
        image.setBackground(img.getBackground());
        image.setLoopCount(img.getLoopCount());
        for (int i = 0; i < img.getFrameCount(); i++) {
            image.addFrame(zoom(img.getFrame(i), rate), img.getDelay(i));
        }
        return image;
    }

    /**
     * 放大或缩小图片
     *
     * @param img    待处理图片
     * @param width  新图片高度
     * @param height 新图片宽度
     * @return 新图片
     */
    public static AnimatedImage zoom(AnimatedImage img, int width, int height) {
        AnimatedImage image = new AnimatedImage();
        image.setBackground(img.getBackground());
        image.setLoopCount(img.getLoopCount());
        for (int i = 0; i < img.getFrameCount(); i++) {
            image.addFrame(zoom(img.getFrame(i), width, height), img.getDelay(i));
        }
        return image;
    }

    /**
     * 旋转图片
     *
     * @param sourceImage 待处理图片
     * @param clockwise   是否顺时针
     *                    <li>1 - 顺时针</li>
     *                    <li>2 - 180°</li>
     *                    <li>3 - 逆时针</li>
     * @return 新图片
     */
    public static BufferedImage rotate(BufferedImage sourceImage, int clockwise) {
        BufferedImage image;
        switch (clockwise % 4) {
            case 0:
                return sourceImage;
            case 2:
                image = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), sourceImage.getType());
                break;
            default:
                image = new BufferedImage(sourceImage.getHeight(), sourceImage.getWidth(), sourceImage.getType());
        }
        while (clockwise < 0) {
            clockwise += 4;
        }
        for (int i = 0; i < sourceImage.getWidth(); i++) {
            for (int j = 0; j < sourceImage.getHeight(); j++) {
                switch (clockwise % 4) {
                    case 1 -> image.setRGB(sourceImage.getHeight() - j - 1, i, sourceImage.getRGB(i, j));
                    case 2 ->
                            image.setRGB(sourceImage.getWidth() - i - 1, sourceImage.getHeight() - j - 1, sourceImage.getRGB(i, j));
                    case 3 -> image.setRGB(j, sourceImage.getWidth() - i - 1, sourceImage.getRGB(i, j));
                }
            }
        }
        return image;
    }

    /**
     * 旋转图片
     *
     * @param img       待处理图片
     * @param clockwise 是否顺时针
     *                  <li>1 - 顺时针</li>
     *                  <li>2 - 180°</li>
     *                  <li>3 - 逆时针</li>
     * @return 新图片
     */
    public static AnimatedImage rotate(AnimatedImage img, int clockwise) {
        AnimatedImage image = new AnimatedImage();
        image.setBackground(img.getBackground());
        image.setLoopCount(img.getLoopCount());
        for (int i = 0; i < img.getFrameCount(); i++) {
            image.addFrame(rotate(img.getFrame(i), clockwise), img.getDelay(i));
        }
        return image;
    }

    /**
     * 放大或缩小画布
     *
     * @param sourceImage 待处理图片
     * @param x           图片的左上角相对于新画布左上角的横坐标
     * @param y           图片的左上角相对于新画布左上角的纵坐标
     * @param width       新画布高度
     * @param height      新画布宽度
     * @param background  画布背景颜色
     * @return 新图片
     */
    public static BufferedImage zoomGraphics(BufferedImage sourceImage, int x, int y, int width, int height, Color background) {
        BufferedImage zoomImage = new BufferedImage(width, height, sourceImage.getType());
        Graphics gc = zoomImage.getGraphics();
        if (background != null) {
            gc.setColor(background);
            gc.fillRect(0, 0, width, height);
        }
        Rectangle cutArea = new Rectangle(0, 0, sourceImage.getWidth(), sourceImage.getHeight());
        if (x < 0) {
            cutArea.x = -x;
            cutArea.width -= cutArea.x;
        }
        if (y < 0) {
            cutArea.y = -y;
            cutArea.height -= cutArea.y;
        }
        if (x + sourceImage.getWidth() > width) {
            cutArea.width -= x + sourceImage.getWidth() - width;
            cutArea.width = Math.max(cutArea.width, 0);
        }
        if (x + sourceImage.getHeight() > height) {
            cutArea.height -= y + sourceImage.getHeight() - height;
            cutArea.height = Math.max(cutArea.height, 0);
        }
        if (cutArea.width > 0 && cutArea.height > 0)
            sourceImage = sourceImage.getSubimage(cutArea.x, cutArea.y, cutArea.width, cutArea.height);
        gc.drawImage(sourceImage, Math.max(x, 0), Math.max(y, 0), null);
        return zoomImage;
    }

    public static BufferedImage clone(BufferedImage img) {
        BufferedImage image = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        for (int i = 0; i < image.getWidth(); i++)
            for (int j = 0; j < image.getHeight(); j++)
                image.setRGB(i, j, img.getRGB(i, j));
        return image;
    }

    public static void mosaic(BufferedImage img, int px) {
        mosaic(img, 0, 0, img.getWidth(), img.getHeight(), px);
    }

    public static void mosaic(BufferedImage img, int x, int y, int width, int height, int px) {
        if (px < 0)
            throw new IllegalArgumentException(px + " px");
        else if (px <= 1)
            return;
        BufferedImage image = clone(img);
        for (int i = 0; i * px < width; i++) {
            for (int j = 0; j * px < height; j++) {
                int a = 0;
                int r = 0;
                int g = 0;
                int b = 0;
                int count = 0;
                for (int xx = 0; xx < px; xx++) {
                    int xi = i * px + xx;
                    if (xi >= x + width)
                        break;
                    for (int yy = 0; yy < px; yy++) {
                        int yi = j * px + yy;
                        if (yi >= y + height)
                            break;
                        Color c = new Color(image.getRGB(xi, yi));
                        a += c.getAlpha();
                        r += c.getRed();
                        g += c.getGreen();
                        b += c.getBlue();
                        count++;
                    }
                }
                a = Math.round(a / (float) count);
                r = Math.round(r / (float) count);
                g = Math.round(g / (float) count);
                b = Math.round(b / (float) count);
                for (int xx = 0; xx < px; xx++) {
                    int xi = i * px + xx;
                    if (xi >= x + width)
                        break;
                    for (int yy = 0; yy < px; yy++) {
                        int yi = j * px + yy;
                        if (yi >= y + height)
                            break;
                        img.setRGB(xi, yi, a << 24 | r << 16 | g << 8 | b);
                    }
                }
            }
        }
    }

    private static final Map<Integer, double[]> gaussianMap = new HashMap<>();

    /**
     * 高斯模糊
     *
     * @param px    模糊半径
     */
    public static void gaussianBlur(BufferedImage image, int px) {
        if (px == 0)
            return;
        else if (px < 0)
            throw new IllegalArgumentException(px + " px");
        BufferedImage img = clone(image);
        if (!gaussianMap.containsKey(px)) {
            synchronized (ImageUtil.class) {
                if (!gaussianMap.containsKey(px)) {
                    gaussianMap.put(px, gaussian(px));
                }
            }
        }
        double[] gauss = gaussianMap.get(px);
        gaussianLineBlur(img, gauss, true, image);
        gaussianLineBlur(image, gauss, false, img);
    }

    private static void gaussianLineBlur(BufferedImage image, double[] gauss, boolean H, BufferedImage img) {
        int px = (gauss.length - 1) / 2;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                double a = 0;
                double r = 0;
                double g = 0;
                double b = 0;
                for (int xx = 0; xx < gauss.length; xx++) {
                    int xi, yi;
                    if (H) {
                        xi = x + xx - px;
                        yi = y;
                    } else {
                        xi = x;
                        yi = y + xx - px;
                    }
                    Color c;
                    if (yi >= 0 && yi < img.getHeight() && xi >= 0 && xi < img.getWidth())
                        c = new Color(img.getRGB(xi, yi));
                    else
                        c = Color.GRAY;
                    a += c.getAlpha() * gauss[xx];
                    r += c.getRed() * gauss[xx];
                    g += c.getGreen() * gauss[xx];
                    b += c.getBlue() * gauss[xx];
                }
                image.setRGB(x, y, (int) Math.round(b) | (int) Math.round(g) << 8 | (int) Math.round(r) << 16 | (int) Math.round(a) << 24);
            }
        }
    }

    private static double[] gaussian(double r) {
        int num = (int) r + 1;
        double[] result = new double[2 * num + 1];
        r = r / 3;
        for (int i = 0; i <= num; i++) {
            int x = num - i;
            double b = x * x;
            double c = Math.exp(-b / (r * r) / 2);
            result[i] = c;
            result[2 * num - i] = c;
        }
        double total = 0;
        for (double v : result) {
            total += v;
        }
        for (int i = 0; i < result.length; i++) {
            result[i] /= total;
        }
        return result;
    }

    /**
     * RGB转化为HSB
     *
     * @param rgbR 0 - 255
     * @param rgbG 0 - 255
     * @param rgbB 0 - 255
     * @return <b>h</b> 0 - 360<br>
     * <b>s</b> 0 - 1<br>
     * <b>v</b> 0 - 1
     */
    public static float[] rgb2hsb(int rgbR, int rgbG, int rgbB) {
        if (rgbR < 0 || rgbR > 255 || rgbG < 0 || rgbG > 255 || rgbB < 0 || rgbB > 255)
            throw new IllegalArgumentException("R:" + rgbR + ", G:" + rgbG + ", B:" + rgbB);

        int[] rgb = new int[]{rgbR, rgbG, rgbB};
        Arrays.sort(rgb);
        int max = rgb[2];
        int min = rgb[0];

        float hsbB = max / 255.0f;
        float hsbS = max == 0 ? 0 : (max - min) / (float) max;

        float hsbH = 0;
        float v = (rgbG - rgbB) * 60f / (max - min);
        if (max == rgbR && rgbG >= rgbB) {
            hsbH = v + 0;
        } else if (max == rgbR) {
            hsbH = v + 360;
        } else if (max == rgbG) {
            hsbH = (rgbB - rgbR) * 60f / (max - min) + 120;
        } else if (max == rgbB) {
            hsbH = (rgbR - rgbG) * 60f / (max - min) + 240;
        }

        return new float[]{hsbH, hsbS, hsbB};
    }

    /**
     * HSB转化为RGB
     *
     * @param h 色调 0 - 360 其实无所谓
     * @param s 饱和度 0 - 1
     * @param v 亮度 0 - 1
     * @return <b>r</b> 0 - 255<br>
     * <b>g</b> 0 - 255<br>
     * <b>b</b> 0 - 255
     */
    public static int[] hsb2rgb(float h, float s, float v) {
        if (Float.compare(s, 0.0f) < 0 || Float.compare(v, 0.0f) < 0
                || Float.compare(s, 100.0f) > 0 || Float.compare(v, 100.0f) > 0)
            throw new IllegalArgumentException("H:" + h + ", S:" + s + ", B:" + v);
        h = h % 360;
        h = h >= 0 ? h : h + 360;

        float r = 0, g = 0, b = 0;
        int i = (int) ((h / 60) % 6);
        float f = (h / 60) - i;
        float p = v * (1 - s);
        float q = v * (1 - f * s);
        float t = v * (1 - (1 - f) * s);
        switch (i) {
            case 0 -> {
                r = v;
                g = t;
                b = p;
            }
            case 1 -> {
                r = q;
                g = v;
                b = p;
            }
            case 2 -> {
                r = p;
                g = v;
                b = t;
            }
            case 3 -> {
                r = p;
                g = q;
                b = v;
            }
            case 4 -> {
                r = t;
                g = p;
                b = v;
            }
            case 5 -> {
                r = v;
                g = p;
                b = q;
            }
            default -> {
            }
        }
        return new int[]{(int) (r * 255.0), (int) (g * 255.0),
                (int) (b * 255.0)};
    }

    /**
     * 使图像更暗
     */
    public static void darker(BufferedImage img) {
        darker(img, 0, 0, img.getWidth(), img.getHeight());
    }

    /**
     * 使图像更暗
     */
    public static void darker(BufferedImage img, int x, int y, int width, int height) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Color c = new Color(img.getRGB(x + i, y + j)).darker();
                img.setRGB(x + i, y + j, c.getAlpha() << 24 | c.getRed() << 16 | c.getGreen() << 8 | c.getBlue());
            }
        }
    }

    /**
     * 使图像更亮
     */
    public static void brighter(BufferedImage img) {
        brighter(img, 0, 0, img.getWidth(), img.getHeight());
    }

    /**
     * 使图像更亮
     */
    public static void brighter(BufferedImage img, int x, int y, int width, int height) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Color c = new Color(img.getRGB(x + i, y + j)).brighter();
                img.setRGB(x + i, y + j, c.getAlpha() << 24 | c.getRed() << 16 | c.getGreen() << 8 | c.getBlue());
            }
        }
    }
}

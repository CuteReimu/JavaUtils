package self.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件操作工具类
 */
public class FileUtil {

    private FileUtil() {
    }

    /**
     * 获取某目录下所有指定文件名文件
     *
     * @param path     - 目录
     * @param fileName - 文件名
     * @return 目录下的文件组成的一个List
     */
    public static List<File> getFilesByName(String path, String fileName) {
        List<File> fileList = new ArrayList<>();
        File file = new File(path);
        File[] array = file.listFiles();
        if (array != null) {
            for (File value : array) {
                if (value.isFile()) {
                    if (value.getName().equals(fileName)) {
                        fileList.add(value);
                    }
                } else if (value.isDirectory()) {
                    fileList.addAll(getFilesByName(value.getPath(), fileName));
                }
            }
        }
        return fileList;
    }

    /**
     * 获取某目录下所有指定扩展名文件
     *
     * @param path - 目录
     * @param str  - 扩展名
     * @return 目录下的文件组成的一个List
     */
    public static List<File> getFiles(String path, String... str) {
        List<File> fileList = new ArrayList<>();
        File file = new File(path);
        File[] array = file.listFiles();
        String[] searchStr = null;
        if (str.length > 0) {
            searchStr = new String[str.length];
            for (int i = 0; i < str.length; i++) {
                searchStr[i] = ".+\\." + str[i];
            }
        }
        if (array != null) {
            for (File value : array) {
                if (value.isFile()) {
                    if (searchStr != null) {
                        for (String s : searchStr) {
                            if (value.getName().matches(s)) {
                                fileList.add(value);
                                break;
                            }
                        }
                    } else {
                        fileList.add(value);
                    }
                } else if (value.isDirectory()) {
                    fileList.addAll(getFiles(value.getPath(), str));
                }
            }
        }
        return fileList;
    }

    /**
     * 读文本文件
     *
     * @param fileName - 文件路径
     * @return 每一行一个String构成的List
     */
    public static List<String> readLines(String fileName, String charsetName) throws IOException {
        List<String> list = new ArrayList<>();
        String line;
        try (BufferedReader is = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), charsetName))) {
            while ((line = is.readLine()) != null) {
                list.add(line);
            }
        }
        return list;
    }

    /**
     * 写文本文件
     *
     * @param strList     - 待写入的内容，List的每一个元素为一行
     * @param fileName    - 文件名
     * @param charsetName - 写入的编码格式
     */
    public static void writeLines(List<String> strList, String fileName, String charsetName) throws IOException {
        try (BufferedWriter os = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), charsetName))) {
            for (String str : strList) {
                os.write(str);
                os.newLine();
            }
        }
    }

    /**
     * 将某文本文件的内容复制到剪贴板
     *
     * @param fileName - 文件路径
     */
    public static void readToClipboard(String fileName, String charsetName) throws IOException {
        StringBuilder tmp = new StringBuilder();
        String line;
        try (BufferedReader is = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), charsetName))) {
            while ((line = is.readLine()) != null) {
                tmp.append(line);
                tmp.append("\n");
            }
        }
        ClipboardUtil.setSysClipboardText(tmp.toString());
    }

    /**
     * 获取文件的编码格式
     *
     * @param fileName :文件名
     * @return 文件编码格式
     */
    public static String getCodeString(String fileName) throws IOException {
        String code;
        int p = getCode(fileName);
        code = switch (p) {
            case 0xefbb -> "utf-8";
            case 0x2f2a -> "utf-8 without BOM";
            case 0xfffe -> "unicode";
            case 0xfeff -> "utf-16be";
            default -> "gbk";
        };
        return code;
    }

    /**
     * 获取文件的编码格式
     *
     * @param fileName :文件名
     * @return 文件编码格式
     */
    public static int getCode(String fileName) throws IOException {
        BufferedInputStream bin = null;
        try {
            bin = new BufferedInputStream(new FileInputStream(fileName));
            return (bin.read() << 8) + bin.read();
        } finally {
            if (bin != null) {
                try {
                    bin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 复制文件
     *
     * @param oldFile - 可以是文件夹，也可以是文件
     * @param newFile - 和oldFile类型要一样
     */
    public static void copy(File oldFile, File newFile) throws IOException {
        String oldFileName = oldFile.getPath();
        String newFileName = newFile.getPath();
        if (!oldFile.exists()) {
            throw new FileNotFoundException();
        }
        if (oldFile.isDirectory() != newFile.isDirectory())
            throw new RuntimeException();
        if (oldFile.isDirectory()) {
            if (oldFileName.charAt(oldFileName.length() - 1) != '\\' && oldFileName.charAt(oldFileName.length() - 1) != '/')
                oldFileName += File.separator;
            if (newFileName.charAt(newFileName.length() - 1) != '\\' && newFileName.charAt(newFileName.length() - 1) != '/')
                newFileName += File.separator;
            if (!newFile.exists() && !newFile.mkdirs()) {
                throw new IOException();
            }
            List<File> files = getFiles(oldFileName);
            for (File file : files) {
                copy(file, new File(newFileName + file.getPath().substring(oldFileName.length())));
            }
            return;
        }
        try (FileInputStream fi = new FileInputStream(oldFile); FileOutputStream fo = new FileOutputStream(newFile); FileChannel in = fi.getChannel(); FileChannel out = fo.getChannel()) {
            in.transferTo(0, in.size(), out);
        }
    }

    /**
     * 改文件名
     *
     * @param newName - 新文件名（不是fullPath）
     */
    public static boolean rename(File file, String newName) {
        return file.renameTo(new File(file.getParent() + newName));
    }
}

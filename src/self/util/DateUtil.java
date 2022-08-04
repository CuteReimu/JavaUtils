package self.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
    private static TimeZone currentTimeZone = TimeZone.getTimeZone("GMT+8:00");

    public static TimeZone getCurrentTimeZone() {
        return currentTimeZone;
    }

    public static void setCurrentTimeZone(TimeZone currentTimeZone) {
        DateUtil.currentTimeZone = currentTimeZone;
    }

    private DateUtil() {
    }

    /**
     * 获取当前日期
     *
     * @return 日期的格式为"YYMMDD"
     */
    public static int getDate() {
        return getDate(Calendar.getInstance(currentTimeZone));
    }

    /**
     * 获取calendar代表的日期
     *
     * @param c - calendar
     * @return 日期的格式为"YYMMDD"
     */
    public static int getDate(Calendar c) {
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int date = c.get(Calendar.DATE);
        return (year * 100 + month) * 100 + date;
    }

    /**
     * 获取当前时间
     *
     * @return 时间的格式为"HHMMSS"
     */
    public static int getTime() {
        return getTime(Calendar.getInstance(currentTimeZone));
    }

    /**
     * 获取calendar代表的时间
     *
     * @param c - calendar
     * @return 时间的格式为"HHMMSS"
     */
    public static int getTime(Calendar c) {
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        int sec = c.get(Calendar.SECOND);
        return (hour * 100 + min) * 100 + sec;
    }

    /**
     * 通过互联网获取标准时间
     *
     * @return 时间的格式为"HHMMSS"
     */
    public static int getInternetTime() {
        Date d = new Date(currentTimeMillis(true) + 3600000L * 8);// 通过互联网获取标准时间
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        int sec = c.get(Calendar.SECOND);
        return (hour * 100 + min) * 100 + sec;
    }

    /**
     * 获取时间差
     *
     * @param time1 - 起始时间
     * @param time2 - 结束时间
     * @return 时间差
     */
    public static int diffTime(int time1, int time2) {
        if (time1 == time2)
            return 0;
        int hour1 = time1 / 10000;
        int hour2 = time2 / 10000;
        int min1 = time1 / 100 % 100;
        int min2 = time2 / 100 % 100;
        if (min1 > min2) {
            min2 += 60;
            hour2 -= 1;
        }
        int sec1 = time1 % 100;
        int sec2 = time2 % 100;
        if (sec1 > sec2) {
            sec2 += 60;
            min2 -= 1;
        }
        return ((hour2 - hour1) * 100 + (min2 - min1)) * 100 + (sec2 - sec1);
    }

    /**
     * 将int类型时间转化为String类型
     *
     * @param time - int类型时间
     * @return String类型时间，格式HH:MM:SS
     */
    public static String timeToStr(int time) {
        int hour = time / 10000;
        int min = time / 100 % 100;
        int sec = time % 100;
        return hour + ":" + (min < 10 ? "0" + min : min) + ":" + (sec < 10 ? "0" + sec : sec);
    }

    private static long currentTimeMillis(boolean sync) {
        if (!sync)
            return System.currentTimeMillis();
        try {
            long time1 = syncCurrentTime();
            long time2 = System.currentTimeMillis();
            if (Math.abs(time1 - time2) >= 60000)
                return time2;
            return time1;
        } catch (IOException e) {
            return System.currentTimeMillis();
        }
    }

    private static long syncCurrentTime() throws IOException {
        final int DEFAULT_PORT = 37;// NTP服务器端口
        final String DEFAULT_HOST = "time-nw.nist.gov";// NTP服务器地址
        final long differenceBetweenEpochs = 2208988800L;

        InputStream raw;
        try (Socket theSocket = new Socket(DEFAULT_HOST, DEFAULT_PORT)) {
            raw = theSocket.getInputStream();

            long secondsSince1900 = 0;
            for (int i = 0; i < 4; i++) {
                secondsSince1900 = (secondsSince1900 << 8) | raw.read();
            }
            raw.close();
            long secondsSince1970 = secondsSince1900 - differenceBetweenEpochs;
            return secondsSince1970 * 1000;
        }
    }

}

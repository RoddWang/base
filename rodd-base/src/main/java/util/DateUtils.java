package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exception.ApiRuntimeException;

/**
 * 此类描述的是：
 * 
 * @author: wangjian
 * @date: 2017年11月13日 上午11:15:08
 */
public final class DateUtils {

    /** yyyy-MM-dd */
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

    /** yyyy-MM-dd HH:mm:ss,SSS */
    public static final String DATETIME_YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss,SSS";

    /** yyyy-MM-dd HH:mm:ss */
    public static final String DATETIME_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /** 19位时间格式 yyyyMMddHHmmss. */
    public static final String DATETIME_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    /** yyyyMMdd */
    public static final String DATE_YYYY_MM_DD= "yyyyMMdd";

    /** yyMMdd */
    public static final String DATE_YY_MM_DD = "yyMMdd";

    /** yyyyMMddHHmm */
    public static final String DATETIME_YYYY_MM_DD_HH_MM_1 = "yyyyMMddHHmm";

    /** yyyyMMddHHmmss */
    public static final String DATETIME_YYYY_MM_DD_HH_MM_SS_1 = "yyyyMMddHHmmss";

    /** 17位精确到毫秒的时间格式：yyyyMMddHHmmssSSS */
    public static final String DATETIME_YYYY_MM_DD_HH_MM_SS_SSS_1 = "yyyyMMddHHmmssSSS";

    /** The Constant DATE_YYYYMM. */
    public static final String DATE_YYYYMM = "yyyyMM";

    /** The Constant MILLISSECONDS_PER_DAY. */
    public static final int MILLISSECONDS_PER_DAY = 86400000;

    /** The Constant THOUSAND. */
    public static final int THOUSAND = 1000;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);

    /**
     * 私有化工具类，禁止实例化。 DateUtil.
     * 
     */
    private DateUtils() {
        throw new ApiRuntimeException("禁止实例化工具类 DateUtils");
    }

    /**
     * 转换字符串为Date.
     *
     * @param dateStr 要转换的时间字符串
     * @param format 时间格式
     * @return Date 时间类型结果
     */
    public static Date parseString(String dateStr, String format) {
        if (StringUtils.isEmpty(dateStr)) {
            LOGGER.warn("invalid data,dateStr={}", dateStr);
            return null;
        }

        if (StringUtils.isEmpty(format)) {
            LOGGER.warn("invalid format,format={}", format);
            return null;
        }
        DateFormat df = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = df.parse(dateStr);
            if (!dateStr.equals(df.format(date))) {
                date = null;
            }
        } catch (ParseException e) {
            LOGGER.error("fail to parse date", e);
        }
        return date;
    }

    /**
     * 将日期转换未指定类型的字符串.
     *
     * @param date 要转换的date
     * @param format 格式
     * @return 转换完成的字符串
     */
    public static String formatDate(Date date, String format) {
        if (null == date) {
            return "";
        }
        DateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    /**
     * 返回当前时间戳.
     *
     * @author huanghui
     * @param format 默认为：yyyyMMddHHmmss
     * @return string 时间字符串
     * @see [类、类#方法、类#成员]
     */
    public static String getCurrentTimestamp(final String format) {
        String defaultFormat = format;
        if (StringUtils.isBlank(defaultFormat)) {
            defaultFormat = DATETIME_YYYY_MM_DD_HH_MM_SS_1;
        }

        return formatDate(new Date(), defaultFormat);
    }

    /**
     * 获取昨天的日期.
     *
     * @return the yesterday
     */
    public static Date getYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 获取本月已过日期数.
     *
     * @param date the date
     * @return the past days of month
     */
    public static int getPastDaysOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        return calendar.get(Calendar.DATE);
    }

    /**
     * 获取本月剩余日期数.
     *
     * @param date the date
     * @return the residual days of month
     */
    public static int getResidualDaysOfMonth(Date date) {
        return getDaysOfMonth(date) - getPastDaysOfMonth(date);
    }

    /**
     * 获取本月总天数.
     *
     * @param date the date
     * @return the days of month
     */
    public static int getDaysOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        return calendar.getActualMaximum(Calendar.DATE);
    }

    /**
     * 获取上月今天的日期.
     *
     * @return the today of last month
     */
    public static Date getTodayOfLastMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        return calendar.getTime();
    }

    /**
     * 获取上周一的日期.
     *
     * @return the mon day of last week
     */
    public static Date getMonDayOfLastWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar.getTime();
    }

    /**
     * 获取上周为年内第几周.
     *
     * @return the last week of year
     */
    public static int getLastWeekOfYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取到昨天时间 时间格式“YYYY-MM-DD 00:00:00”.
     *
     * @return the yestday time string
     * @see [类、类#方法、类#成员]
     */
    public static String getYestdayTimeString() {
        return formatDate(getYesterday(), DATE_FORMAT_YYYY_MM_DD) + " 00:00:00";
    }

    /**
     * 获取到今日23:59:59时间 时间格式“YYYY-MM-DD 23:59:59”.
     *
     * @return the today time string
     * @see [类、类#方法、类#成员]
     */
    public static String getTodayTimeString() {
        return formatDate(new Date(), DATE_FORMAT_YYYY_MM_DD) + " 23:59:59";
    }

    /**
     * 获得当前时间的年月日字符串
     * 
     * @return
     */
    public static String getCurDateForString() {
        return formatDate(new Date(), DATE_YYYY_MM_DD);
    }

    /**
     * 获取UTC时间.
     *
     * @author liutao
     * @return UTC时间串"yyyy-MM-dd hh:mm:ss"
     */
    public static String getUTCTime() {
        // 1、获取本地时间
        Calendar calendar = Calendar.getInstance();
        // 2、获取时间偏移量
        int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
        // 3、获取夏令时差
        int dstOffset = calendar.get(Calendar.DST_OFFSET);
        // 4、从本地时间扣除差异既可以得到UTC时间
        calendar.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        // 5、格式化时间格式
        SimpleDateFormat foo = new SimpleDateFormat(DATETIME_YYYY_MM_DD_HH_MM_SS);

        return foo.format(new Date(calendar.getTimeInMillis()));

    }

    /**
     * Gets the interval time.
     *
     * @param Day of the interval
     * @return the interval time
     */
    public static String getIntervalTime(int interval) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, interval);
        return formatDate(calendar.getTime(), "yyyyMMdd");
    }
}

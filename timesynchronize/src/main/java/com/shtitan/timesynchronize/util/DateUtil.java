package com.shtitan.timesynchronize.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;

import org.apache.commons.lang.time.FastDateFormat;

/**
 *  处理日期相关的操作
 * @author ppl
 *
 */
public class DateUtil {
    
	public final static SimpleDateFormat LONG_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public final static SimpleDateFormat MID_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public final static SimpleDateFormat SHORT_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public final static SimpleDateFormat COMMON_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
	public final static SimpleDateFormat HHMM_FORMAT = new SimpleDateFormat("HH:mm");
	public final static FastDateFormat HHMMSS_FORMAT = FastDateFormat.getInstance("HH:mm:ss");
	//0时区时间的Format
	public final static SimpleTimeZone timeZone0 = new SimpleTimeZone(0,"GMT");
	public final static SimpleDateFormat LONG_FORMAT_GMT0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 3天的毫秒数
	 */
	public final static long THREE_DAY_MILLSEC = 3*24*3600*1000;

	/**
	 * 1天的毫秒数
	 */
	public final static long ONE_DAY_MILLSEC = 24*3600*1000;
	
	/**
	 * 1小时的毫秒数
	 */
	public final static long ONE_HOUR_MILLSEC = 3600*1000;
	
	/**
	 * 3小时的毫秒数
	 */
	public final static long THREE_HOURS_MILLSEC = 3*3600*1000;
	
	/**
	 * 12小时的毫秒数
	 */
	public final static long TWELVE_HOURS_MILLSEC = 12*3600*1000;
	
	/**
	 * 一天的分钟数
	 */
	public final static int ONE_DAY_MINUTE=24*60;
	/**
	 * 一周的小时数
	 */
	public final static int ONE_WEEK_HOUR=7*24;
	
	public static Date getDateByString(String longDateFormatString) throws Exception{
	    if(longDateFormatString==null || longDateFormatString.length()==0)
	        return new Date(0);
	    else
	        return LONG_FORMAT.parse(longDateFormatString);
	}
	
	/**
	 * 返回当前日期完整字符串，格式为: yyyy-MM-dd hh:mm:ss
	 * @return
	 */
	public static String getLongCurrentDate() {
		return String.valueOf(LONG_FORMAT.format(new Date()));
	}
	/**
     * 返回当前日期完整字符串，格式为: yyyyMMddhhmmss
     * @return
     */
	public static String getCommonFormatDate(){
	    return String.valueOf(COMMON_FORMAT.format(new Date()));
	}
	/**
	 * 给定日期(Date)，返回格式为: yyyy-MM-dd hh:mm:ss的字符串
	 * @param date
	 * @return
	 */
	public static String getLongDate(Date date) {
		if (null == date)
			return getLongCurrentDate();
		return String.valueOf(LONG_FORMAT.format(date));
	}
	
	/**
	 * 给定日期(long:ms)，返回格式为: yyyy-MM-dd hh:mm:ss的字符串
	 * @param value
	 * @return
	 */
	public static String getLongDate(long value) {
	    return String.valueOf(LONG_FORMAT.format(new Date(value)));
	}
	
	/**
     * 给定日期(long:ms)，返回0时区格式为: yyyy-MM-dd hh:mm:ss的字符串
     * @param value
     * @return
     */
   public static String getLongDateGMT0(long value) {
        LONG_FORMAT_GMT0.setTimeZone(timeZone0);
        return LONG_FORMAT_GMT0.format(new Date(value));
    }  
     
	
	/**
	 * 返回当前日期简写字符串，格式为: yyyy-MM-dd
	 * @return
	 */
	public static String getShortCurrentDate() {
		return String.valueOf(SHORT_FORMAT.format(new Date()));
	}
	
	/**
	 * 给定日期(Date)，返回当前日期简写字符串，格式为: yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String getShortDate(Date date) {
		if (null == date)
			return getShortCurrentDate();
		return String.valueOf(SHORT_FORMAT.format(date));
		
	}
	
	/**
	 * 给定日期(long:ms)，返回当前日期简写字符串，格式为: yyyy-MM-dd
	 * @param value
	 * @return
	 */
	public static String getShortDate(long value) {
		return String.valueOf(SHORT_FORMAT.format(new Date(value)));		
	}
	
	/**
	 * 返回当前日期中等复杂程度的字符串，格式为: yyyy-MM-dd hh:mm
	 * @return
	 */
	public static String getMidCurrentDate() {
		return String.valueOf(MID_FORMAT.format(new Date()));
	}
	
	public static String getHHMM_Date(Date date){
		if (null == date)
			return getMidCurrentDate();
		return String.valueOf(HHMM_FORMAT.format(date));
	}
	
	/* * 给定日期(Date)，返回当前日期中等复杂程度的字符串，格式为: yyyy-MM-dd hh:mm
	 * @param date
	 * @return
	 */
	public static String getMidDate(Date date) {
		if (null == date)
			return getMidCurrentDate();
		return String.valueOf(MID_FORMAT.format(date));
	}
	
	/**
	 * 给定日期(long:ms)，返回当前日期中等复杂程度的字符串，格式为: yyyy-MM-dd hh:mm
	 * @param value
	 * @return
	 */
	public static String getMidDate(long value) {
		return String.valueOf(MID_FORMAT.format(new Date(value)));		
	}
	
	/**
	 * 给定日期(long:ms)，返回当前日期中等复杂程度的字符串，格式为: hh:mm
	 * @param value
	 * @return
	 */
	public static String getHHMMDate(long value) {
		return String.valueOf(MID_FORMAT.format(new Date(value)));
	}
	
	/**
	 * // 将毫秒数换算成x天x时x分x秒x毫秒
	 * @param ms
	 * @return
	 */
	public static String getWellTimeFromMilliSecond(long ms) {
		int oneSecond = 1000;
		int oneMinute = oneSecond * 60;
		int oneHour = oneMinute * 60;
		int oneDay = oneHour * 24;

		long day = ms / oneDay;
		long hour = (ms - day * oneDay) / oneHour;
		long minute = (ms - day * oneDay - hour * oneHour) / oneMinute;
		long second = (ms - day * oneDay - hour * oneHour - minute * oneMinute) / oneSecond;
		long milliSecond = ms - day * oneDay - hour * oneHour - minute * oneMinute - second	* oneSecond;

		String strDay = day < 10 ? "" + day : "" + day;
		String strHour = hour < 10 ? "" + hour : "" + hour;
		String strMinute = minute < 10 ? "" + minute : "" + minute;
		String strSecond = second < 10 ? "" + second : "" + second;
		String strMilliSecond = milliSecond < 10 ? "" + milliSecond : "" + milliSecond;
		strMilliSecond = milliSecond < 100 ? "" + strMilliSecond : "" + strMilliSecond;
		StringBuffer timeBuffer = new StringBuffer();
		timeBuffer.append(strDay);
		timeBuffer.append("d");
		timeBuffer.append(strHour);
		timeBuffer.append("h");
		timeBuffer.append(strMinute);
		timeBuffer.append("m");
		timeBuffer.append(strSecond);
		timeBuffer.append("s");
		timeBuffer.append(strMilliSecond);
		timeBuffer.append("ms");
		
		return timeBuffer.toString();
	}

	/**
	 * 得到日期所在的周区间，例如将2008-04-09归入2008-04-07_2008-04-13这个周区间
	 * @param dateString 时间字符串
	 * @return
	 */
	public static String getWeekFromDate(String dateString){
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = format.parse(dateString);
			Date mondy = new Date();
			Date sundy = new Date();
			int day = date.getDay();
			if (day == 0) {
				mondy.setTime(date.getTime() - 1000L * 60L * 60L * 24L * 6L);
				sundy.setTime(date.getTime());
			} else {
				mondy.setTime(date.getTime() - 1000L * 60L * 60L * 24L * (day-1));
				sundy.setTime(date.getTime() + 1000L * 60L * 60L * 24L * (7 - day));
			}
			sb.append(format.format(mondy));
			sb.append("_");
			sb.append(format.format(sundy));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
     * 将秒数转换为时间间隔
     * @param seconds
     * @return
     */
    public static int[] getTimeRangeBySeconds(long seconds) {
        int[] times = new int[4];
        times[0] = (int) (seconds / 86400L);
        seconds %= 86400L;
        times[1] = (int) (seconds / 3600L);
        seconds %= 3600L;
        times[2] = (int) (seconds / 60L);
        times[3] = (int) (seconds % 60L);
        return times;
    }
    
    /**
	 * 判断两个时间是否是同一周
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameWeekDates(Date date1, Date date2) {

		Calendar cal1 = Calendar.getInstance();

		Calendar cal2 = Calendar.getInstance();

		cal1.setTime(date1);

		cal2.setTime(date2);

		int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);

		if (0 == subYear) {

			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))

				return true;

		} else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {

			// 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周

			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))

				return true;

		} else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {

			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))

				return true;

		}

		return false;

	}

	/**
	 * 判断是否属于同一个月
	 */

	public static boolean isSameMonth(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("The date must not be null");
		} else {
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(date1);
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(date2);
			return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(2) == cal2.get(2);
		}
	}

	/**
	 * 判断是否同一年
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameYear(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("The date must not be null");
		} else {
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(date1);
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(date2);
			return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1);
		}
	}
	
	 /**
     * 根据传入的参数获取Quartz的cron表达式
     * @param date 时间对象
     * @param type 1:时间;2:日期+时间
     * @return 
     */
    public static String getDateTimeCronTriggerExpr(Date date, int type){
        StringBuilder expr = new StringBuilder();
        if(type == 0){
            String[] timeArr = HHMMSS_FORMAT.format(date).split(":");
            if(timeArr.length == 3){
                expr.append(timeArr[2]).append(" ").append(timeArr[1]).append(" ").append(timeArr[0]);
            }else{
                expr.append("0").append(" ").append("0").append(" ").append("0");
            }
        }
        return expr.toString();
    }


}

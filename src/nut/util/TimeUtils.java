package nut.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Utility functions for time operations
 * @author Vladimir
 *
 */
public class TimeUtils {	
	private static final int millisecondsInHour = 60 * 60 * 1000;
	
	public static long getMilliseconds(LocalDateTime date){
		return date.toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli();
	}
	
	public static double getHoursBetween(LocalDateTime date1, LocalDateTime date2){		 
		return ((double)(getMilliseconds(date2) - getMilliseconds(date1))) / millisecondsInHour;
	}
	
	public static boolean isBeforeOrEqual(LocalDateTime date1, LocalDateTime date2){
		return date1.isBefore(date2) || date1.isEqual(date2);
	}
	
	public static boolean isAfterOrEqual(LocalDateTime date1, LocalDateTime date2){
		return date1.isAfter(date2) || date1.isEqual(date2);
	}
	
	/**
	 * Makes a local date from given parameters
	 * Local date is used as all times are supposed to be in the same Time Zone
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @return Date
	 */
	public static LocalDateTime makeDate(int year, int month, int day, int hour, int minute){
		return LocalDateTime.of(year, month, day, hour, minute, 0);
	}
	
	public static String formatDate(LocalDateTime date, String format){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return date.format(formatter);
	}
}

package nut.util;

public class HourAndMinute {
	private static final int minutesInHour = 60;
	
	private HourAndMinute(){
		
	}
	
	public static HourAndMinute getHourAndMinute(Double time){
		String[] strTimeInfo = time.toString().split("\\.");
		HourAndMinute result = new HourAndMinute();
		result.Hours = Integer.parseInt(strTimeInfo[0]);
		result.Minutes = (long)(minutesInHour * Double.parseDouble("0." + strTimeInfo[1]));
		return result;
	}
	
	public long Hours;
	public long Minutes;
}

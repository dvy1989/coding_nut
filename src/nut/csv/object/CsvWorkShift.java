package nut.csv.object;

import java.util.regex.Pattern;

import nut.csv.CsvFieldParser;

/**
 * Information about work shift in CSV file
 * @author Владимир
 *
 */
public class CsvWorkShift {
	private static Pattern dayPattern = Pattern.compile("[0-9]{1,2}\\.[0-9]{1,2}\\.[0-9]{4}");
	private static Pattern timePattern = Pattern.compile("[0-9]{1,2}:[0-9]{1,2}");
	
	/**
	 * Data are stored in the following way
	 * 0 - day
	 * 1 - month
	 * 2 - year
	 */
	public int[] Date;
	
	/**
	 * Finish of work shift
	 * 0 - hour
	 * 1 - minute
	 */
	public int[] Finish;
	
	public String FirstName;
	
	public int Id;
	
	public String SecondName;
		
	/**
	 * Start of work shift
	 * 0 - hour
	 * 1 - minute
	 */
	public int[] Start;	 
	
	/**
	 * Parsing of time labels, assigned to a shift
	 * @param str
	 * @return Array, representing a time label
	 */
	private int[] getTimeData(String str){		 
		if (!timePattern.matcher(str).matches()){
			throw new IllegalArgumentException("Not a time");
		}
		String[] tmp = str.split(":");
		int[] numTmp = new int[2];
		numTmp[0] = Integer.parseInt(tmp[0]);
		for (int i = 0; i < 2; i++){
			numTmp[i] = Integer.parseInt(tmp[i]);
		}
		return numTmp;
	}
	
	@CsvFieldParser(orderNumber=2)
	public void setDate(String str){		 
		if (!dayPattern.matcher(str).matches()){
			throw new IllegalArgumentException("Not a date");
		}
		String[] tmp = str.split("\\.");
		Date = new int[3];
		for (int i = 0; i < 3; i++){
			Date[i] = Integer.parseInt(tmp[i]);
		}
	}
	
	@CsvFieldParser(orderNumber=4)
	public void setFinish(String str){
		Finish = getTimeData(str);
	}
	
	@CsvFieldParser(orderNumber=1)
	public void setId(String str){
		Id = Integer.parseInt(str);
	}
	
	@CsvFieldParser(orderNumber=0)
	public void setName(String str){		 
		String[] strArr = str.split(" ");
		FirstName = strArr[0];
		SecondName = strArr[1];
	}
	
	@CsvFieldParser(orderNumber=3)
	public void setStart(String str){
		Start = getTimeData(str);
	}
}

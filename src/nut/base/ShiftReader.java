package nut.base;

import java.io.InputStream;

import nut.csv.CsvReader;
import nut.csv.object.CsvWorkShift;
import nut.data.EmployeeList;
import nut.data.shift.WorkTimeMonitor;
import nut.util.Log;

/**
 * Reads work shifts and puts information
 * from CSV to corresponding storages
 * @author Владимир
 *
 */
public class ShiftReader {
	/**
	 * Shows, that data is loaded
	 */
	private static boolean isLoaded = false;
	/**
	 * CSV separator
	 */
	private static final String separator = ",";
	
	public static void allowReload(){
		isLoaded = false;
	}
	
	/**
	 * Reads shifts from given input
	 * @param input
	 */
	public static void readShifts(InputStream input){		 
		if (isLoaded){
			return;
		}
		try(CsvReader<CsvWorkShift> reader = new CsvReader<CsvWorkShift>(input, separator, CsvWorkShift.class, true)) {
			CsvWorkShift shift;
			while ((shift = reader.readLine()) != null) {
				try{					 
					EmployeeList.getInstance().add(shift.Id, shift.FirstName, shift.SecondName);
					WorkTimeMonitor.getInstance().addShift(shift.Id, shift.Date, shift.Start, shift.Finish);
				}
				catch (Exception exp){
					Log.error("An error occured during reading a work shift", exp);
				}				
			}
			isLoaded = true;
		} catch (Exception exp) {
			Log.error("Error while retrieving work shifts", exp);
		}
	}
}

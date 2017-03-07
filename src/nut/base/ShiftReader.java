package nut.base;

import java.io.IOException;
import java.util.Collection;

import nut.csv.CsvReader;
import nut.csv.object.CsvWorkShift;
import nut.data.EmployeeList;
import nut.data.shift.WorkTimeMonitor;
import nut.util.Log;

public class ShiftReader {
	private static final String separator = ",";
	private static boolean isLoaded = false;
	
	public static void readShift(String fileName){
		if (isLoaded){
			return;
		}
		try{
			Collection<CsvWorkShift> shifts = CsvReader.<CsvWorkShift>readCsvFile(fileName, separator, CsvWorkShift.class);
			for (CsvWorkShift shift : shifts){
				try{
					EmployeeList.getInstance().add(shift.Id, shift.FirstName, shift.SecondName);
					WorkTimeMonitor.getInstance().addShift(shift.Id, shift.Date, shift.Start, shift.Finish);
				}
				catch (Exception exp){
					Log.error("An error occured during reading a work shift", exp);
				}				
			}
			isLoaded = true;
		}
		catch (IOException exp){
			Log.error("Error while reading the file", exp);
		}
	}
	
	public static void allowReload(){
		isLoaded = false;
	}
}

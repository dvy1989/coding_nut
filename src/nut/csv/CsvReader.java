package nut.csv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.*;

import nut.util.Log;

/**
 * Class, used for reading CSV files
 * @author Vladimir
 *
 */
public class CsvReader {
	/**
	 * Reads CSV file
	 * @param fileName Path to file
	 * @param separator Separator at CSV file
	 * @param objectType Type of objects, that are stored in CSV
	 * @return List of objects from CSV file
	 * @throws IOException If an error occurs during file reading, calling function should handle it
	 */
	public static <T extends ICsvObject> Collection<T> readCsvFile(String fileName, String separator, Class<T> objectType) throws IOException{
		ArrayList<T> objects = new ArrayList<T>();
		try (FileReader fr = new FileReader(fileName)){
			try (BufferedReader br = new BufferedReader(fr)){
				String csvLine;
				//Skip header
				br.readLine();
				while ((csvLine = br.readLine())!=null){
					try {
						objects.add(readObject(csvLine, separator, objectType));						 
					} catch (Exception exp) {
						// In case of error in retrieving object, the method should put into log
						Log.error("Unable to read object from CSV file", exp);
					}
				}
			}
		}
		return objects;
	}
	
	/**
	 * Read an object from CSV file
	 * All exception should be handled by a calling method
	 * @param line Line at file
	 * @param separator Separator
	 * @param objectType Type of object to read
	 * @return Object
	 * @throws InstantiationException Could occur during object creation
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException Could occur when a field parsing method is called
	 */
	private static <T> T readObject(String line, String separator, Class<T> objectType) throws InstantiationException, 
																							   IllegalAccessException, 
																							   IllegalArgumentException, 
																							   InvocationTargetException{
		 
		String[] arr = line.split(separator);
		T obj = objectType.newInstance();
		for (Method objectMethod : objectType.getMethods()){
			CsvFieldParser methodInfo = objectMethod.getAnnotation(CsvFieldParser.class);
			if (methodInfo != null){
				if (arr.length > methodInfo.orderNumber()){					 
					objectMethod.invoke(obj, arr[methodInfo.orderNumber()]);
				}
			}
		}
		return obj;
	}
}

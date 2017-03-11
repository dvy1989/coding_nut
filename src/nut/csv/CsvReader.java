package nut.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import nut.util.Log;

/**
 * Class, used for reading CSV files
 * @author Vladimir
 * For this class try(...) usage is defined
 */
public class CsvReader<T> implements AutoCloseable {
	/**
	 * Shows, that header of CSV was skipped
	 */
	private boolean headerSkipped;
	private InputStreamReader inputReader;
	private BufferedReader lineReader;
	/**
	 * CSV separator
	 */
	private String separator;
	/**
	 * Shows, that CSV header should be skipped
	 */
	private boolean skipHeader;
	/**
	 * Type of objects in CSV
	 */
	private Class<T> type;
	
	/**
	 * Constructor
	 * @param stream Input stream
	 * @param separator CSV separator
	 * @param type Type of objects, stored in csv
	 * @param skipHeader True, if header should be skipped (for this case should be true)
	 */
	public CsvReader(InputStream stream, String separator, Class<T> type, boolean skipHeader){		
		this.inputReader = new InputStreamReader(stream);
		this.lineReader = new BufferedReader(this.inputReader);
		this.skipHeader = skipHeader;
		this.headerSkipped = false;
		this.type = type;
		this.separator = separator;
	}
	
	/**
	 * Parses line of CSV file and returns an object
	 * All exceptions should be handled by calling method
	 * @param line
	 * @return Object
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	private T parseLine(String line) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{		 
		String[] arr = line.split(separator);
		T obj = type.newInstance();
		for (Method objectMethod : type.getMethods()){
			CsvFieldParser methodInfo = objectMethod.getAnnotation(CsvFieldParser.class);
			if (methodInfo != null){
				if (arr.length > methodInfo.orderNumber()){					 
					objectMethod.invoke(obj, arr[methodInfo.orderNumber()]);
				}
			}
		}
		return obj;
	}
	
	@Override
	public void close() throws Exception {
		 try{
			 lineReader.close();
		 }
		 catch (Exception exp){
			 Log.error("Error while closing a BufferedReader", exp);
		 }
		 
		 try{
			 inputReader.close();
		 }
		 catch (Exception exp){
			 Log.error("Error while closing a InputStreamReader", exp);
		 }		  
	}
	
	/**
	 * Reads a line of CSV file and returns an object
	 * All exceptions should be handled by calling method
	 * @return
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public T readLine() throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{		 
		// Skip header
		if (this.skipHeader && !this.headerSkipped){
			this.headerSkipped = true;
			String line = this.lineReader.readLine();
			if (line == null){
				return null;
			}
			return readLine();
		}
		
		String line = this.lineReader.readLine();
		if (line == null){
			return null;
		}
		return parseLine(line);
	}
}
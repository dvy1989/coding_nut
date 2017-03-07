package nut.csv;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates a method, that is used to parse a CSV field
 * @author Vladimir
 * NOTE! Method should be public and have only one string parameter in signature
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvFieldParser {
	/**
	 * Gets an order number of the field in CSV file
	 * @return Order number of the field in CSV
	 */
	int orderNumber();	 
}

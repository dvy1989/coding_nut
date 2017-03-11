/**
 * 
 */
package nut.api;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Владимир
 *
 */
public class FileLookup {
	public static final String csvFileName = "/data/HourList201403.csv";
	
	public static InputStream lookupFile(HttpServletRequest request, String path){
		return request.getServletContext().getResourceAsStream(path);		 
	}
}

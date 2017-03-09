/**
 * 
 */
package nut.api;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ��������
 *
 */
public class WebHelper {
	private WebHelper(){
		
	}
	
	private static WebHelper instance = new WebHelper();
	
	public String getCsvPath(HttpServletRequest httpRequest){
		//httpRequest.getServletContext().getRe
		String csvFileName = "HourList201403.csv";
		Path path = Paths.get(new File(".").getAbsolutePath());
		return Paths.get(path.getParent().getParent().toString(), "eclipseApps", "Nut", csvFileName).toString();
	}
	
	public static WebHelper getInstance(){
		return instance;
	}
}

package nut.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {
	private static Logger logger = Logger.getLogger("NutLogger");	 
	
	public static void error(String text, Exception exp){
		logger.log(Level.SEVERE, text, exp);
	}
}

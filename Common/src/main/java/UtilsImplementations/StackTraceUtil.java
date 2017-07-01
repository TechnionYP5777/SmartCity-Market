package UtilsImplementations;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * This class prints the exception to string
 * 
 * @since 2017-01-06
 * @author Noam Yefet
 *
 */
public class StackTraceUtil {

	public static String stackTraceToStr(Exception x) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		x.printStackTrace(pw);
		return sw + "";
	}
	
}

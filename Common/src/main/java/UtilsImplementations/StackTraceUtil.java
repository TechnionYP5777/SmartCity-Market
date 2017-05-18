package UtilsImplementations;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StackTraceUtil {

	static public String getStackTrace(Exception e) {
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
		
	}
	
}

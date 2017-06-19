package UtilsImplementations;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StackTraceUtil {

	public static String stackTraceToStr(Exception x) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		x.printStackTrace(pw);
		return sw + "";
	}
	
}

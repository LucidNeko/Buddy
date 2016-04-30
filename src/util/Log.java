package util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
	
	private static final boolean LOG_TO_FILE = false;
	
	private static PrintStream out;
	static {
		if(LOG_TO_FILE) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			Date date = new Date();
			String fname = dateFormat.format(date) + ".log";
			
			try {
				out = new PrintStream(new FileOutputStream(fname));
			} catch (FileNotFoundException e) {
				System.err.println("Failed to create logfile " + fname + " with error " + e.getMessage() + ". Defaulting to System.out");
				out = System.out;
			}
		} else {
			out = System.out;
		}
	}
	
	public static void info(Object... objects) {
		info(objectString(", ", objects));
	}
	
	public static void info(String s, Object... objects) {
		out.println("[INFO ] => " + format(s, objects));
	}

	public static void error(Object... objects) {
		error(objectString(", ", objects));
	}
	
	public static void error(String s, Object... objects) {
		out.println("[ERROR] => " + format(s, objects));
	}

	public static void warn(Object... objects) {
		warn(objectString(", ", objects));
	}
	
	public static void warn(String s, Object... objects) {
		out.println("[WARN ] => " + format(s, objects));
	}
	
	public static String format(String s, Object... objects) {
		for(int i = 0; s.contains("{}") && i < objects.length; i++) {
			if(objects[i] instanceof String) {
				//If String contains escape chars, double them up so they get escaped
				objects[i] = ((String)(objects[i])).replaceAll("\\\\", "\\\\\\\\");
			}
			s = s.replaceFirst("\\{\\}", objects[i].toString());
		}
		return s;
	}
	
	private static String objectString(String joiner, Object... objects) {
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < objects.length; i++) {
			s.append(objects[i].toString()).append(joiner);
		}
		return s.toString();
	}
	
	public static void main(String[] args) {
		Log.error("{} - {} = {}", 3, 4, 3-4);
		Log.info("There once was a {} who lived in a {}", "cat", "shoe");
		Log.warn("Oh SHIT {} did {}", "bob", 12);
		Log.info();
		Log.info(1, 2, 3, 45, 6, "cat", 3.4569f);
		Log.warn(1, 2, 3, 45, 6, "cat", 3.4569f);
		Log.info(1, 2, 3, 45, 6, "cat", 3.4569f);
	}

}

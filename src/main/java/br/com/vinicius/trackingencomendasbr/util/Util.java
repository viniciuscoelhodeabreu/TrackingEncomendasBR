package br.com.vinicius.trackingencomendasbr.util;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class Util {

	public static String getSystemTemporaryPath() {
		try {
			return File.createTempFile(Byte.toString(Byte.MIN_VALUE), Byte.toString(Byte.MIN_VALUE)).getParentFile().getPath().concat("/");
		} catch (IOException e) {
			return new String();
		}
	}
	
	public static Calendar getCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		return calendar;
	}
	
}

package br.com.vinicius.trackingencomendasbr.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import lombok.SneakyThrows;

public class Util {


	public static Object getFieldValue(String fieldName, Object fromObject) {
		try {
			Field field = fromObject.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(fromObject);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
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
	
	@SneakyThrows
	public static void sleepMinutes(Integer minutes) {
		Thread.sleep(TimeUnit.MINUTES.toMillis(minutes));
	}
	
	@SneakyThrows
	public static void sleepSeconds(Integer seconds) {
		Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
	}
	
}

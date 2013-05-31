package com.gpxrecorder.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateHelper {

	public static String getFormatedStringFromMilliSeconds(long milliSeconds, String dateFormat) {
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat,
				Locale.ROOT);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}
}

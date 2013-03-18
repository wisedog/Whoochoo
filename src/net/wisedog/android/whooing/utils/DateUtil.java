package net.wisedog.android.whooing.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import net.wisedog.android.whooing.Define;

public class DateUtil {

	static public String getDateWithTimestamp(long milisec){
		Date date = new Date(milisec);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);Locale locale = new Locale(Define.LOCALE_LANGUAGE, Define.COUNTRY_CODE);
        java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT, locale);
        String dateStr = df.format(calendar.getTime()).toString();
        return dateStr;
	}
}

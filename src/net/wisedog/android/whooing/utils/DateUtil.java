package net.wisedog.android.whooing.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	static public String getDateWithTimestamp(long milisec){
		//TODO locale Date formatting 
		Date date = new Date(milisec);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(date);
	}
}

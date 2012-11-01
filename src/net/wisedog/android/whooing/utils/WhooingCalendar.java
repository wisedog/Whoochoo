package net.wisedog.android.whooing.utils;

import java.util.Calendar;

public class WhooingCalendar {

	static public String getTodayYYYYMMDD(){
		Calendar rightNow = Calendar.getInstance(); 
		
		int year = rightNow.get(Calendar.YEAR);
		int month = rightNow.get(Calendar.MONTH);
		int day = rightNow.get(Calendar.DAY_OF_MONTH);
		month = month + 1;    //달은 0~11이다.
		String monthString = "";
		String dayString = "";
		if(day < 10)
			dayString += "0";
		if(month < 10)
			monthString += "0";
		monthString +=month;
		dayString += day;
		
		return ""+year+monthString+dayString;
	}

    static public int getTodayYYYYMMDDint(){
        String str = getTodayYYYYMMDD();
        int date = Integer.getInteger(str, 0);
        return date;
    }
}

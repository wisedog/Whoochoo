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
        
        int date = 0;
        try{
            date = Integer.parseInt(str);
        }catch(NumberFormatException e){
            return 0;
        }
        return date;
    }
    
    static public String getTodayYYYYMM(){
        Calendar rightNow = Calendar.getInstance(); 
        
        int year = rightNow.get(Calendar.YEAR);
        int month = rightNow.get(Calendar.MONTH);
        month = month + 1;
        String monthString = "";
        if(month < 10){
            monthString += "0";
        }
        monthString += month;
        return "" + year + monthString;
    }
    
    static public String getPreMonthYYYYMM(int premonth){
        Calendar rightNow = Calendar.getInstance(); 
        rightNow.add(Calendar.MONTH, -(premonth));
        int year = rightNow.get(Calendar.YEAR);
        int month = rightNow.get(Calendar.MONTH);
        month = month + 1;
        String monthString = "";
        if(month < 10)
            monthString += "0";
        monthString += month;
        return "" + year + monthString;
    }
    
    static public String getPreMonthYYYYMMDD(int premonth){
        Calendar rightNow = Calendar.getInstance(); 
        rightNow.add(Calendar.MONTH, -(premonth));
        int year = rightNow.get(Calendar.YEAR);
        int month = rightNow.get(Calendar.MONTH);
        int days = rightNow.get(Calendar.DATE);
        month = month + 1;
        String monthString = "";
        if(month < 10)
            monthString += "0";
        monthString += month;
        
        String dayString = "";
        if(days < 10){
            dayString += "0";
        }
        dayString += days;
        
        return "" + year + monthString + dayString;
    }
    
    static public String getNextMonthYYYYMM(int nextMonth){
        Calendar rightNow = Calendar.getInstance(); 
        rightNow.add(Calendar.MONTH, nextMonth);
        int year = rightNow.get(Calendar.YEAR);
        int month = rightNow.get(Calendar.MONTH);
        month = month + 1;
        String monthString = "";
        if(month < 10)
            monthString += "0";
        monthString += month;
        
        
        return "" + year + monthString;
    }
}

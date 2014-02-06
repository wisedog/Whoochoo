/*
 * Copyright (C) 2013 Jongha Kim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.wisedog.android.whooing.utils;

import java.util.Calendar;
import java.util.Locale;

import android.content.Context;

import net.wisedog.android.whooing.Define;

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
    
    static public String getTodayLocale(Context context){
    	Define.gettingLoginInfo(context);
        Locale locale = new Locale(Define.LOCALE_LANGUAGE, Define.COUNTRY_CODE);
        Calendar calendar = Calendar.getInstance(); 
        java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM, locale);
        String date = df.format(calendar.getTime()).toString();
        return date;
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
    
    static public String getPreMonthLocale(Context context, int premonth){
    	Define.gettingLoginInfo(context);
        Locale locale = new Locale(Define.LOCALE_LANGUAGE, Define.COUNTRY_CODE);
        Calendar calendar = Calendar.getInstance(); 
        calendar.add(Calendar.MONTH, -(premonth));
        java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM, locale);
        String date = df.format(calendar.getTime()).toString();
        return date;
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
    
    static public String getLocaleDateInt(int date){
        int year = date / 10000;
        int month = (date % 10000) / 100;
        int day = month % 100;
        return getLocaleDateString(year,month,day,java.text.DateFormat.MEDIUM);        
    }
    
    static public String getLocaleDateString(int year, int month, int day){
        return getLocaleDateString(year,month,day,java.text.DateFormat.MEDIUM);
    }
    
    static public String getLocaleDateString(int year, int month, int day, int style){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Locale locale = new Locale(Define.LOCALE_LANGUAGE, Define.COUNTRY_CODE);
        java.text.DateFormat df = java.text.DateFormat.getDateInstance(style, locale);
        String date = df.format(calendar.getTime()).toString();
        return date;
    }
    
    static public int getWhooingStyleDateInt(int year, int month, int day){
        return ((year * 10000) + (month * 100) + day);
    }
    
}

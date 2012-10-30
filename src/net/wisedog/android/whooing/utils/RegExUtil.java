package net.wisedog.android.whooing.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExUtil {

	public static String takeToken(String string){
		String str = null;
		Pattern p = Pattern.compile("token=([^&]+)");
		Matcher m = p.matcher(string);
		if(m.find()){
			str = m.group(1);
		}
		return str;
	}
	
	/**
	 * 
	 * @param      url         Url to be parsed
	 * @param      paramName   GET parameter name to extract from given Url
	 * @return     the value of given parameter name. If the value is not exist, return null
	 * */
	public static String takeParam(String url, String paramName){
	    if(url == null){
	        return null;
	    }
	    String str = null;
	    Pattern p = Pattern.compile(paramName + "=([^&]+)");
	    Matcher m = p.matcher(url);
        if(m.find()){
            str = m.group(1);
        }
	    
	    return str;
	}
}

package net.wisedog.android.whooing.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExUtil {

	public static String takePin(String string){
		String str = null;
		Pattern p = Pattern.compile("pin=([^&]+)");
		Matcher m = p.matcher(string);
		if(m.find()){
			str = m.group(1);
		}
		return str;
	}
}

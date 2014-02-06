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

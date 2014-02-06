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

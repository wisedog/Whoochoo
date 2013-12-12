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
package net.wisedog.android.whooing;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;

public class Define {
    public static boolean DEBUG = false;
    
	public static String APP_ID = "125";
	public static String APP_SECRET = "1c5224ad2961704a6076c0bda127003933828a16";
	public static String PIN = null;
	public static String REAL_TOKEN = null;
	public static int USER_ID = -1;
	public static String TOKEN_SECRET = null;
	public static String APP_SECTION = null;
	
	public static String CURRENCY_CODE = null;
	public static String COUNTRY_CODE = null;;
	public static String TIMEZONE = null;
	public static String LOCALE_LANGUAGE = null;
	
	// General Key and Shared Preference Key
	public static String SHARED_PREFERENCE = "SHARED_AUTH";
	public static String KEY_SHARED_PIN = "SHARED_PIN";
	public static String KEY_SHARED_TOKEN = "SHARED_TOKEN";
	public static String KEY_SHARED_TOKEN_SECRET = "SHARED_TOKEN_SECRET";
	public static String KEY_SHARED_USER_ID = "SHARED_USER_ID";
	public static String KEY_SHARED_SECTION_ID = "SHARED_SECTION_ID";
	public static String KEY_SHARED_USER_INFO = "SHARED_USER_INFO";
	
	public static String KEY_SHARED_CURRENCY_CODE = "SHARED_CURRENCY_CODE";
	public static String KEY_SHARED_COUNTRY_CODE = "SHARED_COUNTRY_CODE";
	public static String KEY_SHARED_TIMEZONE = "SHARED_TIMEZONE";
	public static String KEY_SHARED_LOCALE_LANGUAGE = "SHARED_LOCALE_LANG";
	
	// Response/Request Key
	public static final int RESPONSE_EXIT = 10;
	public static final int REQUEST_AUTH = 50;
	public static final int REQUEST_NORMAL = 51;
	
	//Message
	public static final int MSG_REQ_AUTH = 100;
	public static final int MSG_FAIL = 101;
	public static final int MSG_AUTH_DONE = 102;
	public static final int MSG_AUTH_TOTAL_DONE = 103;
	public static final int MSG_USER_SETTING_DONE = 104;
	public static final int MSG_SETTING_DONE = 105;
	
	public static final int RESULT_OK = 200;
	public static final int RESULT_NONE_RECORD = 204;
	public static final int RESULT_ERROR = 400;
	public static final int RESULT_INSUFFIENT_API = 402;
	
	public static final int MSG_API_OK = 1000;
	public static final int MSG_API_FAIL = 1003;
	public static final int MSG_API_FAIL_WITH_404 = 404;
		
	public static final int API_GET_SECTIONS = 2010;
	public static final int API_GET_MAIN = 2020;
	public static final int API_GET_BUDGET = 2030;
	public static final int API_GET_BALANCE = 2040;

    public static final int API_GET_ENTRIES_LATEST = 2060;
    public static final int API_GET_ENTRIES_INSERT = 2061;
    public static final int API_GET_ENTRIES_LATEST_ITEMS = 2062;
    public static final int API_GET_PL = 2070;
    public static final int API_GET_MOUNTAIN = 2080;
    public static final int API_GET_ENTRIES = 2090;
    public static final int API_GET_BILL = 2100;
    
    // Post It
    public static final int API_GET_POST_IT = 2110;
    public static final int API_POST_POSTIT = 2111;
    public static final int API_PUT_POSTIT = 2112;
    public static final int API_DELETE_POSTIT = 2113;
    
    // Board
    public static final int API_GET_BOARD = 2190;
    public static final int API_GET_BOARD_BASE = 2200;
    public static final int API_GET_BOARD_FREE = 2200;
    public static final int API_GET_BOARD_MONEY_TALK = 2201;
    public static final int API_GET_BOARD_COUNSELING = 2202;
    public static final int API_GET_BOARD_WHOOING = 2203;
    
    //Board Article
    public static final int API_GET_BOARD_ARTICLE = 2210;
    public static final int API_POST_BOARD_ARTICLE = 2211;
    public static final int API_PUT_BOARD_ARTICLE = 2212;
    public static final int API_DELETE_BOARD_ARTICLE = 2213;
    
    
    public static final int API_POST_BOARD_REPLY = 2220;
    public static final int API_PUT_BOARD_REPLY = 2221;
    public static final int API_DELETE_BOARD_REPLY = 2222;
    
    //Board Comment
    public static final int API_GET_BOARD_COMMENT = 2230;
    public static final int API_POST_BOARD_COMMENT = 2231;
    public static final int API_PUT_BOARD_COMMENT = 2232;
    public static final int API_DELETE_BOARD_COMMENT = 2233;

    // Account
    public static final int API_GET_ACCOUNTS = 2300;
    public static final int API_POST_ACCOUNTS = 2301;
    public static final int API_PUT_ACCOUNTS = 2302;
    public static final int API_DELETE_ACCOUNTS = 2303;
    public static final int API_GET_ACCOUNT_EXISTS_ENTRIES = 2304;    
    
    // user
    public static final int API_GET_USER_INFO = 3000;
    public static final int API_PUT_USER_INFO = 3001;
    
    // etc
    public static final int API_GET_FREQUENT_ITEM = 3010;
    
    
	public static Typeface ROBOFONT = null;
    
    
    //flags
    //If there is need to be refreshed by insert some transaction or something like that
    //set this flag true
    public static boolean NEED_TO_REFRESH = false;
    
    public static boolean SHOW_NO_API_INFORM = false;
    
    public static void gettingLoginInfo(Context context){
    	if(context == null){
    		return;
    	}
    	/*
        if(Define.DEBUG){
            Define.REAL_TOKEN = "9772ff8e2f751ddf1c5cf74b0d9b328f392a4b71";
            Define.PIN = "339599";
            Define.TOKEN_SECRET = "7776e79057bc222254da0b108555afd86e3b7d3c";
            Define.APP_SECTION = "s10550";
            Define.USER_ID = 8955;
            Define.CURRENCY_CODE = "KRW";
            Define.COUNTRY_CODE="KR";
            Define.LOCALE_LANGUAGE = "ko";
        }*/
    	SharedPreferences prefs = context.getSharedPreferences(Define.SHARED_PREFERENCE, Context.MODE_PRIVATE);
        Define.REAL_TOKEN = prefs.getString(Define.KEY_SHARED_TOKEN, null);
        Define.PIN = prefs.getString(Define.KEY_SHARED_PIN, null);
        Define.TOKEN_SECRET = prefs.getString(Define.KEY_SHARED_TOKEN_SECRET, null);
        Define.APP_SECTION = prefs.getString(Define.KEY_SHARED_SECTION_ID, null);
        Define.USER_ID = prefs.getInt(Define.KEY_SHARED_USER_ID, 0);
        Define.CURRENCY_CODE = prefs.getString(Define.KEY_SHARED_CURRENCY_CODE, "USD");
        Define.COUNTRY_CODE = prefs.getString(Define.KEY_SHARED_COUNTRY_CODE, "US");
        Define.LOCALE_LANGUAGE = prefs.getString(Define.KEY_SHARED_LOCALE_LANGUAGE, "en");
        Define.TIMEZONE = prefs.getString(Define.KEY_SHARED_TIMEZONE, null);
    }

}

package net.wisedog.android.whooing;

public class Define {
	public static String APP_ID = "125";
	public static String APP_SECRET = "1c5224ad2961704a6076c0bda127003933828a16";
	public static String PIN = null;
	public static String REAL_TOKEN = null;
	public static String USER_ID = null;
	public static String TOKEN_SECRET = null;
	public static String APP_SECTION = null;
	
	// General Key and Shared Preference Key
	public static String SHARED_PREFERENCE = "SHARED_AUTH";
	public static String KEY_SHARED_PIN = "SHARED_PIN";
	public static String KEY_SHARED_TOKEN = "SHARED_TOKEN";
	public static String KEY_SHARED_TOKEN_SECRET = "SHARED_TOKEN_SECRET";
	public static String KEY_SHARED_USER_ID = "SHARED_USER_ID";
	public static String KEY_SHARED_SECTION_ID = "SHARED_SECTION_ID";
	public static String KEY_SHARED_USER_INFO = "SHARED_USER_INFO";
	
	// Response/Request Key
	public static final int RESPONSE_EXIT = 10;
	public static final int REQUEST_AUTH = 50;
	
	//Message
	public static final int MSG_REQ_AUTH = 100;
	public static final int MSG_FAIL = 101;
	public static final int MSG_AUTH_DONE = 102;
	public static final int MSG_AUTH_TOTAL_DONE = 103;
	
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
	public static final int API_GET_ACCOUNTS = 2050;
    public static final int API_GET_ENTRIES_LATEST = 2060;
    public static final int API_GET_ENTRIES_INSERT = 2061;
    public static final int API_GET_ENTRIES_LATEST_ITEMS = 2062;
    public static final int API_GET_PL = 2070;
    public static final int API_GET_MOUNTAIN = 2080;
    public static final int API_GET_ENTRIES = 2090;
    public static final int API_GET_BILL = 2100;
    public static final int API_GET_POST_IT = 2110;
    public static final int API_POST_POSTIT = 2111;
    public static final int API_PUT_POSTIT = 2112;
    public static final int API_DEL_POSTIT = 2113;
    public static final int API_GET_BOARD = 2190;
    public static final int API_GET_BOARD_BASE = 2200;
    public static final int API_GET_BOARD_FREE = 2200;
    public static final int API_GET_BOARD_MONEY_TALK = 2201;
    public static final int API_GET_BOARD_COUNSELING = 2202;
    public static final int API_GET_BOARD_WHOOING = 2203;
    public static final int API_GET_BOARD_ARTICLE = 2210;
    public static final int API_POST_BOARD_REPLY = 2211;
    public static final int API_POST_BOARD_COMMENT = 2212;
    public static final int API_GET_USER_INFO = 2300;
    public static final int API_GET_FREQUENT_ITEM = 2310;
    
	
    
    
    //flags
    //If there is need to be refreshed by insert some transaction or something like that
    //set this flag true
    public static boolean NEED_TO_REFRESH = false;

}

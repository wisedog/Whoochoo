package net.wisedog.android.whooing.network;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.activity.BbsFragmentActivity;
import net.wisedog.android.whooing.api.AccountsApi;
import net.wisedog.android.whooing.api.BbsApi;
import net.wisedog.android.whooing.api.Board;
import net.wisedog.android.whooing.api.Entries;
import net.wisedog.android.whooing.api.GeneralApi;
import net.wisedog.android.whooing.api.MainInfo;
import net.wisedog.android.whooing.api.PostIt;
import net.wisedog.android.whooing.api.Section;
import net.wisedog.android.whooing.utils.WhooingCalendar;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


/**
 * A thread for Rest API
 * @author Wisedog(me@wisedog.net)
 * */
public class ThreadRestAPI extends Thread {
	private Handler mHandler;
	private int mAPIName;
	private Bundle mBundle;
	
	public ThreadRestAPI(Handler mHandler, int apiName) {
        super();
        checkLoginInfo();
        this.mHandler = mHandler;
        this.mAPIName = apiName;
    }
	
	public ThreadRestAPI(Handler mHandler, int apiName, Bundle bundle) {
        super();
        //checkLoginInfo();
        this.mHandler = mHandler;
        this.mAPIName = apiName;
        this.mBundle = bundle;
    }

	private void checkLoginInfo() {
		/*if(Define.DEBUG){
            Define.REAL_TOKEN = "9772ff8e2f751ddf1c5cf74b0d9b328f392a4b71";
            Define.PIN = "339599";
            Define.TOKEN_SECRET = "7776e79057bc222254da0b108555afd86e3b7d3c";
            Define.APP_SECTION = "s10550";
            Define.USER_ID = 8955;
            Define.CURRENCY_CODE = "KRW";
            Define.COUNTRY_CODE="KR";
            Define.LOCALE_LANGUAGE = "ko";
        }
        
        SharedPreferences prefs = getSharedPreferences(Define.SHARED_PREFERENCE, MODE_PRIVATE);
        Define.REAL_TOKEN = prefs.getString(Define.KEY_SHARED_TOKEN, null);
        Define.PIN = prefs.getString(Define.KEY_SHARED_PIN, null);
        Define.TOKEN_SECRET = prefs.getString(Define.KEY_SHARED_TOKEN_SECRET, null);
        Define.APP_SECTION = prefs.getString(Define.KEY_SHARED_SECTION_ID, null);
        Define.USER_ID = prefs.getInt(Define.KEY_SHARED_USER_ID, 0);
        Define.CURRENCY_CODE = prefs.getString(Define.KEY_SHARED_CURRENCY_CODE, null);
        Define.COUNTRY_CODE = prefs.getString(Define.KEY_SHARED_COUNTRY_CODE, null);
        Define.TIMEZONE = prefs.getString(Define.KEY_SHARED_TIMEZONE, null);
        Log.i("wisedog", "user_id: " + Define.USER_ID + " app_section : " + Define.APP_SECTION + " real_token:" + Define.REAL_TOKEN
                + " pin : " + Define.PIN + " token_secret : " + Define.TOKEN_SECRET);
        Log.i("wisedog", "country: " + Define.COUNTRY_CODE + " currency: " + Define.CURRENCY_CODE 
                + " LanguageApp : " + Define.LANGUAGE_APP + " Timezone: " + Define.TIMEZONE );
		*/
	}

	@Override
	public void run() {
		JSONObject result = null;
		String startDate = null;
		String endDate = null;		 
		String requestUrl = null;
		
		int boardType = BbsFragmentActivity.BOARD_TYPE_FREE;
		String type = "";
		if(mAPIName == Define.API_GET_BOARD || mAPIName == Define.API_GET_BOARD_ARTICLE
				|| mAPIName == Define.API_POST_BOARD_COMMENT || mAPIName == Define.API_GET_BOARD_COMMENT){
		    if (mBundle == null) {
                Log.e(ThreadRestAPI.class.toString(),
                        "Not enough information for API_GET_BOARD or API_GET_BOARD_ARTICLE");
                sendMessage(null, mAPIName);
                return;
            }
		    boardType = mBundle.getInt("board_type");
	        if(boardType == BbsFragmentActivity.BOARD_TYPE_FREE){
	            type = "free";
	        }else if(boardType == BbsFragmentActivity.BOARD_TYPE_MONEY_TALK){
	            type = "moneytalk";
	        }else if(boardType == BbsFragmentActivity.BOARD_TYPE_COUNSELING){
	            type = "counseling";
	        }else if(boardType == BbsFragmentActivity.BOARD_TYPE_WHOOING){
	            type = "whooing";
	        }
		}
		
		        
		switch(mAPIName){
		case Define.API_GET_MAIN:
			MainInfo mainInfo = new MainInfo();
			result = mainInfo.getInfo(Define.APP_SECTION,Define.APP_ID, Define.REAL_TOKEN, 
					Define.APP_SECRET, Define.TOKEN_SECRET);
			break;
		case Define.API_GET_SECTIONS:
			Section section = new Section();
			result = section.getSections(Define.APP_ID, Define.REAL_TOKEN, 
					Define.APP_SECRET, Define.TOKEN_SECRET);
			break;
        case Define.API_GET_BUDGET:
            GeneralApi budget = new GeneralApi();
            if (mBundle == null) {
                Log.e(ThreadRestAPI.class.toString(), "Not enough information for API_GET_MOUNTAIN");
                sendMessage(null, mAPIName);
                return;
            }
            startDate = mBundle.getString("start_date");
            endDate = mBundle.getString("end_date");
            String account = mBundle.getString("account");
            String requestURL = "https://whooing.com/api/budget/" + account + ".json_array?section_id=" + 
                    Define.APP_SECTION + "&start_date=" + startDate + "&end_date=" + endDate;
            Log.i("wisedog", "app_id" + Define.APP_ID + " real_token : " + Define.REAL_TOKEN 
            		+ " app_secret : " + Define.APP_SECRET + " token_secret : " + Define.TOKEN_SECRET);
            result = budget.getInfo(requestURL, Define.APP_ID, Define.REAL_TOKEN, Define.APP_SECRET,
                    Define.TOKEN_SECRET);
            break;
        case Define.API_GET_BALANCE:
            String date = "";
            if (mBundle == null) {
                date = WhooingCalendar.getTodayYYYYMMDD();
            } else {
                date = mBundle.getString("end_date");
            }

            if (date == null) {
                date = WhooingCalendar.getTodayYYYYMMDD();
            }

            String budgetURL = "https://whooing.com/api/bs.json_array" + "?section_id="
                    + Define.APP_SECTION + "&end_date=" + date;
            GeneralApi balanceApi = new GeneralApi();
            result = balanceApi.getInfo(budgetURL, Define.APP_ID, Define.REAL_TOKEN,
                    Define.APP_SECRET, Define.TOKEN_SECRET);
            break;
            
		case Define.API_GET_MOUNTAIN:
		    if(mBundle == null){
                Log.e(ThreadRestAPI.class.toString(), "Not enough information for API_GET_MOUNTAIN");
                sendMessage(null, mAPIName);
                return;
            }
		    startDate = mBundle.getString("start_date");
		    endDate = mBundle.getString("end_date");
		    requestUrl = "https://whooing.com/api/mountain.json_array?section_id="+Define.APP_SECTION + 
		            "&start_date=" + startDate + "&end_date=" + endDate; 
		    GeneralApi mountain = new GeneralApi();
		    result = mountain.getInfo(requestUrl, Define.APP_ID, Define.REAL_TOKEN,
                     Define.APP_SECRET, Define.TOKEN_SECRET);
		    break;
		case Define.API_GET_ENTRIES_LATEST:
		    Entries entries = new Entries();
		    result = entries.getLatest(Define.APP_SECTION, Define.APP_ID, 
		            Define.REAL_TOKEN, Define.APP_SECRET, Define.TOKEN_SECRET, 5);
		    break;
		case Define.API_GET_ENTRIES_INSERT:
		    if(mBundle == null){
		        Log.e(ThreadRestAPI.class.toString(), "Not enough information for Insert Entry");
		        sendMessage(null, mAPIName);
		        return;
		    }
            Entries entryInsert = new Entries();
            result = entryInsert.insertEntry(Define.APP_SECTION, Define.APP_ID, 
                    Define.REAL_TOKEN, Define.APP_SECRET, Define.TOKEN_SECRET, mBundle);
            break;
		case Define.API_GET_PL:
			if (mBundle == null) {
				Log.e(ThreadRestAPI.class.toString(),
						"Not enough information for API_GET_PL");
				sendMessage(null, mAPIName);
				return;
			}
			startDate = mBundle.getString("start_date");
			endDate = mBundle.getString("end_date");
			requestUrl = "https://whooing.com/api/pl.json_array?section_id="
					+ Define.APP_SECTION + "&start_date=" + startDate
					+ "&end_date=" + endDate;
			GeneralApi pl = new GeneralApi();
			result = pl.getInfo(requestUrl, Define.APP_ID, Define.REAL_TOKEN,
					Define.APP_SECRET, Define.TOKEN_SECRET);
			break;
		case Define.API_GET_ENTRIES:
			if (mBundle == null) {
				Log.e(ThreadRestAPI.class.toString(),
						"Not enough information for API_GET_ENTRIES");
				sendMessage(null, mAPIName);
				return;
			}
			startDate = mBundle.getString("start_date");
			endDate = mBundle.getString("end_date");
			int limit = mBundle.getInt("limit");
			String itemText = mBundle.getString("item");
			String accountType = mBundle.getString("account");
			String accountId = mBundle.getString("account_id");
			
			//TODO https://whooing.com/#forum/developer/ko/api_reference/entries
			//TODO 여기 참조해서 각 parameter에 대해서 처리하기. 아마 max, limit, item밖에 쓰지않을 생각임
			//TODO Entries API참조해서 할것.
			requestUrl = "https://whooing.com/api/entries.json_array?section_id="
                    + Define.APP_SECTION + "&start_date=" + startDate
                    + "&end_date=" + endDate + "&limit=" + limit;
			
			if(itemText !=  null){
			    requestUrl = requestUrl + "&item=" + itemText;
			}
			if(accountType != null && accountId != null){
				requestUrl = requestUrl + "&account=" + accountType + "&account_id=" + accountId;
			}
			
			GeneralApi entriesAPI = new GeneralApi();
			result = entriesAPI.getInfo(requestUrl, Define.APP_ID, Define.REAL_TOKEN,
					Define.APP_SECRET, Define.TOKEN_SECRET);
			break;
		case Define.API_GET_BILL:
			if (mBundle == null) {
				Log.e(ThreadRestAPI.class.toString(),
						"Not enough information for API_GET_ENTRIES");
				sendMessage(null, mAPIName);
				return;
			}
			startDate = mBundle.getString("start_date");
			endDate = mBundle.getString("end_date");
			
			requestUrl = "https://whooing.com/api/bill.json_array?section_id="
					+ Define.APP_SECTION + "&start_date=" + startDate
					+ "&end_date=" + endDate;
			GeneralApi billAPI = new GeneralApi();
			result = billAPI.getInfo(requestUrl, Define.APP_ID, Define.REAL_TOKEN,
					Define.APP_SECRET, Define.TOKEN_SECRET);
			break;
			
		case Define.API_GET_POST_IT:
		    requestUrl = "https://whooing.com/api/post_it.json?section_id="
                    + Define.APP_SECTION;
		    GeneralApi postitAPI = new GeneralApi();
            result = postitAPI.getInfo(requestUrl, Define.APP_ID, Define.REAL_TOKEN,
                    Define.APP_SECRET, Define.TOKEN_SECRET);
		    break;
		case Define.API_GET_USER_INFO:
			requestUrl = "https://whooing.com/api/user.json";
			GeneralApi userAPI = new GeneralApi();
            result = userAPI.getInfo(requestUrl, Define.APP_ID, Define.REAL_TOKEN,
                    Define.APP_SECRET, Define.TOKEN_SECRET);
			break;
		case Define.API_GET_BOARD:
		    
			int pageBoard = mBundle.getInt("page");
			int limitBoard = mBundle.getInt("limit");
			requestUrl = "https://whooing.com/api/bbs/" + type + ".json?section_id="
                    + Define.APP_SECTION + "&page=" + pageBoard + "&limit=" + limitBoard;
		    GeneralApi boardAPI = new GeneralApi();
            result = boardAPI.getInfo(requestUrl, Define.APP_ID, Define.REAL_TOKEN,
                    Define.APP_SECRET, Define.TOKEN_SECRET);
			break;
		case Define.API_GET_BOARD_ARTICLE:
		    int bbsId = mBundle.getInt("bbs_id");
		    requestUrl = "https://whooing.com/api/bbs/" + type + "/" + bbsId + ".json?section_id="
                    + Define.APP_SECTION;
		    GeneralApi bbsArticleAPI = new GeneralApi();
            result = bbsArticleAPI.getInfo(requestUrl, Define.APP_ID, Define.REAL_TOKEN,
                    Define.APP_SECRET, Define.TOKEN_SECRET);
		    break;
		case Define.API_POST_BOARD_REPLY:
			Board boardReply = new Board();
			result = boardReply.postReply(Define.APP_SECTION, Define.APP_ID, 
					Define.REAL_TOKEN, Define.APP_SECRET, Define.TOKEN_SECRET, mBundle);
			break;
		case Define.API_POST_BOARD_COMMENT:
			Board boardComment = new Board();
			result = boardComment.postComment(Define.APP_SECTION, Define.APP_ID, 
					Define.REAL_TOKEN, Define.APP_SECRET, Define.TOKEN_SECRET, mBundle);
			break;
		case Define.API_POST_POSTIT:
			PostIt postPostIt = new PostIt();
			result = postPostIt.postPostIt(Define.APP_SECTION, Define.APP_ID, 
					Define.REAL_TOKEN, Define.APP_SECRET, Define.TOKEN_SECRET, mBundle);
			break;
		case Define.API_DELETE_POSTIT:
		    PostIt delPostIt = new PostIt();
		    result = delPostIt.delPostIt(Define.APP_SECTION, Define.APP_ID, 
                    Define.REAL_TOKEN, Define.APP_SECRET, Define.TOKEN_SECRET, mBundle);
		    break;
		case Define.API_PUT_POSTIT:
            PostIt putPostIt = new PostIt();
            result = putPostIt.putPostIt(Define.APP_SECTION, Define.APP_ID, 
                    Define.REAL_TOKEN, Define.APP_SECRET, Define.TOKEN_SECRET, mBundle);
            break;
		case Define.API_GET_FREQUENT_ITEM:
		    requestUrl = "https://whooing.com/api/frequent_items.json_array?section_id=" + Define.APP_SECTION;
            GeneralApi frequentApi = new GeneralApi();
            result = frequentApi.getInfo(requestUrl, Define.APP_ID, Define.REAL_TOKEN,
                    Define.APP_SECRET, Define.TOKEN_SECRET);
            break;
		case Define.API_GET_ENTRIES_LATEST_ITEMS:
		    requestUrl = "https://whooing.com/api/entries/latest_items.json?section_id="
                    + Define.APP_SECTION;
            GeneralApi entriesLastestItemAPI = new GeneralApi();
            result = entriesLastestItemAPI.getInfo(requestUrl, Define.APP_ID, Define.REAL_TOKEN,
                    Define.APP_SECRET, Define.TOKEN_SECRET);
		    break;
		case Define.API_GET_BOARD_COMMENT:
		    int bbsId1 = mBundle.getInt("bbs_id");
		    String commentId = mBundle.getString("comment_id");
            requestUrl = "https://whooing.com/api/bbs/" + type + "/" + bbsId1 + "/" + commentId + ".json";
            GeneralApi bbsCommentAPI = new GeneralApi();
            result = bbsCommentAPI.getInfo(requestUrl, Define.APP_ID, Define.REAL_TOKEN,
                    Define.APP_SECRET, Define.TOKEN_SECRET);
		    break;
		case Define.API_DELETE_BOARD_ARTICLE:
		    BbsApi bbsApi = new BbsApi();
		    result = bbsApi.delBbsArticle(Define.APP_SECTION, Define.APP_ID, Define.REAL_TOKEN,
                    Define.APP_SECRET, Define.TOKEN_SECRET, mBundle);
		    break;
		case Define.API_POST_BOARD_ARTICLE:
		    BbsApi bbsPostApi = new BbsApi();
            result = bbsPostApi.postBbsArticle(Define.APP_SECTION, Define.APP_ID, Define.REAL_TOKEN,
                    Define.APP_SECRET, Define.TOKEN_SECRET, mBundle);
            break;
		case Define.API_PUT_BOARD_ARTICLE:
			BbsApi bbsPutApi = new BbsApi();
			result = bbsPutApi.putBbsArticle(Define.APP_SECTION, Define.APP_ID, Define.REAL_TOKEN,
                    Define.APP_SECRET, Define.TOKEN_SECRET, mBundle);
			break;
		case Define.API_DELETE_BOARD_REPLY:
		    BbsApi bbsDelReplyApi = new BbsApi();
            result = bbsDelReplyApi.delBbsReply(Define.APP_SECTION, Define.APP_ID, Define.REAL_TOKEN,
                    Define.APP_SECRET, Define.TOKEN_SECRET, mBundle);
            break;
		case Define.API_PUT_BOARD_REPLY:
			BbsApi bbsPutReplyApi = new BbsApi();
            result = bbsPutReplyApi.putBbsReply(Define.APP_SECTION, Define.APP_ID, Define.REAL_TOKEN,
                    Define.APP_SECRET, Define.TOKEN_SECRET, mBundle);
            break;
		case Define.API_DELETE_BOARD_COMMENT:
			BbsApi bbsDeleteCommentApi = new BbsApi();
            result = bbsDeleteCommentApi.delBbsComment(Define.APP_SECTION, Define.APP_ID, Define.REAL_TOKEN,
                    Define.APP_SECRET, Define.TOKEN_SECRET, mBundle);
            break;
            
         //Accounts 
		case Define.API_GET_ACCOUNTS:
            GeneralApi api = new GeneralApi();
            result = api.getInfo("https://whooing.com/api/accounts.json_array?section_id="+Define.APP_SECTION, 
                    Define.APP_ID, Define.REAL_TOKEN,
                     Define.APP_SECRET, Define.TOKEN_SECRET);
            break;
            
		case Define.API_PUT_ACCOUNTS:
		    AccountsApi putAccounts = new AccountsApi();
		    result = putAccounts.postOrPutAccounts(AccountsApi.TYPE_PUT, Define.APP_SECTION, Define.APP_ID, Define.REAL_TOKEN,
                    Define.APP_SECRET, Define.TOKEN_SECRET, mBundle);
		    break;
		case Define.API_POST_ACCOUNTS:
		    AccountsApi postAccounts = new AccountsApi();
		    result = postAccounts.postOrPutAccounts(AccountsApi.TYPE_POST, Define.APP_SECTION, Define.APP_ID, Define.REAL_TOKEN,
                    Define.APP_SECRET, Define.TOKEN_SECRET, mBundle);           
		    break;
		case Define.API_DELETE_ACCOUNTS:
		    AccountsApi delAccounts = new AccountsApi();
		    result = delAccounts.deleteAccounts(Define.APP_SECTION, Define.APP_ID, Define.REAL_TOKEN,
                    Define.APP_SECRET, Define.TOKEN_SECRET, mBundle);           
		    break;
		case Define.API_GET_ACCOUNT_EXISTS_ENTRIES:
		    AccountsApi existAccounts = new AccountsApi();
		    result = existAccounts.existTransaction(Define.APP_SECTION, Define.APP_ID, Define.REAL_TOKEN,
                    Define.APP_SECRET, Define.TOKEN_SECRET, mBundle);
		    break;
		    
		default:
			Log.e(ThreadRestAPI.class.toString(), "Unknown API");
			return;
		}
    		
		sendMessage(result, mAPIName);
		super.run();
	}
	
	private void sendMessage(JSONObject result, int arg){
		Message msg = new Message();
		if(result != null){
			msg.what = Define.MSG_API_OK;
		}
		else{
			msg.what = Define.MSG_API_FAIL;
		}
		msg.obj = result;
		msg.arg1 = arg;
		mHandler.sendMessage(msg);
	}
}

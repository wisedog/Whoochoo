package net.wisedog.android.whooing.network;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.activity.BbsFragmentActivity;
import net.wisedog.android.whooing.api.Board;
import net.wisedog.android.whooing.api.Entries;
import net.wisedog.android.whooing.api.GeneralApi;
import net.wisedog.android.whooing.api.MainInfo;
import net.wisedog.android.whooing.api.Section;
import net.wisedog.android.whooing.utils.WhooingCalendar;

import org.json.JSONObject;

import android.app.Activity;
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
	private Activity mActivity;
	private int mAPIName;
	private Bundle mBundle;
	
	public ThreadRestAPI(Handler mHandler, Activity activity, int apiName) {
		super();
		this.mHandler = mHandler;
		this.mActivity = activity;
		this.mAPIName = apiName;
	}
	
	public ThreadRestAPI(Handler mHandler, Activity activity, int apiName, Bundle bundle) {
        super();
        this.mHandler = mHandler;
        this.mActivity = activity;
        this.mAPIName = apiName;
        this.mBundle = bundle;
    }
	
	public ThreadRestAPI(Handler mHandler, int apiName) {
        super();
        this.mHandler = mHandler;
        this.mAPIName = apiName;
    }
	
	public ThreadRestAPI(Handler mHandler, int apiName, Bundle bundle) {
        super();
        this.mHandler = mHandler;
        this.mAPIName = apiName;
        this.mBundle = bundle;
    }

	@Override
	public void run() {
		JSONObject result = null;
		String startDate = null;
		String endDate = null;		 
		String requestUrl = null;
		
		int boardType = BbsFragmentActivity.BOARD_TYPE_FREE;
		String type = "";
		if(mAPIName == Define.API_GET_BOARD || mAPIName == Define.API_GET_BOARD_ARTICLE){
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
		case Define.API_GET_ACCOUNTS:
			GeneralApi api = new GeneralApi();
			result = api.getInfo("https://whooing.com/api/accounts.json_array?section_id="+Define.APP_SECTION, 
					Define.APP_ID, Define.REAL_TOKEN,
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
			
			//TODO https://whooing.com/#forum/developer/ko/api_reference/entries
			//TODO 여기 참조해서 각 parameter에 대해서 처리하기. 아마 max, limit, item밖에 쓰지않을 생각임
			//TODO Entries API참조해서 할것. 
			requestUrl = "https://whooing.com/api/entries.json_array?section_id="
					+ Define.APP_SECTION + "&start_date=" + startDate
					+ "&end_date=" + endDate + "&limit=" + limit;
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

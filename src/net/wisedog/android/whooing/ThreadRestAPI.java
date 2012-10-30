package net.wisedog.android.whooing;

import net.wisedog.android.whooing.api.Balance;
import net.wisedog.android.whooing.api.Budget;
import net.wisedog.android.whooing.api.MainInfo;
import net.wisedog.android.whooing.api.Section;

import org.json.JSONObject;

import android.app.Activity;
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
	
	public ThreadRestAPI(Handler mHandler, Activity activity, int apiName) {
		super();
		this.mHandler = mHandler;
		this.mActivity = activity;
		this.mAPIName = apiName;
	}

	@Override
	public void run() {
		JSONObject result = null;
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
			Budget budget = new Budget();
			result = budget.getBudget(Define.APP_SECTION, Define.APP_ID, 
					Define.REAL_TOKEN, Define.APP_SECRET, Define.TOKEN_SECRET);
			break;
		case Define.API_GET_BALANCE:
			Balance balance = new Balance();
			result = balance.getBalance(Define.APP_SECTION, Define.APP_ID, 
					Define.REAL_TOKEN, Define.APP_SECRET, Define.TOKEN_SECRET, null);
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

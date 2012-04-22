package net.wisedog.android.whooing;

import java.util.Calendar;

import net.wisedog.android.whooing.utils.JSONUtil;
import net.wisedog.android.whooing.utils.SHA1Util;

import org.json.JSONArray;
import org.json.JSONException;
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
		switch(mAPIName){
		case Define.API_SECTION:
			
			break;
		default:
			Log.e(ThreadRestAPI.class.toString(), "Unknown API");
		}
		JSONObject result = getSection();
		try {
			//JSONObject obj = result.getJSONObject("result");
			JSONArray array = result.getJSONArray("results");
			
			JSONObject obj1 = (JSONObject) array.get(0);
			obj1.getString("111");
			//obj.getString("123");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	//TODO Support multiple section
		
		
		Message msg = new Message();
		if(result != null){
			msg.what = Define.MSG_API_OK;
		}
		else{
			msg.what = Define.MSG_API_FAIL;
		}
		msg.obj = result;
		msg.arg1 = mAPIName;
		mHandler.sendMessage(msg);
		super.run();
	}
	
	
	private JSONObject getSection(){
		String url = "https://whooing.com/api/sections.json";
		
		String sig_raw = Define.APP_KEY+"|" +Define.TOKEN_SECRET;
		String signiture = SHA1Util.SHA1(sig_raw);
		String headerValue = "app_id="+Define.APP_ID+ ",token="+Define.TOKEN + ",signiture="+ signiture+
				",nounce="+"abcde"+",timestamp="+Calendar.getInstance().getTimeInMillis();

		try {
			JSONObject result = JSONUtil.getJSONObject(url, "X-API-KEY", headerValue);
			return result;
			
		} catch (JSONException e) {
			Log.e(ThreadHandshake.class.toString(), "JSON Error");
			e.printStackTrace();
		} 
		return null;
	}
}

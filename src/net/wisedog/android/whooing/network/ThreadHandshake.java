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
package net.wisedog.android.whooing.network;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.utils.JSONUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * A thread for network. 
 * */
public class ThreadHandshake extends Thread {
	private Handler mHandler;
	private Activity mActivity;
	private boolean isAfterAuth;
	private String mSecondToken = null;
	
	public ThreadHandshake(Handler mHandler, Activity activity, boolean isAfterAuth) {
		super();
		this.mHandler = mHandler;
		this.mActivity = activity;
		this.isAfterAuth = isAfterAuth;
	}
	
	public ThreadHandshake(Handler mHandler, Activity activity, boolean isAfterAuth, String secondToken) {
        super();
        this.mHandler = mHandler;
        this.mActivity = activity;
        this.isAfterAuth = isAfterAuth;
        this.mSecondToken = secondToken;
    }
	

	@Override
	public void run() {
		if(!isAfterAuth){	//Before Auth
			String result = initHandshake();
			if(result != null){
			    Message msg = new Message();
			    msg.what = Define.MSG_REQ_AUTH;
			    msg.obj = result;
			    mHandler.sendMessage(msg);
			}
		}
		else{	//After Auth
			boolean result = true;
			if(mSecondToken == null || Define.PIN == null){
				result = false;
			}else{
				result = thirdHandshake(mSecondToken, Define.PIN);
			}	
			if(result == false){
				Message msg = new Message();		
				msg.what = Define.MSG_FAIL;
				mHandler.sendMessage(msg);
			}
		}
		
		super.run();
	}	
	/**
	 * Do initial handshake to server. Getting a token in this phase
	 * @return	Returns true if it sucess
	 * */
	public String initHandshake(){
		String url = "https://whooing.com/app_auth/request_token?app_id="
						+ Define.APP_ID+"&app_secret="+Define.APP_SECRET;
		String firstToken = null;
		try {
			JSONObject result = JSONUtil.getJSONObject(url, null, null);
			firstToken = result.getString("token");
			Log.i("wisedog", "FIRST TOKEN:"+firstToken);
		} catch (JSONException e) {
			Log.e(ThreadHandshake.class.toString(), "JSON Error");
			e.printStackTrace();
		} 
		if(firstToken != null){
			Log.d("wisedog", "TOKEN : " + firstToken);
		}
		
		return firstToken;
	}
	
	/**
	 * After Authorizing user, get token secret and user id
	 * @return	Return true if it success or false 
	 * */
	private boolean thirdHandshake(String secondToken, String pin){
		String token_secret = null;
		int user_id = -1;
		String realToken = null;
		if(secondToken == null || pin == null)
			return false;
		
		String url = "https://whooing.com/app_auth/access_token?app_id="+Define.APP_ID+
				"&app_secret="+Define.APP_SECRET+ "&token="+secondToken+"&pin="+pin;
		
		try {
			JSONObject result = JSONUtil.getJSONObject(url, null, null);
			token_secret = result.getString("token_secret");
			Log.d("wisedog", "Token secret:"+token_secret);
			user_id = result.getInt("user_id");
			Log.d("wisedog", "USER ID:"+user_id);
			realToken = result.getString("token");
			Log.d("wisedog", "Real Token:"+realToken);
			
		} catch (JSONException e) {
			Log.e(ThreadHandshake.class.toString(), "JSON Error");
			e.printStackTrace();
			return false;
		}
		
		SharedPreferences prefs = mActivity.getSharedPreferences(Define.SHARED_PREFERENCE,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(Define.KEY_SHARED_TOKEN_SECRET, token_secret);
		editor.putInt(Define.KEY_SHARED_USER_ID, user_id);
		editor.putString(Define.KEY_SHARED_TOKEN, realToken);
		editor.commit();
		
		Define.TOKEN_SECRET = token_secret;
		Define.USER_ID = user_id;
		Define.REAL_TOKEN = realToken;
		
		Message msg = new Message();		
		msg.what = Define.MSG_AUTH_DONE;
		mHandler.sendMessage(msg);
		return true;
	}
}
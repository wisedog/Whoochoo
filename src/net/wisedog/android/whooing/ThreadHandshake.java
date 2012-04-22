package net.wisedog.android.whooing;

import java.io.IOException;
import java.io.InputStream;

import net.wisedog.android.whooing.utils.StringUtil;
import net.wisedog.android.whooing.utils.JSONUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * A thread for network. 
 * @author Wisedog(me@wisedog.net)
 * */
public class ThreadHandshake extends Thread {
	private Handler mHandler;
	private Activity mActivity;
	private boolean isAfterAuth;
	
	public ThreadHandshake(Handler mHandler, Activity activity, boolean isAfterAuth) {
		super();
		this.mHandler = mHandler;
		this.mActivity = activity;
		this.isAfterAuth = isAfterAuth;
	}

	@Override
	public void run() {
		if(!isAfterAuth){	//Before Auth
			boolean result = initHandshake();
			if(result == true)
				result = secondHandshake(Define.TOKEN);
		}
		else{	//After Auth
			boolean result = true;
			if(Define.TOKEN == null || Define.PIN == null){
				result = false;
			}else{
				result = thirdHandshake(Define.TOKEN, Define.PIN);
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
	public boolean initHandshake(){
		String url = "https://whooing.com/app_auth/request_token?app_id="+ Define.APP_ID+"&app_secret="+Define.APP_KEY;
		String token = null;
		try {
			JSONObject result = JSONUtil.getJSONObject(url, null, null);
			token = result.getString("token");
		} catch (JSONException e) {
			Log.e(ThreadHandshake.class.toString(), "JSON Error");
			e.printStackTrace();
		} 
		if(token != null){
			Define.TOKEN = token;
		}
		
		SharedPreferences prefs = mActivity.getSharedPreferences(Define.SHARED_PREFERENCE,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(Define.KEY_SHARED_TOKEN, token);
		editor.commit();
		
		return true;
	}
	

	
	/**
	 * Getting PIN number. If shaking hand with server is successful, then 
	 * invoke an activity to try to authorize user
	 * @return Returns true if it success
	 * */
	private boolean secondHandshake(String token){
		if(token == null)
			return false;
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(
				"https://whooing.com/app_auth/authorize?token="+ Define.TOKEN);
		try{
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				String contentStr = StringUtil.convertStreamToString(content);
				
				Message msg = new Message();		
				msg.what = Define.MSG_REQ_AUTH;
				msg.obj = contentStr;
				mHandler.sendMessage(msg);
			}
			else {
				Log.e(WhooingMain.class.toString(), "Failed to download file");
				return false;
			}
		}
		catch(ClientProtocolException e){
			Log.e(WhooingMain.class.toString(), "HttpResponse Failed");
			return false;
		} 
		catch (IOException e) {
			Log.e(WhooingMain.class.toString(), "HttpResponse IO Failed");
			return false;
		}
		return false;
	}
	
	/**
	 * After Authorizing user, get token secret and user id
	 * @return	Return true if it success or false 
	 * */
	private boolean thirdHandshake(String token, String pin){
		String token_secret = null;
		String user_id = null;
		if(token == null || pin == null)
			return false;
		
		String url = "https://whooing.com/app_auth/access_token?app_id="+Define.APP_ID+
				"&app_secret="+Define.APP_KEY+ "&token="+token+"&pin="+pin;
		
		try {
			JSONObject result = JSONUtil.getJSONObject(url, null, null);
			token_secret = result.getString("token_secret");
			user_id = result.getString("user_id");
		} catch (JSONException e) {
			Log.e(ThreadHandshake.class.toString(), "JSON Error");
			e.printStackTrace();
			return false;
		}
		
		SharedPreferences prefs = mActivity.getSharedPreferences(Define.SHARED_PREFERENCE,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(Define.KEY_SHARED_TOKEN_SECRET, token_secret);
		editor.putString(Define.USER_ID, user_id);
		editor.commit();
		
		Define.TOKEN_SECRET = token_secret;
		Define.USER_ID = user_id;
		
		Message msg = new Message();		
		msg.what = Define.MSG_DONE;
		mHandler.sendMessage(msg);
		return true;
	}
}
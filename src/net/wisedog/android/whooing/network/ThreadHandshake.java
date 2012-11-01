package net.wisedog.android.whooing.network;

import java.io.IOException;
import java.io.InputStream;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.WhooingMain;
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
		String url = "https://whooing.com/app_auth/request_token?app_id="+ Define.APP_ID+"&app_secret="+Define.APP_SECRET;
		String firstToken = null;
		try {
			JSONObject result = JSONUtil.getJSONObject(url, null, null);
			firstToken = result.getString("token");
			Log.i("whooing", "FIRST TOKEN:"+firstToken);
		} catch (JSONException e) {
			Log.e(ThreadHandshake.class.toString(), "JSON Error");
			e.printStackTrace();
		} 
		if(firstToken != null){
			//Define.FIRST_TOKEN = firstToken;
			Log.d("whooing", "TOKEN : " + firstToken);
		}
		
		/*SharedPreferences prefs = mActivity.getSharedPreferences(Define.SHARED_PREFERENCE,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(Define.KEY_SHARED_TOKEN, firstToken);
		editor.commit();*/
		
		return firstToken;
	}
	

	
	/**
	 * Getting PIN number. If shaking hand with server is successful, then 
	 * invoke an activity to try to authorize user
	 * @return Returns true if it success
	 * */
	@Deprecated
	private boolean secondHandshake(String token){
		if(token == null)
			return false;
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(
				"https://whooing.com/app_auth/authorize?token=");//+ Define.FIRST_TOKEN);
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
	private boolean thirdHandshake(String secondToken, String pin){
		String token_secret = null;
		String user_id = null;
		String realToken = null;
		if(secondToken == null || pin == null)
			return false;
		
		String url = "https://whooing.com/app_auth/access_token?app_id="+Define.APP_ID+
				"&app_secret="+Define.APP_SECRET+ "&token="+secondToken+"&pin="+pin;
		
		try {
			JSONObject result = JSONUtil.getJSONObject(url, null, null);
			token_secret = result.getString("token_secret");
			Log.d("whooing", "Token secret:"+token_secret);
			user_id = result.getString("user_id");
			Log.d("whooing", "USER ID:"+user_id);
			realToken = result.getString("token");
			Log.d("whooing", "Real Token:"+user_id);
			
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
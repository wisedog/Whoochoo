package net.wisedog.android.whooing;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import net.wisedog.android.whooing.utils.StringUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;


/**
 * A thread for Rest API
 * @author Wisedog(me@wisedog.net)
 * */
public class ThreadRestAPI extends Thread {
	private Handler mHandler;
	private Activity mActivity;
	
	public ThreadRestAPI(Handler mHandler, Activity activity) {
		super();
		this.mHandler = mHandler;
		this.mActivity = activity;
	}

	@Override
	public void run() {
		getSection();
		super.run();
	}
	
	
	private String getSection(){
		String url = "https://whooing.com/api/sections.json";
		SecretKeySpec key = null;
		Mac mac = null;
		try {
			key = new SecretKeySpec((Define.APP_KEY+"|" +Define.TOKEN_SECRET).getBytes("UTF-8"), "HmacSHA1");
			mac = Mac.getInstance("HmacSHA1");
			mac.init(key);
		} catch (UnsupportedEncodingException e2) {
			Log.e(ThreadRestAPI.class.toString(), "UnsupportEncodingException in REST Thread");
			return null;
		} catch (NoSuchAlgorithmException e1) {
			Log.e(ThreadRestAPI.class.toString(), "NoSuchAlgorithmException in REST Thread");
			return null;
		} catch (InvalidKeyException e1) {
			Log.e(ThreadRestAPI.class.toString(), "InvalidKeyException in REST Thread");
			return null;
		}	
		
		byte[] bytes = mac.doFinal();
		String signature = Base64.encodeToString(bytes, NORM_PRIORITY);
		
		String header = "app_id="+Define.APP_ID+ ",token="+Define.TOKEN + ",signiture="+ signature+
				",nounce="+"abcde"+",timestamp="+Calendar.getInstance().getTimeInMillis();
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(url);
		getRequest.addHeader("X-API-KEY", "X-API-KEY:"+header);
		
		
		try {
			HttpResponse response = client.execute(getRequest);
			String result = EntityUtils.toString(response.getEntity()); 
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				
				// Load the requested page converted to a string into a JSONObject.
                JSONObject re;
				try {
					// Get the token value
					re = new JSONObject(StringUtil.convertStreamToString(content));
					Log.i("wisedog","1231");
				} catch (JSONException e) {
					Log.e(ThreadRestAPI.class.toString(), "Failed to get JSON token value");
					return null;
				}
			}
			else{
				Message msg = new Message();		
				msg.what = Define.MSG_API_FAIL;
				mHandler.sendMessage(msg);
				return null;
			}

			JSONObject object = (JSONObject) new JSONTokener(result).nextValue();
			JSONArray records = object.getJSONArray("records");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		return null;
	}
}

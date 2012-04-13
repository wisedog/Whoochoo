package net.wisedog.android.whooing;

import java.io.IOException;
import java.io.InputStream;

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
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import net.wisedog.android.whooing.utils.StringUtil;

public class WhooingMain extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whooing_main);
		Define.TOKEN = initHandshake();
		Toast.makeText(this, Define.TOKEN, 2000).show();
	}

	/**
	 * Do initial handshake to server. Getting a token in this phase
	 * @return	Returns token or null
	 * */
	public String initHandshake(){
		String token = null;
	    HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(
				"https://whooing.com/app_auth/request_token?app_id="+ Define.APP_ID+"&app_secret="+Define.APP_KEY+"");
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				
				// Load the requested page converted to a string into a JSONObject.
                JSONObject result;
				try {
					// Get the token value
					result = new JSONObject(StringUtil.convertStreamToString(content));
					token = result.getString("token");
				} catch (JSONException e) {
					Log.e(WhooingMain.class.toString(), "Failed to get JSON token value");
					return null;
				}
			} else {
				Log.e(WhooingMain.class.toString(), "Failed to download file");
				return null;
			}
		} catch (ClientProtocolException e) {
			Log.e(WhooingMain.class.toString(), "HttpResponse Failed");
			token = null;
		} catch (IOException e) {
			Log.e(WhooingMain.class.toString(), "HttpResponse IO Failed");
			token = null;
		}
		
		return token;
	}
	/**
	 * 
	 * @return Returns Pin or null
	 * */
	private String secondHandshake(String token){
		String pin = null;
		if(Define.TOKEN == null)
			return null;
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
				contentStr +="";
				//TODO Complete getting Pin code. I should go home, so I commit incompletely 
			}
		}
		catch(ClientProtocolException e){
			
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pin;
	}
    

    @Override
	public void onBackPressed() {
		// block back event
    	//TODO Ask exit
	}
	
}

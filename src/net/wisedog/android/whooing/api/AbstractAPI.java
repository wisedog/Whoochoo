package net.wisedog.android.whooing.api;

import java.util.Calendar;

import net.wisedog.android.whooing.utils.JSONUtil;
import net.wisedog.android.whooing.utils.SHA1Util;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Abstract class of whooing API classes
 * @author	Wisedog(me@wisedog.net)
 * */
public class AbstractAPI {
	/**
	 * Make header and call API
	 * @param	url		URL string to call
	 * @return	Returns JSONObject if it success, or null
	 * @throws NotEnoughApiException 
	 * */
	protected JSONObject callAPI(String url, String appID, String token, String appKey, 
			String tokenSecret){
		String sig_raw = appKey+"|" +tokenSecret;
		String signiture = SHA1Util.SHA1(sig_raw);
		String headerValue = "app_id="+appID+ ",token="+token + ",signiture="+ signiture+
				",nounce="+"abcde"+",timestamp="+Calendar.getInstance().getTimeInMillis();

		try {
			JSONObject result = JSONUtil.getJSONObject(url, "X-API-KEY", headerValue);
			return result;
			
		} catch (JSONException e) {
			Log.e(AbstractAPI.class.toString(), "callAPI error");
			e.printStackTrace();
		} 
		return null;
	}
}

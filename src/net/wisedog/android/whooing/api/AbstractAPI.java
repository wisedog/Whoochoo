package net.wisedog.android.whooing.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.utils.JSONUtil;
import net.wisedog.android.whooing.utils.SHA1Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
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
	protected JSONObject callApi(String url, String appID, String token, String appKey, 
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
	
	protected JSONObject callApiPost(String url, String appID, String token, String appKey, 
            String tokenSecret, String appSection, Bundle b){
	    String sig_raw = appKey+"|" +tokenSecret;
        String signiture = SHA1Util.SHA1(sig_raw);
        String headerValue = "app_id="+appID+ ",token="+token + ",signiture="+ signiture+
                ",nounce="+"abcde"+",timestamp="+Calendar.getInstance().getTimeInMillis();
        
        AccountsEntity left = b.getParcelable("l_account");
        AccountsEntity right = b.getParcelable("r_account");
        Double amount = b.getDouble("money");
        
        int date = b.getInt("entry_date");
        
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("section_id", appSection));
        nameValuePairs.add(new BasicNameValuePair("entry_date", String.valueOf(date)));
        nameValuePairs.add(new BasicNameValuePair("l_account", left.accountType));
        nameValuePairs.add(new BasicNameValuePair("l_account_id", left.account_id));
        nameValuePairs.add(new BasicNameValuePair("r_account", right.accountType));
        nameValuePairs.add(new BasicNameValuePair("r_account_id", right.account_id));
        nameValuePairs.add(new BasicNameValuePair("item", b.getString("item")));
        nameValuePairs.add(new BasicNameValuePair("money", String.valueOf(amount)));
        nameValuePairs.add(new BasicNameValuePair("memo", b.getString("memo")));
        
        try {
            JSONObject result = JSONUtil.getJSONObjectPost(url, "X-API-KEY", headerValue, nameValuePairs);
            return result;
            
        } catch (JSONException e) {
            Log.e(AbstractAPI.class.toString(), "callAPI error");
            e.printStackTrace();
        } 
        
        return null;
	}
}

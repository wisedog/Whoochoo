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
package net.wisedog.android.whooing.api;

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
	 * Make header and call API(GET)
	 * @param  url		URL string to call
	 * @param  appID   issued from whooing application id
	 * @param  token   token
	 * @param  appKey  application key
	 * @param  tokenSecret secret token
	 * @return	Returns JSONObject if it success, or null
	 * */
	protected JSONObject callApi(String url, String appID, String token, String appKey, 
			String tokenSecret){
	    //make up header
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
	
	/**
	 * Call raw POST api 
	 * */
	protected JSONObject callRawApiPost(String url, String appID, String token, String appKey, 
            String tokenSecret, String appSection, List<NameValuePair> nameValuePairs){
		//make up header
	    String sig_raw = appKey+"|" +tokenSecret;
        String signiture = SHA1Util.SHA1(sig_raw);
        String headerValue = "app_id="+appID+ ",token="+token + ",signiture="+ signiture+
                ",nounce="+"abcde"+",timestamp="+Calendar.getInstance().getTimeInMillis();
        
        //shot!
        try {
            JSONObject result = JSONUtil.getJSONObjectPost(url, "X-API-KEY", headerValue, nameValuePairs);
            return result;
            
        } catch (JSONException e) {
            Log.e(AbstractAPI.class.toString(), "callAPI error");
            e.printStackTrace();
        } 
        
        return null;
	}
	
	/**
     * Make header and call API for Transaction Entries
     * @param  url      URL string to call
     * @param  appID   issued from whooing application id
     * @param  token   token
     * @param  appKey  application key
     * @param  tokenSecret secret token
     * @param   appSection  section
     * @param   b   Bundle for post value
     * @return  Returns JSONObject if it success, or null 
     * */
	protected JSONObject callApiPost(String url, String appID, String token, String appKey, 
            String tokenSecret, String appSection, Bundle b){
	    //make up header
	    String sig_raw = appKey+"|" +tokenSecret;
        String signiture = SHA1Util.SHA1(sig_raw);
        String headerValue = "app_id="+appID+ ",token="+token + ",signiture="+ signiture+
                ",nounce="+"abcde"+",timestamp="+Calendar.getInstance().getTimeInMillis();
        
        //make post value
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
        
        //shot!
        try {
            JSONObject result = JSONUtil.getJSONObjectPost(url, "X-API-KEY", headerValue, nameValuePairs);
            return result;
            
        } catch (JSONException e) {
            Log.e(AbstractAPI.class.toString(), "callAPI error");
            e.printStackTrace();
        } 
        
        return null;
	}
	
	protected JSONObject callApiDelete(String url, String appID, String token, String appKey, 
            String tokenSecret, String appSection){
        //make up header
        String sig_raw = appKey+"|" +tokenSecret;
        String signiture = SHA1Util.SHA1(sig_raw);
        String headerValue = "app_id="+appID+ ",token="+token + ",signiture="+ signiture+
                ",nounce="+"abcde"+",timestamp="+Calendar.getInstance().getTimeInMillis();
        
        //shot!
        try {
            JSONObject result = JSONUtil.getJSONObjectDelete(url, "X-API-KEY", headerValue);
            return result;
            
        } catch (JSONException e) {
            Log.e(AbstractAPI.class.toString(), "callAPI error");
            e.printStackTrace();
        } 
        
        return null;
    }
	
	/**
     * Call raw POST api 
     * */
    protected JSONObject callApiPut(String url, String appID, String token, String appKey, 
            String tokenSecret, String appSection, List<NameValuePair> nameValuePairs){
        //make up header
        String sig_raw = appKey+"|" +tokenSecret;
        String signiture = SHA1Util.SHA1(sig_raw);
        String headerValue = "app_id="+appID+ ",token="+token + ",signiture="+ signiture+
                ",nounce="+"abcde"+",timestamp="+Calendar.getInstance().getTimeInMillis();
        
        //shot!
        try {
            JSONObject result = JSONUtil.getJSONObjectPut(url, "X-API-KEY", headerValue, nameValuePairs);
            return result;
            
        } catch (JSONException e) {
            Log.e(AbstractAPI.class.toString(), "callAPI error");
            e.printStackTrace();
        } 
        
        return null;
    }
	
	
}

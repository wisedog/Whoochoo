package net.wisedog.android.whooing.api;

import org.json.JSONObject;


/**
 * Section API routine class
 * @author	Wisedog(me@wisedog.net)
 * @see		https://whooing.com/#forum/developer/en/api_reference/sections
 * */
public class Section extends AbstractAPI {
	/**
	 * Get all sections
	 * @param	appID		Application ID
	 * @param	token		Token
	 * @param	appKey		Application Key
	 * @param	tokenSecret	Secret key for token
	 * @return	Returns JSONObject for result, or null if it fails		
	 * */
	public JSONObject getSections(String appID, String token, String appKey, String tokenSecret){
		String url = "https://whooing.com/api/sections.json_array";
		return callAPI(url, appID, token, appKey, tokenSecret);
	}
}

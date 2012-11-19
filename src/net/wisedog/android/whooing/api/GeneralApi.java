/**
 * Copyright (c) 2012 Wisedog.net
 */
package net.wisedog.android.whooing.api;

import org.json.JSONObject;

/**
 * @author Wisedog(me@wisedog.net)
 */
public class GeneralApi extends AbstractAPI {
    /**
     * Call RPC from given API
     * @param   url         Url address to call
     * @param   appID       Application ID
     * @param   token       Token
     * @param   appKey      Application Key
     * @param   tokenSecret Secret key for token
     * @return  Returns JSONObject for result, or null if it fails      
     * */
    public JSONObject getInfo(String url, String appID, 
    		String token, String appKey, String tokenSecret){
        return callApi(url, appID, token, appKey, tokenSecret);
    }
}

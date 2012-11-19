/**
 * 
 */
package net.wisedog.android.whooing.api;

import org.json.JSONObject;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class UserAPI extends AbstractAPI {
    /**
     * Get User ID
     * @param   appID       Application ID
     * @param   token       Token
     * @param   appKey      Application Key
     * @param   tokenSecret Secret key for token
     * @return  Returns JSONObject for result, or null if it fails      
     * */
    public JSONObject getUserInfo(String appID, String token, String appKey, String tokenSecret){
        String url = "https://whooing.com/api/user.json_array";
        return callApi(url, appID, token, appKey, tokenSecret);
    }
}

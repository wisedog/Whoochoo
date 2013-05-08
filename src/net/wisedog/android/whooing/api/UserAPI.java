/**
 * 
 */
package net.wisedog.android.whooing.api;

import java.util.ArrayList;
import java.util.List;

import net.wisedog.android.whooing.Define;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.Bundle;

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
        String url = "https://whooing.com/api/user.json";
        return callApi(url, appID, token, appKey, tokenSecret);
    }

    /**
    * Get User ID
    * @param   appID       Application ID
    * @param   token       Token
    * @param   appKey      Application Key
    * @param   tokenSecret Secret key for token
    * @return  Returns JSONObject for result, or null if it fails      
    * */
    public JSONObject putUserInfo(String appID, String token, String appKey, 
            String tokenSecret, Bundle bundle) {
        if(bundle == null){
            return null;
        }
        String entriesURL = "https://whooing.com/api/user.json";
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("username", bundle.getString("username")));
        JSONObject result = callApiPut(entriesURL, appID, token, appKey, tokenSecret, Define.APP_SECTION, nameValuePairs);
        return result;
    }
}

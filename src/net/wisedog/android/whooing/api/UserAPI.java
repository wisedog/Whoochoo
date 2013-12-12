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

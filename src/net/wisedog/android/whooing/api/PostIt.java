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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.Bundle;

public class PostIt extends AbstractAPI{

	public JSONObject postPostIt(String appSection, String appID, String token, 
            String appKey, String tokenSecret, Bundle bundle){
        String entriesURL = "https://whooing.com/api/post_it.json";
        if(bundle == null){
            return null;
        }
        String page = bundle.getString("page");
        String contents = bundle.getString("contents");
        
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("page", page));
        nameValuePairs.add(new BasicNameValuePair("contents", contents));
        nameValuePairs.add(new BasicNameValuePair("section_id", appSection));
        
        JSONObject result = callRawApiPost(entriesURL, appID, token, appKey, tokenSecret, appSection, nameValuePairs);
        return result;
    }

    /**
     * @param appSection
     * @param appID
     * @param token
     * @param appKey
     * @param tokenSecret
     * @param bundle
     * @return
     */
    public JSONObject delPostIt(String appSection, String appID, String token, 
            String appKey, String tokenSecret, Bundle bundle) {
        if(bundle == null){
            return null;
        }
        String entriesURL = "https://whooing.com/api/post_it/" + bundle.getInt("post_it_id") + ".json";
        JSONObject result = callApiDelete(entriesURL, appID, token, appKey, tokenSecret, appSection);
        return result;
    }

    /**
     * @param aPP_SECTION
     * @param aPP_ID
     * @param rEAL_TOKEN
     * @param aPP_SECRET
     * @param tOKEN_SECRET
     * @param mBundle
     * @return
     */
    public JSONObject putPostIt(String appSection, String appID, String token, 
            String appKey, String tokenSecret, Bundle bundle) {
    	 if(bundle == null){
             return null;
         }
         String entriesURL = "https://whooing.com/api/post_it/" + bundle.getInt("post_it_id") + ".json";
         List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
         nameValuePairs.add(new BasicNameValuePair("contents", bundle.getString("contents")));
         nameValuePairs.add(new BasicNameValuePair("section_id", appSection));
         JSONObject result = callApiPut(entriesURL, appID, token, appKey, tokenSecret, appSection, nameValuePairs);
         return result;
    }

}

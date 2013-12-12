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

public class Board extends AbstractAPI{

	public JSONObject postReply(String appSection, String appID, String token, 
            String appKey, String tokenSecret, Bundle bundle){
        String entriesURL = "https://whooing.com/api/bbs/";
        if(bundle == null){
            return null;
        }
        String category = bundle.getString("category");
        int bbsId = bundle.getInt("bbs_id");
        String contents = bundle.getString("contents");
        entriesURL = entriesURL + category + "/" + bbsId + ".json";
        
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("contents", contents));
        
        JSONObject result = callRawApiPost(entriesURL, appID, token, appKey, tokenSecret, appSection, nameValuePairs);
        return result;
    }

	public JSONObject postComment(String appSection, String appID, String token, 
            String appKey, String tokenSecret, Bundle bundle){
		String entriesURL = "https://whooing.com/api/bbs/";
        if(bundle == null){
            return null;
        }
        String category = bundle.getString("category");
        int bbsId = bundle.getInt("bbs_id");
        String contents = bundle.getString("contents");
        String commentId = bundle.getString("comment_id");
        entriesURL = entriesURL + category + "/" + bbsId + "/" + commentId + ".json";
        
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("contents", contents));
        
        JSONObject result = callRawApiPost(entriesURL, appID, token, appKey, tokenSecret, appSection, nameValuePairs);
        return result;
	} 
}

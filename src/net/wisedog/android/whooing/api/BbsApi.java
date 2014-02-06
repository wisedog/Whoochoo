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

import net.wisedog.android.whooing.activity.BbsListFragment;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.Bundle;

public class BbsApi extends AbstractAPI{

	public JSONObject postBbsArticle(String appSection, String appID, String token, 
            String appKey, String tokenSecret, Bundle bundle){
	    if(bundle == null){
            return null;
        }
        String entriesURL = "https://whooing.com/api/bbs/" + getBoardType(bundle.getInt("board_type")) + ".json";
        
        String subject = bundle.getString("subject");
        String contents = bundle.getString("contents");
        
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("contents", contents));
        nameValuePairs.add(new BasicNameValuePair("subject", subject));
        
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
    public JSONObject delBbsArticle(String appSection, String appID, String token, 
            String appKey, String tokenSecret, Bundle bundle) {
        if(bundle == null){
            return null;
        }
        String boardType = getBoardType(bundle.getInt("board_type"));
        String entriesURL = "https://whooing.com/api/bbs/" + boardType + "/" +  bundle.getInt("bbs_id") + ".json";
        JSONObject result = callApiDelete(entriesURL, appID, token, appKey, tokenSecret, appSection);
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
    public JSONObject delBbsReply(String appSection, String appID, String token, 
            String appKey, String tokenSecret, Bundle bundle) {
        if(bundle == null){
            return null;
        }
        String boardType = getBoardType(bundle.getInt("board_type"));
        String entriesURL = "https://whooing.com/api/bbs/" + boardType + "/" +  bundle.getInt("bbs_id") 
                + "/" + bundle.getString("comment_id") + ".json";
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
    public JSONObject putBbsArticle(String appSection, String appID, String token, 
            String appKey, String tokenSecret, Bundle bundle) {
    	 if(bundle == null){
             return null;
         }
    	 String boardType = getBoardType(bundle.getInt("board_type"));
         String entriesURL = "https://whooing.com/api/bbs/" + boardType + "/" + bundle.getInt("bbs_id") + ".json";
         List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
         
         String subject = bundle.getString("subject");
         String contents = bundle.getString("contents");
         
         nameValuePairs.add(new BasicNameValuePair("contents", contents));
         nameValuePairs.add(new BasicNameValuePair("subject", subject));
         JSONObject result = callApiPut(entriesURL, appID, token, appKey, tokenSecret, appSection, nameValuePairs);
         return result;
    }
    
    public JSONObject putBbsReply(String appSection, String appID, String token, 
            String appKey, String tokenSecret, Bundle bundle) {
    	if(bundle == null){
            return null;
        }
   	 	String boardType = getBoardType(bundle.getInt("board_type"));
		String entriesURL = "https://whooing.com/api/bbs/" + boardType + "/"
				+ bundle.getInt("bbs_id") + "/"
				+ bundle.getString("comment_id") + ".json";
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        
        String contents = bundle.getString("contents");
        
        nameValuePairs.add(new BasicNameValuePair("contents", contents));
        JSONObject result = callApiPut(entriesURL, appID, token, appKey, tokenSecret, appSection, nameValuePairs);
        return result;
	}
    
    public JSONObject delBbsComment(String appSection, String appID, String token, 
            String appKey, String tokenSecret, Bundle bundle) {
        if(bundle == null){
            return null;
        }
        String boardType = getBoardType(bundle.getInt("board_type"));
        String entriesURL = "https://whooing.com/api/bbs/" + boardType + "/" +  bundle.getInt("bbs_id") 
                + "/" + bundle.getString("comment_id") + "/" + bundle.getInt("addition_id") + ".json";
        JSONObject result = callApiDelete(entriesURL, appID, token, appKey, tokenSecret, appSection);
        return result;
    }
    
    
    /**
     * @param   type    board type integer value
     * @return board type string for composition URL
     * */
    public String getBoardType(int type){
        String boardType = "free";
        if(type == BbsListFragment.BOARD_TYPE_FREE){
            boardType = "free";
        }else if(type == BbsListFragment.BOARD_TYPE_MONEY_TALK){
            boardType = "moneytalk";
        }else if(type == BbsListFragment.BOARD_TYPE_COUNSELING){
            boardType = "counseling";
        }else if(type == BbsListFragment.BOARD_TYPE_WHOOING){
            boardType = "whooing";
        }
        return boardType;
    }

	

}

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

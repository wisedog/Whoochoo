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
}

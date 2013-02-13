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

}

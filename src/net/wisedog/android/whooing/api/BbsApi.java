package net.wisedog.android.whooing.api;

import java.util.ArrayList;
import java.util.List;

import net.wisedog.android.whooing.activity.BbsFragmentActivity;

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
     * @param aPP_SECTION
     * @param aPP_ID
     * @param rEAL_TOKEN
     * @param aPP_SECRET
     * @param tOKEN_SECRET
     * @param mBundle
     * @return
     */
    //TODO Should be modify for bbs
    public JSONObject putBbsArticle(String appSection, String appID, String token, 
            String appKey, String tokenSecret, Bundle bundle) {
    	 if(bundle == null){
             return null;
         }
         String entriesURL = "https://whooing.com/api/bbs/" + bundle.getInt("bbs_id") + ".json";
         List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
         nameValuePairs.add(new BasicNameValuePair("contents", bundle.getString("contents")));
         nameValuePairs.add(new BasicNameValuePair("section_id", appSection));
         JSONObject result = callApiPut(entriesURL, appID, token, appKey, tokenSecret, appSection, nameValuePairs);
         return result;
    }
    
    public String getBoardType(int type){
        String boardType = "free";
        if(type == BbsFragmentActivity.BOARD_TYPE_FREE){
            boardType = "free";
        }else if(type == BbsFragmentActivity.BOARD_TYPE_MONEY_TALK){
            boardType = "moneytalk";
        }else if(type == BbsFragmentActivity.BOARD_TYPE_COUNSELING){
            boardType = "counseling";
        }else if(type == BbsFragmentActivity.BOARD_TYPE_WHOOING){
            boardType = "whooing";
        }
        return boardType;
    }

}

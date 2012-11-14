/**
 * 
 */
package net.wisedog.android.whooing.api;

import org.json.JSONObject;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class Entries extends AbstractAPI{
    
    public JSONObject getLatest(String appSection, String appID, String token, 
            String appKey, String tokenSecret, int limit){
        String entriesURL = "https://whooing.com/api/entries/latest.json_array"
                +"?section_id=" +appSection + "&limit=" + limit;
        JSONObject result = callAPI(entriesURL, appID, token, appKey, tokenSecret);
        return result;
    }
}

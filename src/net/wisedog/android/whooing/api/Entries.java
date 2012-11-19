/**
 * 
 */
package net.wisedog.android.whooing.api;

import net.wisedog.android.whooing.R;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.widget.EditText;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class Entries extends AbstractAPI{
    
    public JSONObject getLatest(String appSection, String appID, String token, 
            String appKey, String tokenSecret, int limit){
        String entriesURL = "https://whooing.com/api/entries/latest.json_array"
                +"?section_id=" +appSection + "&limit=" + limit;
        JSONObject result = callApi(entriesURL, appID, token, appKey, tokenSecret);
        return result;
    }
    
    public JSONObject insertEntry(String appSection, String appID, String token, 
            String appKey, String tokenSecret, Bundle bundle){
        String entriesURL = "https://whooing.com/api/entries.json_array";
        if(bundle == null){
            return null;
        }
        
        String serializedString = getSerializedObject(bundle);
        if(serializedString == null){
            return null;
        }
        
        JSONObject result = callApiPost(entriesURL, appID, token, appKey, tokenSecret, serializedString);
        return result;
    }
    
    public String getSerializedObject(Bundle b){
        if(b == null){
            return null;
        }
        
        /*bundle.putInt("entry_date", formattedDate);
        bundle.putParcelable("l_account", mLeftAccount);
        bundle.putParcelable("r_account", mRightAccount);
        bundle.putString("item", ((EditText)findViewById(R.id.add_transaction_edit_item)).getText().toString());
        bundle.putDouble("money", amountDouble);
        bundle.putString("memo", "");*/
        return null;
    }
}

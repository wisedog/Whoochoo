/**
 * 
 */
package net.wisedog.android.whooing.api;

import java.util.ArrayList;
import java.util.List;

import net.wisedog.android.whooing.db.AccountsEntity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.Bundle;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class AccountsApi extends AbstractAPI {
    public static int TYPE_POST = 0;
    public static int TYPE_PUT = 1;
    
    /**
     * Delete accounts info
     * @param   appID       Application ID
     * @param   token       Token
     * @param   appKey      Application Key
     * @param   tokenSecret Secret key for token
     * @param   bundle      Bundle 
     * @return  Returns JSONObject for result, or null if it fails      
     * */
    public JSONObject deleteAccounts(String appSection, String appID, String token, 
            String appKey, String tokenSecret, Bundle bundle) {
        if(bundle == null){
            return null;
        }
        String accountId = bundle.getString("account_id");
        String accountType = bundle.getString("account_type");
        String entriesURL = "https://whooing.com/api/accounts/" + accountType + "/" +  accountId
                + "/" + appSection + ".json";
        JSONObject result = callApiDelete(entriesURL, appID, token, appKey, tokenSecret, appSection);
        
        return result;
    }

    /**
     * Put or Post accounts info. Put and post requires almost same parameters
     * @param   appID       Application ID
     * @param   token       Token
     * @param   appKey      Application Key
     * @param   tokenSecret Secret key for token
     * @param   bundle      Bundle 
     * @return  Returns     JSONObject for result, or null if it fails      
     * */
    public JSONObject postOrPutAccounts(int type, String appSection, String appID, String token, 
            String appKey, String tokenSecret, Bundle bundle) {
        if(bundle == null){
            return null;
        }
        
        AccountsEntity entity = bundle.getParcelable("account_entity");
        if(entity == null){
            return null;
        }
        
        String entriesURL = "https://whooing.com/api/accounts/" + entity.accountType + ".json";
        
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("title", entity.title));
        nameValuePairs.add(new BasicNameValuePair("section_id", appSection));
        nameValuePairs.add(new BasicNameValuePair("type", entity.type));
        nameValuePairs.add(new BasicNameValuePair("open_date", String.valueOf(entity.open_date)));
        nameValuePairs.add(new BasicNameValuePair("category", entity.category));
        
        if(entity.memo != null){
            nameValuePairs.add(new BasicNameValuePair("memo", entity.memo));
        }
        
        if(entity.category.compareTo("creditcard") == 0){
            nameValuePairs.add(new BasicNameValuePair("opt_use_date", entity.opt_use_date));
            nameValuePairs.add(new BasicNameValuePair("opt_pay_date", String.valueOf(entity.opt_pay_date)));
            nameValuePairs.add(new BasicNameValuePair("opt_pay_account_id", entity.opt_pay_account_id));
            
        }else if(entity.category.compareTo("checkcard") == 0){
            nameValuePairs.add(new BasicNameValuePair("opt_pay_account_id", entity.opt_pay_account_id));
        }        
        
        JSONObject result = null;
        if(type == TYPE_POST){
            result = callRawApiPost(entriesURL, appID, token, appKey, tokenSecret, appSection, nameValuePairs);
        }else if(type == TYPE_PUT){
            if(entity.close_date != 0){
                nameValuePairs.add(new BasicNameValuePair("is_close", String.valueOf(entity.close_date)));
            }
            
            result = callApiPut(entriesURL, appID, token, appKey, tokenSecret, appSection, nameValuePairs);
        }
        
        return result;
    }
}

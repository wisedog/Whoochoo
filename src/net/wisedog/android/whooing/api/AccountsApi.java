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
    
    public JSONObject existTransaction(String appSection, String appID, String token, 
            String appKey, String tokenSecret, Bundle bundle){
        if(bundle == null){
            return null;
        }
        String accountId = bundle.getString("account_id");
        String accountType = bundle.getString("account_type");
        
        if(accountId == null || accountType == null){
            return null;
        }
        String entriesURL = "https://whooing.com/api/accounts/" + accountType + "/"+ accountId + "/exists.json"
                +"?section_id=" +appSection;
        JSONObject result = callApi(entriesURL, appID, token, appKey, tokenSecret);
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
        
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        String accountType = null;
        AccountsEntity entity = bundle.getParcelable("account_entity");
        // from deactivate dialog 
        if(entity == null){
            accountType = bundle.getString("account_type");
        }
        else{   //from account modify dialog
            accountType = entity.accountType;
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
        }        
        
        String entriesURL = null;
        
        JSONObject result = null;
        if(type == TYPE_POST){
            entriesURL = "https://whooing.com/api/accounts/" + accountType + ".json";
            result = callRawApiPost(entriesURL, appID, token, appKey, tokenSecret, appSection, nameValuePairs);
        }else if(type == TYPE_PUT){
            if(entity == null){
                nameValuePairs.add(new BasicNameValuePair("is_close", bundle.getString("is_close")));
            }else{
                if(entity.close_date != 0){
                    nameValuePairs.add(new BasicNameValuePair("is_close", "y"));
                }
                else{
                    nameValuePairs.add(new BasicNameValuePair("is_close", "n"));
                }
            }
            
            nameValuePairs.add(new BasicNameValuePair("section_id", appSection));
            
            String accountId = bundle.getString("account_id");
            entriesURL =  "https://whooing.com/api/accounts/" + accountType + "/" + accountId + ".json";
            result = callApiPut(entriesURL, appID, token, appKey, tokenSecret, appSection, nameValuePairs);
        }
        return result;
    }
}

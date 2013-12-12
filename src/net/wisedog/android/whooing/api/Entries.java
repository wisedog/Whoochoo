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

import net.wisedog.android.whooing.db.AccountsEntity;

import org.json.JSONObject;

import android.os.Bundle;

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
        
        /*String serializedString = getSerializedObject(appSection, bundle);
        if(serializedString == null){
            return null;
        }*/
        
        JSONObject result = callApiPost(entriesURL, appID, token, appKey, tokenSecret, appSection, bundle);
        return result;
    }
    
    public String getSerializedObject(String sectionId, Bundle b){
        if(b == null){
            return null;
        }
        String value = "";
        AccountsEntity left = b.getParcelable("l_account");
        AccountsEntity right = b.getParcelable("r_account");
        Double amount = b.getDouble("money");
        
        value = "?section_id=" + sectionId + "&entry_date=" + b.getInt("entry_date")
                + "&l_account=" + left.accountType + "&l_account_id=" + left.account_id 
                + "&r_account=" + right.accountType + "&r_account_id" + right.account_id
                + "&item=" + b.getString("item") + "&money=" + String.valueOf(amount)
                + "&memo="
                ;
        return value;
    }
}

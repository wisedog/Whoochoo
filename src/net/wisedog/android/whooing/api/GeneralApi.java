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

import org.json.JSONObject;

/**
 * @author Wisedog(me@wisedog.net)
 */
public class GeneralApi extends AbstractAPI {
    /**
     * Call RPC from given API
     * @param   url         Url address to call
     * @param   appID       Application ID
     * @param   token       Token
     * @param   appKey      Application Key
     * @param   tokenSecret Secret key for token
     * @return  Returns JSONObject for result, or null if it fails      
     * */
    public JSONObject getInfo(String url, String appID, 
    		String token, String appKey, String tokenSecret){
        return callApi(url, appID, token, appKey, tokenSecret);
    }
}

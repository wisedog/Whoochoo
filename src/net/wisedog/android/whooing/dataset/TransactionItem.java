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
package net.wisedog.android.whooing.dataset;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class TransactionItem{
    public String date;
    public String item;
    public double money;
    public String l_account;
    public String r_account;
    public String l_account_id;
    public String r_account_id;
    public long Entry_ID;
    
    public TransactionItem(String _date, String _item, double amount, String leftAccount,
            String leftAccountId, String rightAccount, String rightAccountId) {
        super();
        date = _date;
        item = _item;
        money = amount;
        l_account = leftAccount;
        l_account_id = leftAccountId;
        r_account = rightAccount;
        r_account_id = rightAccountId;
    }
    /**
     * @param object
     * @throws JSONException 
     */
    public TransactionItem(JSONObject object) throws JSONException {
        date = object.getString("entry_date");
        item = object.getString("item");
        money = object.getDouble("money");
        l_account = object.getString("l_account");
        l_account_id = object.getString("l_account_id");
        r_account = object.getString("r_account");
        r_account_id = object.getString("r_account_id");
    }
}

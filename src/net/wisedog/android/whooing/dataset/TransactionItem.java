/**
 * 
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

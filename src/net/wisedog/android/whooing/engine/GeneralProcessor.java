/**
 * 
 */
package net.wisedog.android.whooing.engine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.db.WhooingDbOpenHelper;
import android.content.Context;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class GeneralProcessor {
	Context mContext = null;
	
	public GeneralProcessor(Context context) {
		mContext = context;
	}

	/**
	 * Check if exists accounts table
	 * @return		return true if it exists and it has 1 record at least, else return false
	 * */
	public boolean checkingAccountsInfo() {
		WhooingDbOpenHelper dbHelper = new WhooingDbOpenHelper(mContext);
		if(dbHelper.getAccountsInfoCount() > 0){
			return true;
		}
		return false;
	}

    /**
     * Insert items with given parameter value
     * @param objResult     JSON format value
     */
    public boolean fillAccountsTable(JSONObject objResult) throws JSONException {
        if(objResult == null){
            return false;
        }
        
        //There are five sections. - assets, liabilites, capital, income, expenses
        
        mContext.deleteDatabase(WhooingDbOpenHelper.DATABASE_NAME);
        WhooingDbOpenHelper dbHelper = new WhooingDbOpenHelper(mContext);
        AccountsEntity info = null;
        String accountsType[] = new String[]{"assets", "liabilities", "capital", "income", "expenses"};
        for(int j = 0; j < accountsType.length; j++){
            JSONArray assetsArray = objResult.getJSONArray(accountsType[j]);
            for(int i = 0; i < assetsArray.length(); i++){
                info = new AccountsEntity(accountsType[j], (JSONObject) assetsArray.get(i));
                dbHelper.addAccountEntity(info);
            }
        }
        return true;
    }

    /**
     * @param accountName   String 
     * @return  An AccountsEntity if it successed, else return null
     */
    public AccountsEntity findAccountById(String accountName) {
        WhooingDbOpenHelper dbHelper = new WhooingDbOpenHelper(mContext);
        AccountsEntity entity = dbHelper.getAccountById(accountName);
        return entity;
    }
}

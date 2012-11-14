/**
 * 
 */
package net.wisedog.android.whooing.engine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.db.AccountsDbOpenHelper;
import android.content.Context;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class GeneralProcessor {
    public static final String PLUS    =   "+";
    public static final String MINUS    =   "-";
    public static final String NOTHING    =   "";
    
	protected Context mContext = null;
	
	public GeneralProcessor(Context context) {
		mContext = context;
	}

	/**
	 * Check if exists accounts table
	 * @return		return true if it exists and it has 1 record at least, else return false
	 * */
	public boolean checkingAccountsInfo() {
		AccountsDbOpenHelper dbHelper = new AccountsDbOpenHelper(mContext);
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
        
        mContext.deleteDatabase(AccountsDbOpenHelper.DATABASE_NAME);
        AccountsDbOpenHelper dbHelper = new AccountsDbOpenHelper(mContext);
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
        AccountsDbOpenHelper dbHelper = new AccountsDbOpenHelper(mContext);
        AccountsEntity entity = dbHelper.getAccountById(accountName);
        return entity;
    }
    
    /**
     * 좌/우변에 따라서 +, - 기호를 붙여줌 
     * @param   isLeft      flag weather left or right
     * @param   entity      Account Entity       
     * @return      if +, return 1, - , return -1, nothing return 0
     * */
    static public String getPlusMinus(AccountsEntity entity, boolean isLeft){
        if(entity == null){
            return NOTHING;
        }
        if(isLeft){
            if(entity.accountType.compareTo("assets") == 0){
                return PLUS;
            }else if(entity.accountType.compareTo("liabilities") == 0){
                return MINUS;
            }else if(entity.accountType.compareTo("capital") == 0){
                return MINUS;
            }else if(entity.accountType.compareTo("income") == 0){
                return NOTHING;
            }else if(entity.accountType.compareTo("expenses") == 0){
                return NOTHING;
            }
        }else{
            if(entity.accountType.compareTo("assets") == 0){
                return MINUS;
            }else if(entity.accountType.compareTo("liabilities") == 0){
                return PLUS;
            }else if(entity.accountType.compareTo("capital") == 0){
                return PLUS;
            }else if(entity.accountType.compareTo("income") == 0){
                return NOTHING;
            }else if(entity.accountType.compareTo("expenses") == 0){
                return NOTHING;
            }
        } 
        return NOTHING;
    }
}

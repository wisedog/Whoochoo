/**
 * 
 */
package net.wisedog.android.whooing.engine;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.R;
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
    public boolean fillAccountsTable(final JSONObject objResult) throws JSONException {
        if(objResult == null){
            return false;
        }
        
        //There are five sections. - assets, liabilites, capital, income, expenses
        
        mContext.deleteDatabase(AccountsDbOpenHelper.DATABASE_NAME);
        final AccountsDbOpenHelper dbHelper = new AccountsDbOpenHelper(mContext);
        final String accountsType[] = new String[]{"assets", "liabilities", "capital", "income", "expenses"};
        new Thread(new Runnable(){

            @Override
            public void run() {
                for(int j = 0; j < accountsType.length; j++){
                    JSONArray assetsArray = null;
                    AccountsEntity info = null;
                    try {
                        assetsArray = objResult.getJSONArray(accountsType[j]);
                        for(int i = 0; i < assetsArray.length(); i++){
                            info = new AccountsEntity(accountsType[j], (JSONObject) assetsArray.get(i));
                            dbHelper.addAccountEntity(info);
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }                    
                }                
            }            
        }).start();
        return true;
    }
    
    /**
     * @return  Return all account information in database
     * */
    public ArrayList<AccountsEntity> getAllAccount(){
        AccountsDbOpenHelper dbHelper = new AccountsDbOpenHelper(mContext);
        return dbHelper.getAllAccountsInfo();
    }

    /**
     * @param accountName   String 
     * @return  An AccountsEntity if it successed, else return null
     */
    public AccountsEntity findAccountById(String accountId) {
        AccountsDbOpenHelper dbHelper = new AccountsDbOpenHelper(mContext);
        AccountsEntity entity = null;
        if(accountId.compareTo("x0") == 0){ //Deleted item
            entity = new AccountsEntity();
            entity.title = mContext.getString(R.string.text_deleted);
            entity.account_id = "x0";
            entity.accountType = "N/A";
        }
        else{
            entity = dbHelper.getAccountById(accountId);
        }
        
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

    /**
     * Add an account entity to database 
     * @param   entity      entity to add
     * */
    public boolean addAccount(AccountsEntity entity) {
        AccountsDbOpenHelper dbHelper = new AccountsDbOpenHelper(mContext);
        return dbHelper.addAccountEntity(entity);
    }

    /**
     * Modify given account entity 
     * @param   entity      entity to update
     * */
    public boolean modifyAccount(AccountsEntity entity) {
        AccountsDbOpenHelper dbHelper = new AccountsDbOpenHelper(mContext);
        return dbHelper.updateAccount(entity);
    }
    
    /**
     * Delete give account entity
     * @param   entity      entity to delete
     * */
    public boolean deleteAccount(AccountsEntity entity){
        AccountsDbOpenHelper dbHelper = new AccountsDbOpenHelper(mContext);
        return dbHelper.deleteAccount(entity); 
    }
}

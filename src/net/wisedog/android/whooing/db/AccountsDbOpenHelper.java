/**
 * 
 */
package net.wisedog.android.whooing.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class AccountsDbOpenHelper extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    public static final String DATABASE_NAME = "whooing";
 
    // Contacts table name
    public static final String TABLE_ACCOUNTS = "accounts";
 
    // Contacts Table Columns names
    private static final String KEY_ACCOUNT_ID = "account_id";
    private static final String KEY_ACCOUNT_TYPE = "account_type";
    private static final String KEY_TYPE = "type";
    private static final String KEY_TITLE = "title";
    private static final String KEY_MEMO = "memo";
    private static final String KEY_OPEN_DATE = "open_date";
    private static final String KEY_CLOSE_DATE = "close_date";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_OPT_USE_DATE = "opt_use_date";
    private static final String KEY_OPT_PAY_DATE = "opt_pay_date";
    private static final String KEY_OPT_PAY_ACCOUNT_ID = "opt_pay_account_id";
    
    public AccountsDbOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public AccountsDbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        
    }

    /* (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("wisedog", "DB Helper onCreate");
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_ACCOUNTS + "("
                + KEY_ACCOUNT_ID + " TEXT PRIMARY KEY," 
                + KEY_ACCOUNT_TYPE + " TEXT,"
                + KEY_TYPE + " TEXT," 
                + KEY_TITLE + " TEXT,"
                + KEY_MEMO + " TEXT,"
                + KEY_OPEN_DATE + " INTEGER,"
                + KEY_CLOSE_DATE + " INTEGER,"
                + KEY_CATEGORY + " TEXT,"
                + KEY_OPT_USE_DATE + " TEXT,"
                + KEY_OPT_PAY_DATE + " INTEGER,"
                + KEY_OPT_PAY_ACCOUNT_ID + " TEXT"
                +")"; 
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    /* (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);

        // Create tables again
        onCreate(db);
    }
    
    /**
     * Accounts 정보 레코드 추가
    * @param       info     DataClass of Recent Movie info
    * @return   Return true if it success, otherwise return false         
     * */
    public boolean addAccountEntity(AccountsEntity info){
        if(info == null){
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ACCOUNT_ID, info.account_id);
        values.put(KEY_ACCOUNT_TYPE, info.accountType);
        values.put(KEY_TYPE, info.type);
        values.put(KEY_TITLE, info.title);
        values.put(KEY_MEMO, info.memo);
        values.put(KEY_OPEN_DATE, info.open_date);
        values.put(KEY_CLOSE_DATE, info.close_date);
        values.put(KEY_CATEGORY, info.category);
        values.put(KEY_OPT_USE_DATE, info.opt_use_date);
        values.put(KEY_OPT_PAY_DATE, info.opt_pay_date);
        values.put(KEY_OPT_PAY_ACCOUNT_ID, info.opt_pay_account_id);
        
        long result = db.insert(TABLE_ACCOUNTS, null, values);
        if(result == -1){
            return false;
        }
        db.close();
        return true;
    }
    
    /**
     * Delete an account
     * @param   entity      entity to delete
     * @return      Return true if it success, or false 
     * */
    public boolean deleteAccount(AccountsEntity entity){
        if(entity == null){
            return false;
        }
        //String selectQuery = "DELETE FROM " + TABLE_ACCOUNTS + " WHERE account_id=" + entity.account_id;
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_ACCOUNTS, KEY_ACCOUNT_ID+"='" + entity.account_id + "'", null);        
        db.close();
        if(result == 0){
            return false;
        }            
        return true;
    }
    
    /**
     * Update account with given account entity info
     * @param   entity      entity information to update
     * @return   Return true if it success, or false
     * */
    public boolean updateAccount(AccountsEntity entity){
        if(entity == null){
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(KEY_ACCOUNT_TYPE, entity.accountType);
        values.put(KEY_TYPE, entity.type);
        values.put(KEY_TITLE, entity.title);
        values.put(KEY_MEMO, entity.memo);
        values.put(KEY_OPEN_DATE, entity.open_date);
        values.put(KEY_CLOSE_DATE, entity.close_date);
        values.put(KEY_CATEGORY, entity.category);
        values.put(KEY_OPT_USE_DATE, entity.opt_use_date);
        values.put(KEY_OPT_PAY_DATE, entity.opt_pay_date);
        values.put(KEY_OPT_PAY_ACCOUNT_ID, entity.opt_pay_account_id);        
        
        db.update(TABLE_ACCOUNTS, values, KEY_ACCOUNT_ID + "='" + entity.account_id+"'", null);
        db.close();
        return true;
    }
    

    /**
     * @return  Return all account information
     * */
    public ArrayList<AccountsEntity> getAllAccountsInfo() {
        ArrayList<AccountsEntity> entityList = new ArrayList<AccountsEntity>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ACCOUNTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AccountsEntity entityInfo = new AccountsEntity();
                entityInfo.account_id = cursor.getString(0);
                entityInfo.accountType = cursor.getString(1);
                entityInfo.type = cursor.getString(2);
                entityInfo.title = cursor.getString(3);
                entityInfo.memo = cursor.getString(4);
                entityInfo.open_date = Integer.parseInt(cursor.getString(5));
                entityInfo.close_date = Integer.parseInt(cursor.getString(6));
                entityInfo.category = cursor.getString(7);
                entityInfo.opt_use_date = cursor.getString(8);
                try{
                    entityInfo.opt_pay_date = Integer.parseInt(cursor.getString(9));
                }catch(NumberFormatException e){
                    entityInfo.opt_pay_date = 0;
                }
                entityInfo.opt_pay_account_id = cursor.getString(10);
                
                // Adding contact to list
                entityList.add(entityInfo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        
        if(entityList.isEmpty() == true){
            return null;
        }
        // return contact list
        return entityList;
    }
    
    /**
     * Find account entity by given title
     * @param       accountTitle        Account name
     * @return      Return AccountsEntity when DB helper found appreciate record
     * */
    public AccountsEntity getAccountById(String accountTitle){
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ACCOUNTS + " WHERE account_id = '"+ accountTitle + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.rawQuery(selectQuery, null);
        }catch(SQLiteException e){
            db.close();
            return null;
        }
        if(cursor.moveToFirst()){
            AccountsEntity entityInfo = new AccountsEntity();
            entityInfo.account_id = cursor.getString(0);
            entityInfo.accountType = cursor.getString(1);
            entityInfo.type = cursor.getString(2);
            entityInfo.title = cursor.getString(3);
            entityInfo.memo = cursor.getString(4);
            entityInfo.open_date = Integer.parseInt(cursor.getString(5));
            entityInfo.close_date = Integer.parseInt(cursor.getString(6));
            entityInfo.category = cursor.getString(7);
            entityInfo.opt_use_date = cursor.getString(8);
            try{
                entityInfo.opt_pay_date = Integer.parseInt(cursor.getString(9));
            }catch(NumberFormatException e){
                entityInfo.opt_pay_date = 0;
            }
            entityInfo.opt_pay_account_id = cursor.getString(10);
            db.close();
            return entityInfo;
        }
        return null;
    }

    /**
     * 최근 Accounts 테이블의 레코드 수를 리턴한다. 
     * @return  레코드수 리턴
     * */
    public int getAccountsInfoCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ACCOUNTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count;
    }

    /**
     * 마지막 업데이트 날짜를 리턴한다.
     * @return 데이터가 없는 등 문제가 생기면 0을 리턴, 그렇지 않으면 20121030과 같은 정수값을 리턴
     * */
    public int getLastUpdateDate(){
        String selectQuery = "SELECT  * FROM " + TABLE_ACCOUNTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            return Integer.parseInt(cursor.getString(11));
        }
        else{
            return 0;
        }
    }

    /**
     * 테이블 Drop 후 재생성
     * 
     * */
    public void clearTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        //Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);

        // Create tables again
        onCreate(db);
    }

}

/**
 * 
 */
package net.wisedog.android.whooing.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class SmsDbOpenHelper extends SQLiteOpenHelper {
    
    private static final int DATABASE_VERSION = 1;
    
    // Database Name
    public static final String DATABASE_NAME = "whooing";
    
    // SMS table name
    public static final String TABLE_SMS = "sms";
    
    // SMS Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_DATE = "date";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_ACCOUNT_ID = "account_id";
    public static final String KEY_MSG = "msg";

    /**
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public SmsDbOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    
    public SmsDbOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /* (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
    	Log.d("wisedog", "SMS Table is created");
    }

    /* (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
    	Log.i("wisedog", "on DB upgrade");
    	// Create in AccountsDbOpenHelper
    	/*String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
                + KEY_DATE + " INTEGER,"
                + KEY_AMOUNT + " REAL," 
                + KEY_ACCOUNT_ID + " TEXT,"
                + KEY_MSG + " TEXT"
                +")"; 
        db.execSQL(CREATE_TABLE);*/
    }
    
    /**
     * Accounts 정보 레코드 추가
    * @param       info     DataClass of Recent Movie info
    * @return   	Return affected records     
     * */
    public long addMessage(ContentValues values){
        if(values == null){
            return 0;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        long result = -1;
        try{
        	result = db.insert(TABLE_SMS, null, values);
        }catch(SQLException e){
        	e.printStackTrace();
        	result = -1;
        }
        db.close();
        if(result == -1){
            return 0;
        }
        return result;
    }
    
    /**
     * Delete an item which id is given param
     * @param	id		id to delete
     * @return	Returns how many records are affected
     * */
    public int deleteItem(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_SMS, KEY_ID+"='" + id + "'", null);        
        db.close();
        return result;
    }
    
    /**
     * Delete all record in table
     * */
    public int deleteAll(){
        Log.i("wisedog", "Delete All");
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_SMS, null, null);        
        db.close();
        return result;
    }
    
    /**
     * @return	Count of the table records
     * */
    public int getCount(){
    	String selectQuery = "SELECT * FROM " + TABLE_SMS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
    
    
    /**
     * @return  Return all account information
     * */
    public ArrayList<SmsInfoEntity> getAllAccountsInfo() {
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_SMS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try{
        	cursor = db.rawQuery(selectQuery, null);
        }
        catch(SQLException e){
        	e.printStackTrace();
        	db.close();
        	return null;
        }

        Log.i("wisedog", "Cursor Count : " + cursor.getCount());
        ArrayList<SmsInfoEntity> array = null;
        if(cursor.getCount() > 0){
        	array = new ArrayList<SmsInfoEntity>();
        	// looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                	SmsInfoEntity entity = new SmsInfoEntity();
                	entity.use_date = cursor.getInt(1);
                	entity.amount = cursor.getInt(2);
                	entity.account_id = cursor.getString(3);
                	entity.item = cursor.getString(4);
                	entity.msg = cursor.getString(5);
                	array.add(entity);
                } while (cursor.moveToNext());
            }
        }
        
        cursor.close();
        db.close();
        return array;
    }

}

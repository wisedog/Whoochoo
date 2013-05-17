/**
 * 
 */
package net.wisedog.android.whooing.db;

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
    
    private static final int DATABASE_VERSION = 2;
    
    // Database Name
    public static final String DATABASE_NAME = "whooing";
    
    // Contacts table name
    public static final String TABLE_SMS = "sms";
    
    // Contacts Table Columns names
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
    	String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
                + KEY_DATE + " INTEGER,"
                + KEY_AMOUNT + " REAL," 
                + KEY_ACCOUNT_ID + " TEXT,"
                + KEY_MSG + " TEXT"
                +")"; 
        db.execSQL(CREATE_TABLE);
    }
    
    /**
     * Accounts 정보 레코드 추가
    * @param       info     DataClass of Recent Movie info
    * @return   Return true if it success, otherwise return false         
     * */
    public boolean addMessage(ContentValues values){
        if(values == null){
            return false;
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
            return false;
        }
        return true;
    }
    
    public int deleteAll(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_SMS, KEY_ID+"='" + id + "'", null);        
        db.close();
        return result;
    }
    
    public int deleteItem(){
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_SMS, null, null);        
        db.close();
        return result;
    }
    
    /**
     * @return  Return all account information
     * */
    public void getAllAccountsInfo() {
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_SMS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Log.i("wisedog", "Cursor Count : " + cursor.getCount());
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Log.i("wisedog", "Column 0" + cursor.getString(0));
            	Log.i("wisedog", "Column 1" + cursor.getString(1));
            	Log.i("wisedog", "Column 2" + cursor.getString(2));
            	Log.i("wisedog", "Column 3" + cursor.getString(3));
            	Log.i("wisedog", "Column 4" + cursor.getString(4));
            
                
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

}

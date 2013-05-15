/**
 * 
 */
package net.wisedog.android.whooing.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class SmsDbOpenHelper extends SQLiteOpenHelper {
    
    private static final int DATABASE_VERSION = 1;
    
    // Database Name
    public static final String DATABASE_NAME = "whooing";
    
    // Contacts table name
    public static final String TABLE_SMS = "sms";
    
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_ACCOUNT_ID = "account_id";
    private static final String KEY_MSG = "msg";

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
        String CREATE_TABLE = "CREATE TABLE " + TABLE_SMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," 
                + KEY_DATE + " INTEGER,"
                + KEY_AMOUNT + " REAL," 
                + KEY_ACCOUNT_ID + " TEXT,"
                + KEY_MSG + " TEXT,"
                +")"; 
        db.execSQL(CREATE_TABLE);

    }

    /* (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // Do not support

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
        
        long result = db.insert(TABLE_SMS, null, values);
        if(result == -1){
            return false;
        }
        db.close();
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

}
